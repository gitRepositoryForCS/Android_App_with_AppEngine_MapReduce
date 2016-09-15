
import com.google.appengine.tools.mapreduce.Mapper;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/** All Rights Reserved.
 * Creates MyMapper.java.
 * @author Ying Chen
 */
class MyMapper extends Mapper<byte[],String, String> {

  private static final long serialVersionUID = 409204195454478863L;
  private static final String SEARCH_HOTNESS = "hotness";
  private static final String SEARCH_NAME = "name";
  private static final Pattern pattern_Hotness = Pattern.compile("\\b" + SEARCH_HOTNESS + "\\b");
  private static final Pattern pattern_name = Pattern.compile("\\b" + SEARCH_NAME + "\\b");
  private static Matcher matcher_Hotness ;
  private static Matcher matcher_Name ;

  @Override
  public void map(byte[] value) {
/*
    String val = new String(value);
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
      name = val.substring(nameStart+1, val.indexOf(",", nameStart)); // plus 1 for '/t'.

      emit(name,Double.toString(hotnessRate));
    }
*/
    emit("testtest",Double.toString(1.5));
  }
}
