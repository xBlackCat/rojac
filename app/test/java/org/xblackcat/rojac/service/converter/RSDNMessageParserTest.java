package org.xblackcat.rojac.service.converter;


import junit.framework.TestCase;
import org.xblackcat.rojac.service.converter.tag.TestUtils;

/**
 * Date: 20 лют 2008
 *
 * @author xBlackCat
 */

public class RSDNMessageParserTest extends TestCase {
    private IMessageParser parser;

    protected void setUp() throws Exception {
        super.setUp();

        parser = new RSDNMessageParserFactory(
                "/message/tags/taglist.properties",
                "/message/tags/tagrules.properties",
                "/message/tags/taggroup.properties"
        ).getParser();
    }

    public void testSingleTag() throws Exception {

        {
            String orig = ":smile:";
            String expected = ":)";

            assertEquals(TestUtils.makeExpected(expected), parser.convert(orig));
        }

        {
            String orig = "[b]bold text[/b]";
            String expected = "<b>bold text</b>";

            assertEquals(TestUtils.makeExpected(expected), parser.convert(orig));
        }

        {
            String orig = "[code]code[/code]";
            String expected = "<p>code</p>";

            assertEquals(TestUtils.makeExpected(expected), parser.convert(orig));
        }

        {
            String orig = "Some text before :smile:";
            String expected = "Some text before :)";

            assertEquals(TestUtils.makeExpected(expected), parser.convert(orig));
        }

        {
            String orig = "Some text before [b]bold text[/b]";
            String expected = "Some text before <b>bold text</b>";

            assertEquals(TestUtils.makeExpected(expected), parser.convert(orig));
        }

        {
            String orig = "Some text before [code]code[/code]";
            String expected = "Some text before <p>code</p>";

            assertEquals(TestUtils.makeExpected(expected), parser.convert(orig));
        }

        {
            String orig = ":smile: some text after";
            String expected = ":) some text after";

            assertEquals(TestUtils.makeExpected(expected), parser.convert(orig));
        }

        {
            String orig = "[b]bold text[/b] some text after";
            String expected = "<b>bold text</b> some text after";

            assertEquals(TestUtils.makeExpected(expected), parser.convert(orig));
        }

        {
            String orig = "[code]code[/code] some text after";
            String expected = "<p>code</p> some text after";

            assertEquals(TestUtils.makeExpected(expected), parser.convert(orig));
        }

        {
            String orig = "Before :smile: after";
            String expected = "Before :) after";

            assertEquals(TestUtils.makeExpected(expected), parser.convert(orig));
        }

        {
            String orig = "Before [b]bold text[/b] after";
            String expected = "Before <b>bold text</b> after";

            assertEquals(TestUtils.makeExpected(expected), parser.convert(orig));
        }

        {
            String orig = "Before [code]code[/code] after";
            String expected = "Before <p>code</p> after";

            assertEquals(TestUtils.makeExpected(expected), parser.convert(orig));
        }
    }

    public void testSuperpositions() throws Exception {
        {
            String orig = "Test [b]bold smile :smile: [/b].";
            String expected = "Test <b>bold smile :) </b>.";

            assertEquals(TestUtils.makeExpected(expected), parser.convert(orig));
        }

        {
            String orig = "Test [b]bold smile :smile: [code]bold code[/code] text[/b].";
            String expected = "Test <b>bold smile :) <p>bold code</p> text</b>.";

            assertEquals(TestUtils.makeExpected(expected), parser.convert(orig));
        }

        {
            String orig = "[code]plain code [b]bold code[/b][/code]";
            String expected = "<p>plain code <b>bold code</b></p>";

            assertEquals(TestUtils.makeExpected(expected), parser.convert(orig));
        }

        {
            String orig = "[code]plain code [b]bold code :smile: [/b] :smile: [/code] :smile:";
            String expected = "<p>plain code <b>bold code :smile: </b> :smile: </p> :)";

            assertEquals(TestUtils.makeExpected(expected), parser.convert(orig));
        }

        {
            String orig = "Test [b]bold smile :smile: [code]bold code :smile: [/code] text[/b].";
            String expected = "Test <b>bold smile :) <p>bold code :smile: </p> text</b>.";

            assertEquals(TestUtils.makeExpected(expected), parser.convert(orig));
        }
    }

    public void testInvalidSuperpositions() throws Exception {
        {
            String orig = "Test [b]bold [code]code [/b] [/code].";
            String expected = "Test <b>bold [code]code </b> [/code].";

            assertEquals(TestUtils.makeExpected(expected), parser.convert(orig));
        }

        {
            String orig = "[code]plain code [b]bold code[/code][/b]";
            String expected = "<p>plain code [b]bold code</p>[/b]";

            assertEquals(TestUtils.makeExpected(expected), parser.convert(orig));
        }
    }
}