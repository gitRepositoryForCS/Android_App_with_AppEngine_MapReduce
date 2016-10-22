package test;

import com.google.api.services.storage.Storage;
import com.google.api.services.storage.model.Bucket;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * This class unit tests the way of getting bucket in cloud storage is correct.
 *
 * @author Ying Chen
 */

public class Test_getBucket_In_Cloud_Storage {

    @org.testng.annotations.Test
    public static void testGetBucket() throws IOException, GeneralSecurityException {
        Storage client = StorageFactory.getService();

        Storage.Buckets.Get bucketRequest = client.buckets().get(Constants.BUCKETNAME);
        bucketRequest.setProjection("full");
        Bucket bucket = bucketRequest.execute();
        assertThat(Constants.BUCKETNAME, is(bucket.getName()));
    }


}
