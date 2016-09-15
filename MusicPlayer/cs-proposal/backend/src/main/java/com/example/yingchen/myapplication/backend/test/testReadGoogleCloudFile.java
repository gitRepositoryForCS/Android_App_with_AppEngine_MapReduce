import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by yingchen on 6/24/16.
 */
public class testReadGoogleCloudFile {

    public static void main(String[] args) throws IOException{
        String bucketName = "mybucket.appspot.com";
        GcsFilename fileName = new GcsFilename(bucketName, "testfile.txt");
        GcsService gcsService = GcsServiceFactory.createGcsService();

        GcsInputChannel channel = gcsService.openPrefetchingReadChannel(fileName,0,5000);
        ByteBuffer bb = ByteBuffer.allocateDirect(5000);

        while(channel.read(bb) > 0){

            //limit is set to current position and position is set to zero
            bb.flip();

            while(bb.hasRemaining()){
                char ch = (char) bb.get();
               // resp.getWriter().println(ch);
            }
        }
        channel.close();


         /* GcsOutputChannel writeChannel = gcsService.createOrReplace(
               fileName , new GcsFileOptions.Builder().mimeType("text/plain").build());//application/bin").build());
        for (int i = 0; i < 10; i++) {
            writeChannel.write(ByteBuffer.wrap("123456".getBytes()));
        }
        writeChannel.close();
        */

    }
}
