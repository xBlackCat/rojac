package org.xblackcat.sunaj.util;
/**
 * Date: 23 груд 2007
 * @author xBlackCat
 */


import junit.framework.TestCase;

public class ResourceUtilsTest extends TestCase {
    public void testConstantToProperty() throws Exception {
        assertEquals("example", ResourceUtils.constantToProperty("EXAMPLE"));
        assertEquals("example.test", ResourceUtils.constantToProperty("EXAMPLE_TEST"));
        assertEquals("example.long.test", ResourceUtils.constantToProperty("EXAMPLE_LONG_TEST"));

        assertEquals("example", ResourceUtils.constantToProperty("Example"));
        assertEquals("example.test", ResourceUtils.constantToProperty("Example_test"));
        assertEquals("example.long.test", ResourceUtils.constantToProperty("EXAMPLE_LONG_test"));

        assertNull(ResourceUtils.constantToProperty(null));
    }
}