package test;


import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * This class unit tests the get_hotness_rank mapper and reducer.
 *
 * @author Ying Chen
 */
public class Test_GetRank_Mapper_Reducer {



    @org.testng.annotations.Test
    public void testGetRankMapper() {
        String s = "3.0,n1";
        if(s == null || s.trim().length() ==0) Assert.fail();

        String[] arr = s.split(String.valueOf(Constants.SEPERATOR));
        if(arr.length < 2)  return;
        if(arr[0].trim().length() == 0 || arr[1].trim().length() == 0) Assert.fail();

        //emit(arr[0].trim(), arr[1].trim());
        assertThat(arr[0].trim(), is("3.0"));
        assertThat(arr[1].trim(), is("n1"));
    }

    @org.testng.annotations.Test
    public static void testGetRankReducer(){

        StringBuilder sb = new StringBuilder();
        String key = "3.0";
        String[] names = {"n1","n2"};

        List<String> vals = new ArrayList<String>();
        for(String n : names) vals.add(n);
        Iterator<String> values = vals.iterator();

        while (values.hasNext()) {
            try {
                sb.append(values.next() + Constants.OUTPUT_SEPERATOR);
            } catch(Exception e){
                Assert.fail();
            }
        }
        sb.setLength(sb.length()-2);
        String result = key + Constants.OUTPUT_SEPERATOR + sb.toString()+"\r\n";
        assertThat(result, is("3.0, n1, n2\r\n"));
    }

}
