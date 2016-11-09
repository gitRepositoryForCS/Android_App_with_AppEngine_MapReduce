package test;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This class unit tests if Myservlet class works as expected.
 *
 * @author Ying Chen
 */

public class Test_MyServlet {

    private static final String outputResult= "id\t44, name\tn1,\thotness\t1, loudness\t0.9\n" +
            "id\t1, name\tn2,\thotness\t0.8, loudness\t0.2\n" +
            "id\t4, name\tn4,\thotness\t0.1, loudness\t0.9\n" +
            "id\t11, name\tn5,\thotness\t8, loudness\t0.2\n" +
            "id\t2, name\tn23,\thotness\t0.3, loudness\t0.5\n" +
            "id\t22, name\tn72,\thotness\t0, loudness\t0.5\n" +
            "id\t33, name\tn6,\thotness\t6, loudness\t0.1\n" +
            "id\t3, name\tn3,\thotness\t0.6, loudness\t0.1\n" +
            "\n" +
            "\n";

    /**
     * Test that doGet method in MyServlet works as expected.
     **/
    @Test
    public void test_doGet() throws Exception {

        WebConversation conversation = new WebConversation();
        WebRequest request = new GetMethodWebRequest(Test_Constants.LOCAL_URL);
        WebResponse response = conversation.getResponse( request );

        assertEquals(outputResult,  response.getText() );
    }
}
