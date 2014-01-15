package com.mycompany.app;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
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

    public Indexer(String path) {
        try {

            this.path=path;

            FSDirectory dir = FSDirectory.open(new File(this.path));

            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_45, analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

            md = MessageDigest.getInstance("MD5");

            writer = new IndexWriter(dir, config);



        } catch (Exception ex) {
            System.out.println("Cannot create index..." + ex.getMessage());
            System.exit(-1);
        }

    }



    public static String md5Spring(String text){
        return DigestUtils.md5Hex(text);
    }


    public void addIndex(Email email,String categories) throws IOException {


        try {

            String hash = md5Spring(email.getBody());
            System.out.println(hash);
            ScoreDoc[] hits=null;
            try{
                IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(path)));
                searcher = new IndexSearcher(reader);



                //QueryParser parser = new QueryParser(Version.LUCENE_45, "hash", analyzer);
                //parser.setDefaultOperator(QueryParser.AND_OPERATOR);
                //Query q1 =parser.parse(hash);

                //parser = new QueryParser(Version.LUCENE_45, "from", analyzer);
                //parser.setDefaultOperator(QueryParser.AND_OPERATOR);
                //Query q2 = parser.parse(email.getFrom());




                //BooleanQuery firstHalf = new BooleanQuery();
                //firstHalf.add(q1, BooleanClause.Occur.MUST);
                //firstHalf.add(q2, BooleanClause.Occur.MUST);

                //parser.setDefaultOperator(QueryParser.AND_OPERATOR);



                Query q = new QueryParser(Version.LUCENE_45, "hash", analyzer).parse(hash);
                System.out.println(q);
                TopScoreDocCollector collector = TopScoreDocCollector.create(10, true);

                searcher.search(q, collector);
                hits = collector.topDocs().scoreDocs;


                //searcher.search(firstHalf, collector);
               // searcher.search(q, collector);

                //hits = collector.topDocs().scoreDocs;
                System.out.println(hits.length);
                reader.close();

            }catch (Exception e){
                System.out.println("First Entry");
            }


            if (hits==null || hits.length == 0) {
                System.out.println("add");
                Document doc = new Document();
                doc.add(new IntField("id", email.getId(), Field.Store.YES));
                doc.add(new TextField("subject", email.getSubject(), Field.Store.YES));
                doc.add(new TextField("body", email.getBody(), Field.Store.YES));

                // use a string field for isbn because we don't want it tokenized

                doc.add(new StringField("date", email.getDate(), Field.Store.YES));
                doc.add(new StringField("from", email.getFrom(), Field.Store.YES));
                doc.add(new StringField("to", email.getTo(), Field.Store.YES));
                doc.add(new IntField("polarity", email.getPolarity(), Field.Store.YES));
                doc.add(new StringField("hash", hash, Field.Store.YES));
                if  (!categories.isEmpty()){
                    doc.add(new StringField("categories", categories, Field.Store.YES));
                }
                Term aux = new Term(email.getBody());

                writer.updateDocument(aux, doc);
                System.out.println(writer.numDocs());

            } else {
                System.out.println("Duplicado");
            }
        } catch (Exception e) {
            System.out.println("Execption");
        }

    }
    public static ArrayList<Email> getAllMail(){
        //String aux="";
        ArrayList<Email> aux= new ArrayList<Email>();


        try{
            IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(path)));


            for (int i=0; i<reader.maxDoc(); i++) {
                Document doc = reader.document(i);

                Email email= new Email(Integer.parseInt(doc.get("id")),doc.get("body"), doc.get("date"),  Integer.parseInt(doc.get("polarity")), doc.get("from"), doc.get("to"), doc.get("subject"));

                aux.add(email);

            }
        }catch  (IOException e){
            System.out.println("Error open file (search function)");
        }catch (Exception e){
            System.out.println("Error (search function)");
        }

        return aux;

    }

    public static String getAllInfo(){
        String aux="";

        try{
            IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(path)));


            for (int i=0; i<reader.maxDoc(); i++) {
                Document doc = reader.document(i);
                String body = doc.get("body");
                String id = doc.get("ID");

                aux+= id + "\t" + "X" + "\t" + body;

            }
        }catch  (IOException e){
            System.out.println("Error open file (search function)");
        }catch (Exception e){
            System.out.println("Error (search function)");
        }

        return aux;

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
