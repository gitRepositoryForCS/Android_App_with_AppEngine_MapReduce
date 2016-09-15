package com.example.yingchen.myapplication.backend.test;

/**
 * Created by yingchen on 6/22/16.
 */
public class TestMapper {

    /*
    * public class TestMatcher {

	  public static final String SEARCH_STRING = "hotness";

	public static void main(String[] args) {

		Pattern pattern = Pattern.compile("\\b" + SEARCH_STRING + "\\b");

		FileInputStream fstream;
		 Matcher matcher ;
		try {
			fstream = new FileInputStream("/Users/yingchen/Documents/aaa_report/testfile.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;

			while ((strLine = br.readLine()) != null)   {

				matcher = pattern.matcher(strLine.toLowerCase());
			    while (matcher.find()) {
			        System.out.println("    group: " + matcher.group());
			        int startIndex = matcher.start() +SEARCH_STRING.length();
			        System.out.println(strLine.substring(startIndex, strLine.indexOf(",", startIndex)));
			    }

			}
			br.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe){

		}


	}

}

    * */
}
