package com.mycompany.app;

import org.apache.lucene.analysis.standard.UAX29URLEmailTokenizer;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.*;
import java.util.ArrayList;

public class Indexer {
    private IndexWriter writer;
    private static StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_45);

    private static String path="IndexerBD";

    public Indexer() {
        try {
            FSDirectory dir = FSDirectory.open(new File(path));

            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_45, analyzer);

            writer = new IndexWriter(dir, config);

        } catch (Exception ex) {
            System.out.println("Cannot create index..." + ex.getMessage());
            System.exit(-1);
        }

    }

    public void addIndex(String subject, String body, String date, String from, String to) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("subject", subject, Field.Store.YES));
        doc.add(new TextField("body", body, Field.Store.YES));
        // use a string field for isbn because we don't want it tokenized

        doc.add(new StringField("date", date, Field.Store.YES));
        doc.add(new StringField("from", from, Field.Store.YES));
        doc.add(new StringField("to", to, Field.Store.YES));

        writer.addDocument(doc);
        //System.out.println(writer.numDocs());
    }

    public void search(String s){

        try{
            IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(path)));
            IndexSearcher searcher = new IndexSearcher(reader);
            TopScoreDocCollector collector = TopScoreDocCollector.create(5, true);


            Query q = new QueryParser(Version.LUCENE_45, "body", analyzer).parse(s);
            searcher.search(q, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            // 4. display results
            System.out.println("Found " + hits.length + " hits.");
            for(int i=0;i<hits.length;++i) {
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
                System.out.println((i + 1) + ". " + d.get("path") + " score=" + hits[i].score);
            }


        }catch  (IOException e){
            System.out.println("Error open file (search function)");
        }catch (Exception e){
            System.out.println("Error (search function)");
        }
        //return result;
    }

    public void closeIndexer() throws IOException {
        writer.close();
    }
}
