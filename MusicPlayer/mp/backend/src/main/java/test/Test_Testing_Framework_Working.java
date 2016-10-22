package test;

/**
 * This test is testing the test framework is working.
 *
 * @Author YingChen
 */

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Test_Testing_Framework_Working {

    @org.testng.annotations.Test
    public void test_Testing_Framework() {
        assertThat("", is(""));
    }

}