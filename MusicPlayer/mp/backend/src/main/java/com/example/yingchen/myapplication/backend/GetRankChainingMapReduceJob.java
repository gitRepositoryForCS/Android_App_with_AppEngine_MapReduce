package com.example.yingchen.myapplication.backend;

import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.mapreduce.GoogleCloudStorageFileSet;
import com.google.appengine.tools.mapreduce.MapReduceSettings;
import com.google.appengine.tools.mapreduce.MapReduceSpecification;
import com.google.appengine.tools.mapreduce.Marshallers;

import java.nio.ByteBuffer;
import java.util.logging.Logger;
import com.google.appengine.tools.pipeline.Job0;
import com.google.appengine.tools.mapreduce.inputs.GoogleCloudStorageLineInput;
import com.google.appengine.tools.mapreduce.outputs.GoogleCloudStorageFileOutput;
import com.google.appengine.tools.pipeline.FutureValue;
import com.google.appengine.tools.mapreduce.MapReduceJob;
import java.util.List;
import com.google.appengine.tools.mapreduce.MapReduceResult;

/**
 * Chained mapreduce jobs that is to get the rank of hotness of music. All the constant value can
 * be modified here to satisfy specific operations.
 *
 * @value BUCKETNAME  the bucket name that the input and output files are storded.
 * @value INPUTFILE   the input file name.
 * @value OUTPUTFILE_INTERMEDIATE   the intermediate output file of the first mapreduce job (input of the second one.)
 * @value OUTPUTFILE_FINAL   the final output file name.
 * @value SEPARATOR   the seperator which is used to shard big files.
 * @value REDUCESHARDCOUNT   the number of shards.
 * @value MIMETYPE  mimetype of the input and output files.
 *
 * @author Ying Chen
 */
public class GetRankChainingMapReduceJob  extends Job0 {

    private static final long serialVersionUID = 6725038763886885189L;
    private static final Logger log = Logger.getLogger(GetRankChainingMapReduceJob.class.getName());

    private static final String BUCKETNAME = “your bucket name”;
    private static final String INPUTFILE = “input.txt";
    private static final String OUTPUTFILE_INTERMEDIATE  = "output_getSum";
    private static final String OUTPUTFILE_FINAL = "output_getRank";
    private static final byte SEPARATOR = (byte) '\n';
    private static final int REDUCESHARDCOUNT = 1;
    private static final String MIMETYPE = "text/plain";


    public GetRankChainingMapReduceJob(){ }


    /*get mapreduce settings.*/
    public static MapReduceSettings getMapReduceSettings() {
        MapReduceSettings settings = new MapReduceSettings.Builder()
                .setBucketName(BUCKETNAME)
                .setWorkerQueueName("mapreduce-workers")
                .build();
        return settings;
    }

    @Override
    public FutureValue<MapReduceResult<List<List<GoogleCloudStorageFileSet>>>> run() throws Exception {

        FutureValue<MapReduceResult<List<List<GoogleCloudStorageFileSet>>>> getSum = futureCall(
                new MapReduceJob<>(getSumJobSpec(BUCKETNAME, INPUTFILE,OUTPUTFILE_INTERMEDIATE, MIMETYPE, REDUCESHARDCOUNT),
                        new MapReduceSettings.Builder(getMapReduceSettings()).setBucketName(BUCKETNAME).build()));

        FutureValue<MapReduceResult<List<List<GoogleCloudStorageFileSet>>>> getRank = futureCall(
                new MapReduceJob<>(getRankJobSpec(BUCKETNAME, OUTPUTFILE_INTERMEDIATE+".txt",OUTPUTFILE_FINAL, MIMETYPE, REDUCESHARDCOUNT),
                        new MapReduceSettings.Builder(getMapReduceSettings()).setBucketName(BUCKETNAME).build()) , waitFor(getSum));

        return getRank;
    }

    /*
     * getJobSpec for the first job.
     * */
    private MapReduceSpecification<byte[], String, String, ByteBuffer,GoogleCloudStorageFileSet>
       getSumJobSpec(String bucketName, String inputFileName,String outputFileName, String mimeType, int reduceShardCount) {

        GcsFilename gcsFile = new GcsFilename(bucketName, inputFileName);

        GoogleCloudStorageLineInput input = new GoogleCloudStorageLineInput(gcsFile, SEPARATOR, reduceShardCount);
        GetHotnessSumMapper mapper = new GetHotnessSumMapper();
        GetHotnessSumReducer reducer = new GetHotnessSumReducer();
        GoogleCloudStorageFileOutput output
                = new GoogleCloudStorageFileOutput(bucketName, String.format("%s.txt", outputFileName), mimeType);

        MapReduceSpecification<byte[], String, String, ByteBuffer, GoogleCloudStorageFileSet>
                builder = new MapReduceSpecification.Builder<>(input, mapper, reducer, output)
                .setKeyMarshaller(Marshallers.getStringMarshaller())
                .setValueMarshaller(Marshallers.getStringMarshaller())
                .setJobName("MyMapReduceTest Get Sume.")
                .setNumReducers(reduceShardCount).build();

        return builder;
    }
    /*
     * get job spect for the second job.
     * */
    private MapReduceSpecification<byte[], String, String, ByteBuffer,GoogleCloudStorageFileSet>
         getRankJobSpec(String bucketName, String inputFileName,String outputFileName, String mimeType, int reduceShardCount) {

        GcsFilename gcsFile = new GcsFilename(bucketName, inputFileName);

        GoogleCloudStorageLineInput input = new GoogleCloudStorageLineInput(gcsFile, SEPARATOR, reduceShardCount);
        GetHotnessRankMapper mapper = new GetHotnessRankMapper();
        GetHotnessRankReducer reducer = new GetHotnessRankReducer();
        GoogleCloudStorageFileOutput output
                = new GoogleCloudStorageFileOutput(bucketName, String.format("%s.txt", outputFileName), mimeType);

            MapReduceSpecification<byte[], String, String, ByteBuffer, GoogleCloudStorageFileSet>
                builder = new MapReduceSpecification.Builder<>(input, mapper, reducer, output)
                .setKeyMarshaller(Marshallers.getStringMarshaller())
                .setValueMarshaller(Marshallers.getStringMarshaller())
                .setJobName("MyMapReduceTest Get Rank.")
                .setNumReducers(reduceShardCount).build();

        return builder;
    }

    /*
    public String run() {
         mrJobID = MapReduceJob.start(getJobSpec(REDUCESHARDCOUNT), getMapReduceSettings());
        return mrJobID;
    }
    */
}
