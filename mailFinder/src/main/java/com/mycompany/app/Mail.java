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
            int polarity;
            int id=0;



            System.out.println("Start Importing....");
            Indexer index = new Indexer();
            for (Message m : messages) {

                id++;
                from = "";
                body = getText(m);
                bodyAux=body;


                bodyAux = bodyAux.toLowerCase();

                bodyAux=bodyAux.replaceAll(",", "");

                HashOps polOps = new HashOps();

                polarity= polOps.getPolarity(bodyAux);


                Address[] in = m.getFrom();
                for (Address address : in) {
                    from = getMail(address.toString());
                }
                to = getMail(InternetAddress.toString(m.getAllRecipients()));
                subject = m.getSubject();

                date = m.getSentDate();

                SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy:hh:mm:ss");
                sDate = DATE_FORMAT.format(date);

                sDate = DateTools.dateToString(date, DateTools.Resolution.SECOND);


                if (to.compareTo("Not found") == 0 || from.isEmpty()) {
                    continue;
                }


                Email email= new Email(id,body, sDate, polarity, from, to, subject) ;

                index.addIndex(email,"");

                System.out.println("#######################################");




            }
            index.closeIndexer();

            System.out.println("Finished");
        } catch (Exception mex) {
            mex.printStackTrace();
        }


    }

}