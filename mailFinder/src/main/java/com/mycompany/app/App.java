package com.mycompany.app;


import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.standard.UAX29URLEmailTokenizer;

import java.io.File;

import java.io.IOException;

public class App {



    public static void main(String[] args) throws Exception {

        //importar mails da caixa
        Mail mail = new Mail();
        mail.importMail();


        WhitespaceAnalyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_45);

        try{
            IndexReader reader = DirectoryReader.open(FSDirectory.open(new File("IndexerBD")));
            IndexSearcher searcher = new IndexSearcher(reader);
            TopScoreDocCollector collector = TopScoreDocCollector.create(5, true);

            Query q = new QueryParser(Version.LUCENE_45, "to", analyzer).parse("jaguiar");
            System.out.println(q);

            searcher.search(q, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            // 4. display results
            System.out.println("Found " + hits.length + " hits.");
            for(int i=0;i<hits.length;++i) {
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
                System.out.println((i + 1) + ". " + d.get("from") + " score=" + hits[i].score);
            }


        }catch  (IOException e){
            System.out.println("Error open file (search function)");
        }catch (Exception e){
            System.out.println("Error (search function)");
        }

    }

}