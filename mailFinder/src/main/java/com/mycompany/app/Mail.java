package com.mycompany.app;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;
import java.util.Scanner;


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
            String date;

            Calendar cal = Calendar.getInstance();

            System.out.println("Start Importing....");
            Indexer index = new Indexer();
            int a = 0;
            for (Message m : messages) {

                from = "";
                body = getText(m);


                Address[] in = m.getFrom();
                for (Address address : in) {
                    from = getMail(address.toString());
                }
                to = getMail(InternetAddress.toString(m.getAllRecipients()));
                subject = m.getSubject();

                cal.setTime(m.getSentDate());

                date = String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) + "/"
                        + String.valueOf(cal.get(Calendar.MONTH) + 1) + "/"
                        + String.valueOf(cal.get(Calendar.YEAR)) + " "
                        + String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + ":"
                        + String.valueOf(cal.get(Calendar.MINUTE)) + ":"
                        + String.valueOf(cal.get(Calendar.SECOND));


                if (to.compareTo("Not found") == 0 || from.isEmpty()) {
                    System.out.println(to);
                    continue;
                }

                index.addIndex(subject, body, date, from, to);
                System.out.println("From: " + from);
                // System.out.println("To: " + to);

                //System.out.println("Subject: " + subject);
                //System.out.println("Body: " + body);
                //System.out.println("Date: " + date);

                a++;

            }
            System.out.println(a);
            index.closeIndexer();
            System.out.println("Finished");
        } catch (Exception mex) {
            mex.printStackTrace();
        }


    }

}