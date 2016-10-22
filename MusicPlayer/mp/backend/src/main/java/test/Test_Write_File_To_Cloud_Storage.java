package test;

import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;

import junit.framework.Assert;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

 /*
     * MapReduce output file is written to cloud storage by GoogleCloudStorageFileOutput
     * and GoogleCloudStorageFileOutputWriter
 */

/**
 * This test is making sure the way writing files to cloud storage is correct.
 * Notice: this test won't pass if run locally. But it works when deploying to the cloud.
 *
 * @author Ying Chen
 */
public class Test_Write_File_To_Cloud_Storage {


  @org.testng.annotations.Test
    public static void testWriteChannel()  {
        GcsService gcsService = GcsServiceFactory.createGcsService();
        GcsFilename file = new GcsFilename(Constants.BUCKETNAME, Constants.INPUTFILE);

      GcsOutputChannel writeChannel = null;
      try {
          writeChannel = gcsService.createOrReplace(file,
                  new GcsFileOptions.Builder().mimeType(Constants.MIMETYPE).build());
      } catch (IOException e) {
          Assert.fail();
      }

      String fn = null;
      try {
          writeChannel.write(ByteBuffer.wrap("testWriteChannel".getBytes()));
          fn = gcsService.getMetadata(file).getFilename().getBucketName();
      } catch (IOException e) {
          Assert.fail();
      }

      assertThat(Constants.BUCKETNAME, is(fn));
    }

}
