package com.mycompany.app;

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
    private static StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);

    public Indexer() {
        try {
            FSDirectory dir = FSDirectory.open(new File("IndexerBD"));

            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);

            writer = new IndexWriter(dir, config);

        } catch (Exception ex) {
            System.out.println("Cannot create index..." + ex.getMessage());
            System.exit(-1);
        }

    }

    public void addIndex(String subject, String body, String date, String from, String to) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("", subject, Field.Store.YES));
        doc.add(new TextField("", body, Field.Store.YES));

        // use a string field for isbn because we don't want it tokenized
        doc.add(new StringField("date", date, Field.Store.YES));
        doc.add(new StringField("from", from, Field.Store.YES));
        doc.add(new StringField("to", to, Field.Store.YES));

        writer.addDocument(doc);
    }

    public void closeIndexer() throws IOException {
        writer.close();
    }
}
