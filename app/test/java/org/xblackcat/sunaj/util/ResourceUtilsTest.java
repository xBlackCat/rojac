package org.xblackcat.sunaj.util;
/**
 * Date: 23 груд 2007
 * @author xBlackCat
 */


import junit.framework.TestCase;

import java.util.MissingResourceException;

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

    public void testLoadListFromResource() throws Exception {
        //
        // Test loading for existing resources
        //

        //  Load empty list
        {
            String[] strings = ResourceUtils.loadListFromResource("/loadlist/empty_list.txt");
            assertNotNull(strings);
            assertEquals(0, strings.length);
        }

        //  Load single list
        {
            String[] strings = ResourceUtils.loadListFromResource("/loadlist/single_record.txt");
            assertNotNull(strings);
            assertEquals(1, strings.length);
            assertEquals("test", strings[0]);
        }

        //  Load list with folowing values: "one", "two", "three", "four"
        {
            String[] strings = ResourceUtils.loadListFromResource("/loadlist/few_records.txt");
            assertNotNull(strings);
            assertEquals(4, strings.length);
            assertEquals("one", strings[0]);
            assertEquals("two", strings[1]);
            assertEquals("three", strings[2]);
            assertEquals("four", strings[3]);
        }

        //
        // Test invalid values
        //

        // Test for non-existing resource
        try {
            ResourceUtils.loadListFromResource("/this/path/is/invalid.txt");

            fail("MissingResourceException wasn't be thrown!");
        } catch (MissingResourceException e) {
            // Test passed
            assertTrue(true);
        }
    }
}