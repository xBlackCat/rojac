package org.xblackcat.rojac.service.converter.tag;


import junit.framework.TestCase;
import org.xblackcat.rojac.service.converter.ITag;

/**
 * @author xBlackCat
 */
public class SimpleTagTest extends TestCase {
    public void testSimpleTag() throws Exception {
        ITag tag = new SimpleTag("b", "b");

        {
            String orig = "Test [b]bold[/b] tag";
            String expected = "Test <b>bold</b> tag";

            assertEquals(expected, TestUtils.applyTag(orig, tag));
        }
    }
}