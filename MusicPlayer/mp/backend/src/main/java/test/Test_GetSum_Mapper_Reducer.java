package test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * This class unit tests the get_hotness_sum mapper and reducer.
 *
 * @author Ying Chen
 */
public class Test_GetSum_Mapper_Reducer {

    private static final String SEARCH_HOTNESS = "hotness";
    private static final String SEARCH_NAME = "name";
    private static final Pattern pattern_Hotness = Pattern.compile("\\b" + SEARCH_HOTNESS + "\\b");
    private static final Pattern pattern_name = Pattern.compile("\\b" + SEARCH_NAME + "\\b");
    private static Matcher matcher_Hotness;
    private static Matcher matcher_Name;

    @org.testng.annotations.Test
    public void testGetSumMapper() {

        com.example.yingchen.myapplication.backend.GetHotnessSumMapper gsm = new com.example.yingchen.myapplication.backend.GetHotnessSumMapper();
        String val = "id\t44, name\tn1,\thotness\t1, loudness\t0.9";
        matcher_Hotness = pattern_Hotness.matcher(val.toLowerCase());
        matcher_Name = pattern_name.matcher(val.toLowerCase());
        double hotnessRate;
        int startIndex;
        String name;
        int nameStart;
        while (matcher_Hotness.find() && matcher_Name.find()) {
            startIndex = matcher_Hotness.start() + SEARCH_HOTNESS.length();
            hotnessRate = Double.parseDouble(val.substring(startIndex, val.indexOf(",", startIndex)));

            nameStart = matcher_Name.start() + SEARCH_NAME.length();
            name = val.substring(nameStart + 1, val.indexOf(",", nameStart)); // plus 1 for '/t'.

            //emit(name, Double.toString(hotnessRate));
            assertThat(name, is("n1"));
            assertThat(Double.toString(hotnessRate), is("1.0"));
        }
    }

    @org.testng.annotations.Test
    public static void testGetSumReducer(){

        String key = "n1";
        double[] arr = {1.0, 2.0};
        List<Double> vals = new ArrayList<Double>();
        for(double d : arr) vals.add(d);
        Iterator<Double> values = vals.iterator();

        double total = 0;
        while (values.hasNext()) {
            total += values.next();
        }
        String result = String.valueOf(total) + Test_Constants.SEPERATOR + key + "\r\n";
        assertThat(result, is("3.0" + "," + "n1" + "\r\n"));
    }

}




