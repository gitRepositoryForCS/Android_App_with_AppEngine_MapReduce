package com.example.yingchen.myapplication.backend;

import com.google.appengine.tools.mapreduce.Mapper;

/**
 * Mapper class of mapreduce job that is to get the rank of music hotness.
 *
 * @value SEPERATOR the seperator used to split input stream.
 * @author Ying Chen
 */
class GetHotnessRankMapper extends Mapper<byte[],String, String> {

    private static final long serialVersionUID = 409204195454478863L;
    private static final String SEPERATOR = ",";

    @Override
    public void map(byte[] value) {

        if(value == null || value.length == 0) return;

        String s = new String(value);;
        if(s == null || s.trim().length() ==0) return;

        String[] arr = s.split(SEPERATOR);
        if(arr.length < 2)  return;
        if(arr[0].trim().length() == 0 || arr[1].trim().length() == 0) return;

        emit(arr[0].trim(), arr[1].trim());
    }

    /*
       try{
            d = Double.parseDouble(arr[0].trim());
            l = new Long((long) d);
        }catch (Exception e){
            emit("4", "e");
            return;
        }
    */
}