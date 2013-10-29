import javax.mail.*;
import java.io.IOException;
import java.util.Properties;
/**
 * Created with IntelliJ IDEA.
 * User: goncalodias
 * Date: 10/29/13
 * Time: 19:45
 * To change this template use File | Settings | File Templates.
 */


public class main {


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

      /*  if (p.isMimeType("multipart/alternative")) {
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

        }*/ else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }

        return null;
    }


    public static void main(String[] args) throws Exception {



        Properties props = new Properties();
        Part p;
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
        String message;
        for (Message m : messages) {
             message = getText( m);
            System.out.println("#####################");
            System.out.println("IS HTML: " + textIsHtml);
            System.out.println("From: " + m.);
            System.out.println("mail: " + message);
            System.out.println("#####################");
            System.out.println(" ");

        }
    }


}
