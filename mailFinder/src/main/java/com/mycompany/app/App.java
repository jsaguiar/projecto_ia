package com.mycompany.app;

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

import javax.mail.*;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;


public class App {



    static boolean textIsHtml = false;

    /**
     //     * Return the primary text content of the message.
     */
    static String getText(Part p) throws
            MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String)p.getContent();
            textIsHtml = p.isMimeType("text/html");
            return s;
        }

        else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }

        return null;
    }


    static String getMail(String [] fields){

        String mail;



        for (int i = 0; i< fields.length; i++){
            if(fields[i].contains("@")){
                mail = fields[i];
                mail = mail.replace("<","");
                mail = mail.replace(">","");
                mail = mail.replace("\"","");
                return mail.replace(",","");
            }
        }

        return "Not found";
    }



    public static void main(String[] args) throws Exception {



        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "mstor");
        props.setProperty("mstor.mbox.metadataStrategy", "none");
        props.setProperty("mstor.mbox.cacheBuffers", "disabled");
        props.setProperty("mstor.cache.disabled", "true");
        props.setProperty("mstor.mbox.bufferStrategy", "mapped");
        props.setProperty("mstor.metadata", "disabled");

        props.setProperty("mstor.mbox.metadataStrategy", "xml");
        Session session1 = Session.getDefaultInstance(props);

        Session session = Session.getDefaultInstance(new Properties());

        Store store = session.getStore(new URLName("mstor:mbox"));
        store.connect();

        System.out.println(store.isConnected());

        Folder inbox = store.getDefaultFolder();  // no subfolder here; even if there is an Inbox, I get the same thing...
        inbox.open(Folder.READ_ONLY);

        Message [] messages = inbox.getMessages();

        String body;
        String from;
        String to;
        String subject;
        String date;

        Calendar cal = Calendar.getInstance();

        Indexer index = new Indexer();
       /* for (Message m : messages) {

            body = getText(m);
            to = getMail(InternetAddress.toString(m.getAllRecipients()).split(" "));
            from = getMail(InternetAddress.toString(m.getReplyTo()).split(" "));
            subject = m.getSubject();

            cal.setTime(m.getSentDate());

            date = String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) + "/"
                    + String.valueOf(cal.get(Calendar.MONTH)+1) + "/"
                    + String.valueOf(cal.get(Calendar.YEAR)) + " "
                    + String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + ":"
                    + String.valueOf(cal.get(Calendar.MINUTE)) + ":"
                    + String.valueOf(cal.get(Calendar.SECOND));

            if(to.compareTo("Not found") == 0 || from.compareTo("Not found") == 0){
                break;
            }

            index.addIndex(subject,body,date,from,to);
            System.out.println("From: " + from);
            System.out.println("To: " + to);
            System.out.println("Subject: " + subject);
            System.out.println("Body: " + body);
            System.out.println("Date: " + date);

        }     */

        System.out.println("FINISH");
        index.closeIndexer();


        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_45);

        try{
            IndexReader reader = DirectoryReader.open(FSDirectory.open(new File("IndexerBD")));
            IndexSearcher searcher = new IndexSearcher(reader);
            TopScoreDocCollector collector = TopScoreDocCollector.create(5, true);

            Query q = new QueryParser(Version.LUCENE_45, "data", analyzer).parse("30/09/2013");
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