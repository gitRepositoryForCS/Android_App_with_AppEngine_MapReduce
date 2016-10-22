package com.example.yingchen.myapplication.backend;

import com.google.appengine.tools.mapreduce.Reducer;
import com.google.appengine.tools.mapreduce.ReducerInput;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * Reducer class of mapreduce job that is to get the rank of hotness of music.
 *
 * @value OUTPUT_SEPERATOR  the delimiter used to seperator key and values in output files.
 * @author Ying Chen
 */

class GetHotnessRankReducer extends Reducer<String, String, ByteBuffer> {

    private static final long serialVersionUID = 1316637485625852869L;
    private static final String OUTPUT_SEPERATOR = ", ";

    @Override
    public void reduce(String key, ReducerInput<String> values) {

        StringBuilder sb = new StringBuilder();
        while (values.hasNext()) {
            try {
                sb.append(values.next() + OUTPUT_SEPERATOR);
            } catch(Exception e){
            }
        }
        sb.setLength(sb.length()-2);
        String result = key + OUTPUT_SEPERATOR + sb.toString()+"\r\n";

        char[] arr = result.toCharArray();
        ByteBuffer buf = ByteBuffer.allocate(arr.length*2);
        CharBuffer cbuf = buf.asCharBuffer();
        cbuf.put(result);
        emit(buf);
        buf.clear();
        cbuf.clear();

    }
}
