package org.xblackcat.sunaj.service.converter.tag;

import junit.framework.TestCase;

/**
 * Date: 20 лют 2008
 *
 * @author xBlackCat
 */

public class ParameterizedTagTest extends TestCase {
    public void testParameterizedTag() throws Exception {
        ParameterizedTag urlTag = new ParameterizedTag(
                "[url=",
                "]",
                "[/url]",
                "<a href='",
                "'>",
                "</a>");

        {
            String orig = "Test [url=http://google.com/]link[/url] to google";
            String expected = "Test <a href='http://google.com/'>link</a> to google";

            assertEquals(expected, TestUtils.applyTag(orig, urlTag));
        }

        // Invalid variants
        {
            String orig = "Test [url=http://google.com/]link to google";
            String expected = "Test [url=http://google.com/]link to google";

            assertEquals(expected, TestUtils.applyTag(orig, urlTag));
        }

        {
            String orig = "Test [url=]link[/url] to google";
            String expected = "Test <a href='link'>link</a> to google";

            assertEquals(expected, TestUtils.applyTag(orig, urlTag));
        }
    }
}