package yingchen.cs.musicplayer;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

/*
import org.apache.http.HttpRequestFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
*/

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by yingchen on 10/6/16.
 */

public class SignInVerification extends AsyncTask<String, Void, Void> {
    private static final String TAG = "HttpRequestion.java!!!!";
    private String mIdToken;
    private static final String HTTPPOST_URL = "https://plasma-system-145121.appspot.com";

    @Override
    protected Void doInBackground(String... params) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(HTTPPOST_URL);
        try {
            List nameValuePairs = new ArrayList(1);
            nameValuePairs.add(new BasicNameValuePair("idToken", mIdToken));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            final String responseBody = EntityUtils.toString(response.getEntity());
            Log.i(TAG, "Signed in as: " + responseBody);
            if(statusCode == HttpStatus.SC_OK){
                Log.i(TAG, "user is verified in server. Status code is::::: " + statusCode);
            }
            else if(statusCode == HttpStatus.SC_NOT_FOUND){
                Log.i(TAG, "user denied. Status code is::::: " + statusCode);
            }
            else{ Log.i(TAG, "unexpected status code !!!! ");
            }

        } catch (ClientProtocolException e) {
            Log.e(TAG, "Error sending ID token to backend.", e);
        } catch (IOException e) {
            Log.e(TAG, "Error sending ID token to backend.", e);
        }
        return null;
    }
    protected void onPostExecute() {

    }
}

    /**
     * Fetch a list of the objects within the given bucket.
     *
     * @param bucketName the name of the bucket to list.
     * @return a list of the contents of the specified bucket.
     */
 /*   public static List<StorageObject> listBucket(String bucketName)
            throws IOException, GeneralSecurityException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        Storage client = StorageFactory.getService();
        Storage.Objects.List listRequest = client.objects().list(bucketName);

        List<StorageObject> results = new ArrayList<StorageObject>();
        Objects objects;

        // Iterate through each page of results, and add them to our results list.
        do {
            objects = listRequest.execute();
            // Add the items in this page of results to the list we'll return.
            results.addAll(objects.getItems());

            // Get the next page, in the next iteration of this loop.
            listRequest.setPageToken(objects.getNextPageToken());
        } while (null != objects.getNextPageToken());


        return results;
    }
*/

/*
    private void test(){
        String bucketName = "plasma-system-145121.appspot.com";
        GcsFilename fileName = new GcsFilename(bucketName, "testfile.txt");
        GcsService gcsService = GcsServiceFactory.createGcsService();

        GcsInputChannel channel = gcsService.openPrefetchingReadChannel(fileName,0,5000);
        ByteBuffer bb = ByteBuffer.allocateDirect(5000);

        try {
            StringBuilder sb = new StringBuilder();
            while(channel.read(bb) > 0){

                //limit is set to current position and position is set to zero
                bb.flip();
                while(bb.hasRemaining()){
                    char ch = (char) bb.get();
                    sb.append(ch);
                    // resp.getWriter().println(ch);
                }
            }
            if(sb.length() > 0){
                Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_LONG).show();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        channel.close();
    }
    */

/*
    private void testAccessGCS(){
        String STORAGE_SCOPE = "https://www.googleapis.com/auth/devstorage.read_write";
        JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

        Log.d("testing", "checking if I can create a credential");
        httpTransport = AndroidHttp.newCompatibleTransport();
        KeyStore keystore = null;
        try {
            keystore = KeyStore.getInstance("PKCS12");
            keystore.load(resources_.openRawResource(R.raw.gcs_privatekey),
                    "password".toCharArray());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }


        PrivateKey key = null;
        try {
            key = (PrivateKey) keystore.getKey("privatekey", "password".toCharArray());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(JSON_FACTORY)
                .setServiceAccountPrivateKey(key)
                .setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
                .setServiceAccountScopes(Collections.singleton(STORAGE_SCOPE))
                // .setServiceAccountUser(SERVICE_ACCOUNT_EMAIL)
                // .setClientSecrets(CLIENT_ID, CLIENT_SECRET)
                .build();
        credential.refreshToken();

        String URI = "https://storage.googleapis.com/" + BUCKET_NAME;
        HttpRequestFactory requestFactory = httpTransport.createRequestFactory(credential);
        GenericUrl url = new GenericUrl(URI);
        SignInVerification request = requestFactory.buildGetRequest(url);
        HttpResponse response = request.execute();
        String content = response.parseAsString();
        Log.d("testing", "response content is: " + content);
        new Storage.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName("appname").build();
    }
*/
   // /*

   // */
