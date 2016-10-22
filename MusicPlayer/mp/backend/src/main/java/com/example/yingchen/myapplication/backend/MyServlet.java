package com.example.yingchen.myapplication.backend;
/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.util.Arrays;
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
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.MemcacheService;

import java.security.SecureRandom;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.tools.pipeline.PipelineServiceFactory;
import com.google.appengine.tools.pipeline.PipelineService;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;


public class MyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String CLIENT_ID = “your android app client id“;
    private static Logger Log = Logger.getLogger("com.example.yingchen.myapplication.backend.MyServlet");
    private static String OUTPUT_PATH = "/WEB-INF/mapReduce_output.txt";

    public MyServlet() {
        super();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        ServletContext sc = getServletConfig().getServletContext();
        RequestDispatcher rd = sc.getRequestDispatcher(OUTPUT_PATH);
        rd.forward(req,response);


        //   resp.setContentType("text/plain");
        // resp.getWriter().println("Hello I am the result dopost!!!!");
        // resp.getWriter().println("this is a test. ");
        /* String name = req.getParameter("name");
        resp.setContentType("text/plain");
        if(name == null) { resp.getWriter().println("Please enter a name");}
        resp.getWriter().println("Hello " + name);
        */

    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PipelineService service = PipelineServiceFactory.newPipelineService();
        redirectToPipelineStatus(resp, service.startNewPipeline(new GetRankChainingMapReduceJob() ));
    }

    private void redirectToPipelineStatus(HttpServletResponse resp, String pipelineId) throws IOException {
        String destinationUrl = getPipelineStatusUrl(pipelineId);
        // log.info("Redirecting to " + destinationUrl);
        resp.sendRedirect(destinationUrl);
    }

    private String getPipelineStatusUrl(String pipelineId) {
        return "/_ah/pipeline/status.html?root=" + pipelineId;
    }

    /*
     * This method verified the user.
     */
    private Payload handleSignIn(String idTokenString) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                UrlFetchTransport.getDefaultInstance(), new JacksonFactory())
                .setAudience(Arrays.asList(CLIENT_ID))
                // If you retrieved the token on Android using the Play Services 8.3 API or newer, set
                // the issuer to "https://accounts.google.com". Otherwise, set the issuer to
                // "accounts.google.com". If you need to verify tokens from multiple sources, build
                // a GoogleIdTokenVerifier for each issuer and try them both.
                .setIssuer("https://accounts.google.com")
                .build();

        // (Receive idTokenString by HTTPS POST)
        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (idToken != null) return idToken.getPayload();
        else {
            System.out.println("Invalid ID token.");
            return null;
        }
    }

    /*
     * This method saves verified user's info for later use, such as returns to client when needed.
     */
    private void saveUserData(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idTokenString = req.getParameter("idToken");
        Payload payload = handleSignIn(idTokenString);

        if (payload != null) {

            PrintWriter pw = new PrintWriter(resp.getOutputStream());
            if (pw != null) {
                String userId = payload.getSubject();
                //System.out.println("User ID: " + userId);

                // Get profile information from payload
                String email = payload.getEmail();
                boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");
                String locale = (String) payload.get("locale");
                String familyName = (String) payload.get("family_name");
                String givenName = (String) payload.get("given_name");

                pw.println(email + "     " + emailVerified + "     " + name + "   " +
                        pictureUrl + "    " + locale + "     " + familyName + "    " + givenName);
                // save to cloud storage...
            }
        }
    }
}






