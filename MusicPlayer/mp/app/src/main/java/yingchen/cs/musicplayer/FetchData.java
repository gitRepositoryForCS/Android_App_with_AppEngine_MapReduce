package yingchen.cs.musicplayer;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import org.apache.http.client.methods.HttpGet;
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
    private static final HttpGet HTTP_GET = new HttpGet("https://plasma-system-145121.appspot.com/hello");
    private String TEST_FILE_PATH = "src/test/resources/mapReduce_output.txt";
    private HttpResponse mHttpResponse;

    public FetchData(RunAfterExecute runAfterExecute, int positionInNavigation){
        mRunAfterExecute = runAfterExecute;
        mPositionInNavigation = positionInNavigation;
    }

    public HttpResponse getHttpResponse(){
        return mHttpResponse;
    }
    @Override
    protected String doInBackground(String... params) {
        String output = null;
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            mHttpResponse = httpClient.execute(HTTP_GET);
            HttpEntity httpEntity = mHttpResponse.getEntity();
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
