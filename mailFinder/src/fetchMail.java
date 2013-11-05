import javax.mail.*;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.*;



/**
 * Created with IntelliJ IDEA.
 * User: goncalodias
 * Date: 10/29/13
 * Time: 23:36
 * To change this template use File | Settings | File Templates.
 */
public class fetchMail {



    static boolean textIsHtml = false;

    /**
     * Return the primary text content of the message.
     */
    static String getText(Part p) throws
            MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String)p.getContent();
            textIsHtml = p.isMimeType("text/html");
            return s;
        }

     /*   if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart)p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } */else if (p.isMimeType("multipart/*")) {
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
                if (mail.contains("<") || mail.contains("\"")) {
                    mail = mail.substring(1,mail.length()-1);
                }

                return mail;
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

        String current;
        String from;
        int count = 0;
        for (Message m : messages) {
            count++;

            current = getText(m);

            from = getMail(InternetAddress.toString(m.getAllRecipients()).split(" "));


            System.out.println("****************************");
            System.out.println("nr: " + count);
            System.out.println("From: " + from);
            System.out.println("mail: " + current);



            System.out.println(" ");

        }
    }
}