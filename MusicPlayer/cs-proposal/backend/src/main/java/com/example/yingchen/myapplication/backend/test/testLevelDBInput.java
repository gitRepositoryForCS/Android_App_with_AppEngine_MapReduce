import com.google.appengine.tools.mapreduce.GoogleCloudStorageFileSet;
import com.google.appengine.tools.mapreduce.inputs.GoogleCloudStorageLevelDbInput;
import com.google.appengine.tools.mapreduce.InputReader;
import com.google.appengine.tools.mapreduce.inputs.GoogleCloudStorageLevelDbInputReader;
import java.nio.channels.ReadableByteChannel;
import com.google.appengine.tools.mapreduce.inputs.GoogleCloudStorageLineInput;
import com.google.appengine.tools.cloudstorage.GcsFilename;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yingchen on 6/24/16.
 */
public class testLevelDBInput {
    private static final String bucketName = "mybucket.appspot.com";
    private static final String fileName = "testfile.txt";

    public static void main(String[] args){
        List<String> list = new ArrayList<>();
        list.add(fileName);
        //GoogleCloudStorageFileSet fileSet = new GoogleCloudStorageFileSet(bucketName,list);
        //GoogleCloudStorageLevelDbInput input = new GoogleCloudStorageLevelDbInput(fileSet);
        String file = "testfile.txt";
        GcsFilename fileName = new GcsFilename(bucketName, file);

        GoogleCloudStorageLineInput input = new GoogleCloudStorageLineInput(fileName,(byte)',',1);
        List<? extends InputReader<byte[]>> readers = input.createReaders();
      try{
          byte[] b = readers.get(0).next();
          System.out.println(b.toString());

      }catch(Exception e){

      }

        /*
        *     List<String> list = new ArrayList<>();

        //GoogleCloudStorageFileSet fileSet = new GoogleCloudStorageFileSet(bucketName,list);
        //GoogleCloudStorageLevelDbInput input = new GoogleCloudStorageLevelDbInput(fileSet);
        String file = "testfile.txt";
        list.add(file);
        GcsFilename fName = new GcsFilename(bucketName, file);

        GoogleCloudStorageLineInput input = new GoogleCloudStorageLineInput(fName,(byte)'\n',1);
        List<? extends InputReader<byte[]>> readers = input.createReaders();
        InputReader<byte[]> reader = readers.get(0);

        if(reader ==null){
            resp.getWriter().println("reader is null.");
        }
        else{
            resp.getWriter().println("|||| reader is NOT null !!");

                   try{
                       byte[] b = reader.next();
                      // reader.
                       // System.out.println(b.toString());
                       resp.getWriter().println(" ||||the content is : "+b.toString());

                   }catch(Exception e){
                       resp.getWriter().println("|||No next. Exception caught: "+ e.getStackTrace());

                   }

                   resp.getWriter().println("||||reader.toString():  "+reader.toString());


            }
        * */

        // List<InputReader<ByteBuffer>> readers = input.createReaders();
/*
       try{
           if(readers !=null){
               GoogleCloudStorageLevelDbInputReader inReader = (GoogleCloudStorageLevelDbInputReader) readers.get(0);
               ReadableByteChannel channel = inReader.createReadableByteChannel();

               ByteBuffer byteBuffer = ByteBuffer.allocate(512);
               while(channel.read(byteBuffer) > 0){

                   //limit is set to current position and position is set to zero
                   byteBuffer.flip();

                   while(byteBuffer.hasRemaining()){
                       char ch = (char) byteBuffer.get();
                       System.out.print(ch);
                   }
               }

           }else{

               System.out.println("readers is null.");
           }


       }catch(Exception e){
           System.out.println("Exceptions happen when using readers. ");
       }
*/
    }


}
