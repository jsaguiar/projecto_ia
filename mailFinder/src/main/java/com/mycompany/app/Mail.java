package com.mycompany.app;


import org.apache.lucene.document.DateTools;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Properties;


public class Mail {
    String pass = "wellcare";


    static boolean textIsHtml = false;

    /**
     * //     * Return the primary text content of the message.
     */
    static String getText(Part p) throws
            MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String) p.getContent();
            textIsHtml = p.isMimeType("text/html");
            return s;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }

        return null;
    }


    static String getMail(String aux) {

        String mail;

        String fields[] = aux.split(" ");

        for (int i = 0; i < fields.length; i++) {
            if (fields[i].contains("@")) {
                mail = fields[i];
                mail = mail.replace("<", "");
                mail = mail.replace(">", "");
                mail = mail.replace("\"", "");
                return mail.replace(",", "");
            }
        }

        return "Not found";
    }


    public void importMail() throws Exception {

        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "pop3");
        try {
            Session session = Session.getInstance(props, null);
            Store store = session.getStore();
            store.connect("student.dei.uc.pt", "jaguiar", pass);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message[] messages = inbox.getMessages();
            String body;
            String from;
            String to;
            String subject;
            Date date;
            String sDate;
            String bodyAux;
            String category;
            int polarity;
            int pos=0,neg=0,neu=0;


            Polarity mailPolarity = new Polarity("SentiLex");

            System.out.println("Start Importing....");
            Indexer index = new Indexer();
            int a = 0;
            for (Message m : messages) {

                from = "";
                body = getText(m);
                bodyAux=body;


                bodyAux = bodyAux.toLowerCase();

                bodyAux=bodyAux.replaceAll("[^a-zA-Z]"," ");


                Analyzer analyzer = new Analyzer();



                Address[] in = m.getFrom();
                for (Address address : in) {
                    from = getMail(address.toString());
                }
                to = getMail(InternetAddress.toString(m.getAllRecipients()));
                subject = m.getSubject();


                category = analyzer.analyzeTerm(mailPolarity, "feio 1213442 asasas mau porco");



                date = m.getSentDate();

                SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy:hh:mm:ss");
                sDate = DATE_FORMAT.format(date);

                sDate = DateTools.dateToString(date, DateTools.Resolution.SECOND);


                if (to.compareTo("Not found") == 0 || from.isEmpty()) {
                    continue;
                }

                a++;

                System.out.println(a);
                if(a>=122 && a<=133){
                    System.out.println("Body: " + body);

                }

                if (category.compareTo("Positivos")==0){
                    polarity=1;
                    pos++;
                }
                else if(category.compareTo("Negativos")==0){
                    polarity=-1;

                    neg++;
                }
                else {
                    polarity=0;

                    neu++;
                }


                index.addIndex(subject, body, sDate, from, to, polarity);
                //System.out.println("From: " + from);
                // System.out.println("To: " + to);

                //System.out.println("Subject: " + subject);
                //System.out.println("Body: " + body);
                //System.out.println("Date: " + sDate);
                System.out.println("Polarity: " + category);


                System.out.println("#######################################");




            }
            index.closeIndexer();
            System.out.println("Positivos: " + pos + "\n" + "Negativos: " + neg + "\n" + "Neutros: " + neu);

            System.out.println("Finished");
        } catch (Exception mex) {
            mex.printStackTrace();
        }


    }

}