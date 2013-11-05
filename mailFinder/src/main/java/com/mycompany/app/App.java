import javax.mail.*;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;



/**
 * Created with IntelliJ IDEA.
 * User: goncalodias
 * Date: 10/29/13
 * Time: 23:36
 * To change this template use File | Settings | File Templates.
 */
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

        int count = 0;
        for (Message m : messages) {
            count++;

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


            System.out.println("****************************");

            System.out.println("Number: " + count);
            System.out.println("From: " + from);
            System.out.println("To: " + to);
            System.out.println("Subject: " + subject);
            System.out.println("Body: " + body);
            System.out.println("Date: " + date);




            System.out.println(" ");

        }
    }
}