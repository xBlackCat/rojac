package org.xblackcat.sunaj.service.converter.tag;


import junit.framework.TestCase;
import org.xblackcat.sunaj.service.converter.ITag;

/**
 * Date: 20 лют 2008
 *
 * @author xBlackCat
 */

public class SingleTagTest extends TestCase {
    public void testSingleTag() throws Exception {
        ITag tag = new SingleTag("\n", "<br>");

        {
            String orig = "Test\n carriage return";
            String expected = "Test<br> carriage return";

            assertEquals(expected, TestUtils.applyTag(orig, tag));
        }

    }
}