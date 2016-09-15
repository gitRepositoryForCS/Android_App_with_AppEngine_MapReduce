
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.mapreduce.GoogleCloudStorageFileSet;
import com.google.appengine.tools.mapreduce.MapReduceJob;
import com.google.appengine.tools.mapreduce.MapReduceSettings;
import com.google.appengine.tools.mapreduce.MapReduceSpecification;
import com.google.appengine.tools.mapreduce.Marshallers;
import java.nio.ByteBuffer;
import java.util.logging.Logger;
import com.google.appengine.tools.mapreduce.outputs.MarshallingOutput;
import com.google.appengine.tools.mapreduce.Output;
import com.google.appengine.tools.mapreduce.inputs.GoogleCloudStorageLineInput;
import java.util.ArrayList;
import com.google.appengine.tools.mapreduce.Marshaller;

/**
 * Created by yingchen on 6/5/16.
 */
public class MyMapReduceJob {

  private static final long serialVersionUID = 6725038763886885189L;
  private static final Logger log = Logger.getLogger(MyMapReduceJob.class.getName());
  private static final String bucketName = "mybucket.appspot.com";
  private static final String fileName = "testfile.txt";
    private static final String outputFilename = "output.txt";
  private static final byte separator = (byte) '\n';
  private static final int shardCount = 2;

  public String run(){

      String id = MapReduceJob.start(getJobSpec(shardCount), getMapReduceSettings());
      return id;
  }

    /* getJobSpec */
  private MapReduceSpecification<byte[], String, String, ByteBuffer,GoogleCloudStorageFileSet>
         getJobSpec(int reduceShardCount) {

            GcsFilename gcsFile = new GcsFilename(bucketName, fileName);

          /*
            List<String> list = new ArrayList<>();
            list.add(fileName);
            GoogleCloudStorageFileSet fileSet = new GoogleCloudStorageFileSet(bucketName,list);
          */

            GoogleCloudStorageLineInput input = new GoogleCloudStorageLineInput(gcsFile,separator,shardCount);
            MyMapper mapper = new MyMapper();
            MyReducer reducer = new MyReducer();
            GoogleCloudStorageFileOutput output
            = new GoogleCloudStorageFileOutput(bucketName,String.format("%s.txt",outputFilename),"text/plain");
     // Marshaller<ByteBuffer> outputMarshaller = Marshallers.getSerializationMarshaller();

  //    Output<ByteBuffer,GoogleCloudStorageFileSet > output =  new MarshallingOutput<>(
    //          new GoogleCloudStorageFileOutput(bucketName,String.format("%s.txt",outputFilename),"text/plain"),outputMarshaller);
            MapReduceSpecification<byte[], String, String, ByteBuffer,GoogleCloudStorageFileSet>
                    builder = new MapReduceSpecification.Builder<>(input,mapper,reducer,output)
                .setKeyMarshaller(Marshallers.getStringMarshaller())
                .setValueMarshaller(Marshallers.getStringMarshaller())
                .setJobName("MyMapReduceTest.")
                .setNumReducers(reduceShardCount).build();

    return builder;
  }

  public static MapReduceSettings getMapReduceSettings() {
    MapReduceSettings settings = new MapReduceSettings.Builder()
            .setBucketName(bucketName)
            .setWorkerQueueName("mapreduce-workers")
           // .setModule(module) // if queue is null will use the current queue or "default" if none
            .build();
    return settings;
  }

}



  /*

  private static class LogResults extends Job1<Void, MapReduceResult<List<List<KeyValue<String, Long>>>>> {

    private static final long serialVersionUID = 131906664096202890L;

    @Override
    public Value<Void> run(MapReduceResult<List<List<KeyValue<String, Long>>>> mrResult) throws Exception {
      List<String> mostPopulars = new ArrayList<>();
      long mostPopularCount = -1;
      for (List<KeyValue<String, Long>> countList : mrResult.getOutputResult()) {
        for (KeyValue<String, Long> count : countList) {
          log.info("Character '" + count.getKey() + "' appeared " + count.getValue() + " times");
          if (count.getValue() < mostPopularCount) {
            continue;
          }
          if (count.getValue() > mostPopularCount) {
            mostPopulars.clear();
            mostPopularCount = count.getValue();
          }
          mostPopulars.add(count.getKey());
        }
      }
      if (!mostPopulars.isEmpty()) {
        log.info("Most popular characters: " + mostPopulars);
      }
      return null;
    }
  }

  */