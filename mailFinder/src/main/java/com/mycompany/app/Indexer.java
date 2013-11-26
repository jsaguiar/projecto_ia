package com.mycompany.app;

import com.sun.xml.internal.xsom.impl.Ref;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.UAX29URLEmailTokenizer;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.math.BigInteger;
import java.security.*;
import java.nio.charset.Charset;


import org.apache.lucene.index.Term;

import java.io.*;
import java.util.ArrayList;

public class Indexer {
    private IndexWriter writer;
    private IndexSearcher searcher;
    private static WhitespaceAnalyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_45);
    MessageDigest md;

    private static String path = "IndexerBD";

    public Indexer() {
        try {
            FSDirectory dir = FSDirectory.open(new File(path));

            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_45, analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

            md = MessageDigest.getInstance("MD5");

            writer = new IndexWriter(dir, config);



        } catch (Exception ex) {
            System.out.println("Cannot create index..." + ex.getMessage());
            System.exit(-1);
        }

    }

    public void addIndex(String subject, String body, String date, String from, String to, int polarity) throws IOException {


        try {


            byte[] bytesOfMessage = body.getBytes("UTF-8");


            md.reset();

            md.update(body.getBytes(Charset.forName("UTF8")));
            final byte[] resultByte = md.digest();

            BigInteger bigInt = new BigInteger(1,resultByte);
            String hash = bigInt.toString(16);



            ScoreDoc[] hits=null;
            try{
                IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(path)));
                searcher = new IndexSearcher(reader);


                QueryParser parser = new QueryParser(Version.LUCENE_45, "hash", analyzer);
                parser.setDefaultOperator(QueryParser.AND_OPERATOR);
                Query q1 =parser.parse(hash);

                parser = new QueryParser(Version.LUCENE_45, "from", analyzer);
                parser.setDefaultOperator(QueryParser.AND_OPERATOR);
                Query q2 = parser.parse(from);




                BooleanQuery firstHalf = new BooleanQuery();
                firstHalf.add(q1, BooleanClause.Occur.MUST);
                firstHalf.add(q2, BooleanClause.Occur.MUST);

                parser.setDefaultOperator(QueryParser.AND_OPERATOR);


                TopScoreDocCollector collector = TopScoreDocCollector.create(5, true);

                searcher.search(firstHalf, collector);
                hits = collector.topDocs().scoreDocs;
            }catch (Exception e){
                System.out.println("First Entry");
            }


            if (hits==null || hits.length == 0) {
                System.out.println("add");
                Document doc = new Document();
                doc.add(new TextField("subject", subject, Field.Store.YES));
                doc.add(new TextField("body", body, Field.Store.YES));

                // use a string field for isbn because we don't want it tokenized

                doc.add(new StringField("date", date, Field.Store.YES));
                doc.add(new StringField("from", from, Field.Store.YES));
                doc.add(new StringField("to", to, Field.Store.YES));
                doc.add(new IntField("polarity", polarity, Field.Store.YES));
                doc.add(new StringField("hash", hash, Field.Store.YES));

                Term aux = new Term(body);

                writer.updateDocument(aux, doc);
                System.out.println(writer.numDocs());

            } else {
                System.out.println("Duplicado");
            }
        } catch (Exception e) {
            System.out.println("Execption");
        }

    }

    public void search(String s) {

        try {

            TopScoreDocCollector collector = TopScoreDocCollector.create(5, true);

            Query q = new QueryParser(Version.LUCENE_45, "body", analyzer).parse(s);
            searcher.search(q, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            // 4. display results
            System.out.println("Found " + hits.length + " hits.");
            for (int i = 0; i < hits.length; ++i) {
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
                System.out.println((i + 1) + ". " + d.get("path") + " score=" + hits[i].score);
            }


        } catch (IOException e) {
            System.out.println("Error open file (search function)");
        } catch (Exception e) {
            System.out.println("Error (search function)");
        }
        //return result;
    }

    public void closeIndexer() throws IOException {
        writer.forceMerge(1);
        writer.commit();
        writer.close();
    }
}
