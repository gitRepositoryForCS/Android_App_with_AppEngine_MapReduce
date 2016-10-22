package test;

import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;

import junit.framework.Assert;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by yingchen on 10/19/16.
 */

public class Test_Read_File_From_Cloud_Storage {


    /*
    * This test is making sure the way reading files from cloud storage is correct.
    * Notice: this test won't pass if run locally. But it works when deploying to the cloud.
    * */
   // /*
    @org.testng.annotations.Test
    public void test_Testing_Framework() {

        GcsFilename fileName = new GcsFilename(Constants.BUCKETNAME, Constants.FILE_FOR_TESTING);
        GcsService gcsService = GcsServiceFactory.createGcsService();

        GcsInputChannel channel = gcsService.openPrefetchingReadChannel(fileName, 0, 5000);
        ByteBuffer bb = ByteBuffer.allocateDirect(5000);

        StringBuilder sb = new StringBuilder();
        try {
            while (channel.read(bb) > 0) {
                //limit is set to current position and position is set to zero
                bb.flip();
                while (bb.hasRemaining()) {
                    char ch = (char) bb.get();
                    sb.append(ch);
                }
            }
        } catch (IOException e) {
            Assert.fail();
        }
        channel.close();
        assertThat(sb.toString(),is("This is a test."));
    }
  //  */
}
