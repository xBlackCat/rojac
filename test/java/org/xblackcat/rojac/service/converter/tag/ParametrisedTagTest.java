package org.xblackcat.rojac.service.converter.tag;

import junit.framework.TestCase;

/**
 * @author xBlackCat
 */

public class ParametrisedTagTest extends TestCase {
    public void testParameterizedTag() throws Exception {
        ParametrisedTag urlTag = new ParametrisedTag(
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
