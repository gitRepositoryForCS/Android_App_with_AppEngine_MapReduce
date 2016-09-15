/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/
/**
 * Created by yingchen on 6/20/16.
 */
import java.io.IOException;
import javax.servlet.http.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.firebase.client.DataSnapshot;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.Transport;
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import com.firebase.client.FirebaseError;
import java.io.PrintWriter;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.MemcacheService;
import java.security.SecureRandom;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class MyServlet extends HttpServlet {
    static Logger Log = Logger.getLogger("MyServlet");
    private final MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
    private final UserService userService = UserServiceFactory.getUserService();
    private final SecureRandom random = new SecureRandom();


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        //auto-generated code.
        //resp.setContentType("text/plain");
        //resp.getWriter().println("Please use the form to POST to this url");
        Log.info("Got cron message, constructing email.");

        //Create a new Firebase instance and subscribe on child events.
        Firebase firebase = new Firebase("https://myproject.firebaseio.com/todoItems");
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Build the email message contents using every field from Firebase.
                final StringBuilder newItemMessage = new StringBuilder();
                newItemMessage.append("Good Morning!  You have the following todo items:\n");
                for (DataSnapshot todoItem : dataSnapshot.getChildren()) {
                    for (DataSnapshot field : todoItem.getChildren()) {
                        newItemMessage.append(field.getKey())
                                .append(":")
                                .append(field.getValue().toString())
                                .append("\n");
                    }
                }
                //Now Send the email
                Properties props = new Properties();
                Session session = Session.getDefaultInstance(props, null);
                try {
                    Message msg = new MimeMessage(session);
                    //Make sure you substitute your project-id in the email From field
                    msg.setFrom(new InternetAddress("yingchen@mybucket.appspotmail.com",
                            "Todo List"));
                    msg.addRecipient(Message.RecipientType.TO,
                            new InternetAddress("myGmail@gmail.com", "Recipient"));
                    msg.setSubject("Good Morning!");
                    msg.setText(newItemMessage.toString());
                    Transport.send(msg);
                } catch (MessagingException | UnsupportedEncodingException e) {
                    Log.warning(e.getMessage());
                }
            }
            public void onCancelled(FirebaseError firebaseError) { }
        });


    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
       /* String name = req.getParameter("name");
        resp.setContentType("text/plain");
        if(name == null) { resp.getWriter().println("Please enter a name");}
        resp.getWriter().println("Hello " + name);
        */
       // writeResponse(resp);
            MyMapReduceJob myjob = new MyMapReduceJob();
            String id = myjob.run();
            resp.getWriter().println("My MapReduce job ID is: "+ id);
    }


    private void writeResponse(HttpServletResponse resp) throws IOException {
       // String token = String.valueOf(random.nextLong() & Long.MAX_VALUE);
      //  memcache.put(userService.getCurrentUser().getUserId() + " " + token, true);
        PrintWriter pw = new PrintWriter(resp.getOutputStream());
        if(pw !=null){
            pw.println(
                    "<html><head>"
                            + "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">"
                            + "<title>Big Query data load</title></head>" + "<body>"
                            + "<div> Note : This example does not work on locally devappserver.</div>"
                            + "<div id=\"loadform\"><label>Loads the given number of rows(less than 10,000)"
                            + " into bigquery table bigquery_example:example_table.</label></div>"
                            + "<form action=\"/randomDataLoad\" method=\"post\">"
                            + "<label>Number of rows to load</label><input value='1000' name='row_count' />"
                            + "<div><input type=\"submit\" value=\"Load\"></div>" + "</form></body></html>");
        }
    }
}

/*
    public static Bucket getBucket(String bucketName) throws IOException, GeneralSecurityException {
        Storage client = StorageFactory.getService();

        Storage.Buckets.Get bucketRequest = client.buckets().get(bucketName);
        // Fetch the full set of the bucket's properties (e.g. include the ACLs in the response)
        bucketRequest.setProjection("full");
        return bucketRequest.execute();
    }
*/

/*
    private GcsFilename getFileName(HttpServletRequest req) {
        String[] splits = req.getRequestURI().split("/", 4);
        if (!splits[0].equals("") || !splits[1].equals("gcs")) {
            throw new IllegalArgumentException("The URL is not formed as expected. " +
                    "Expecting /gcs/<bucket>/<object>");
        }
        return new GcsFilename(splits[2], splits[3]);
    }
*/






