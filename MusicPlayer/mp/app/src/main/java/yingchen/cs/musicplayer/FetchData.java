package yingchen.cs.musicplayer;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


/**
 * Send http request to server using asyncTask.
 *
 * @author Ying Chen
 */

public class FetchData extends AsyncTask<String, Void, String> {


    private String result;
    private RunAfterExecute mRunAfterExecute;
    private int mPositionInNavigation;
    private static final  HttpGet HTTP_GET = new HttpGet("https://plasma-system-145121.appspot.com/hello");

    public FetchData(RunAfterExecute runAfterExecute, int positionInNavigation){
        mRunAfterExecute = runAfterExecute;
        mPositionInNavigation = positionInNavigation;
    }

    @Override
    protected String doInBackground(String... params) {
        String output = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(HTTP_GET);
            HttpEntity httpEntity = httpResponse.getEntity();
            output = EntityUtils.toString(httpEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    @Override
    protected void onPostExecute(String output) {
        Log.i("FetchData onPostExecute", "result is::::: "+ output);
        result = output;
        mRunAfterExecute.onPost(mPositionInNavigation, result);
    }

    public String getResult(){
        return result;
    }

    public static interface RunAfterExecute {
        public void onPost(int positionInNavi, String res);
    }

}


   /*
    @Override
    protected String doInBackground(String... urls) {
        String output = null;
        for (String url : urls) {
            output = getOutputFromUrl(url);
        }
        return output;
    }

    private String getOutputFromUrl(String url) {
        StringBuffer output = new StringBuffer("");
        try {
            InputStream stream = getHttpConnection(url);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
            String s = "";
            while ((s = buffer.readLine()) != null)
                output.append(s);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return output.toString();
    }

    // Makes HttpURLConnection and returns InputStream
    private InputStream getHttpConnection(String urlString)
            throws IOException {
        InputStream stream = null;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        try {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");

            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stream;
    }
    */