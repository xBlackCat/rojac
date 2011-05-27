package org.xblackcat.bbcode;

import junit.framework.TestCase;

import java.io.IOException;
import java.io.StringReader;

/**
 * @author xBlackCat
 */
public class BBDomParserTest extends TestCase {
    public void testSplitByParts() throws IOException {
        BBDomParser p = new BBDomParser();

        assertEquals(p.splitByParts(new StringReader("Test")).toString(), "[Test]");
        assertEquals(p.splitByParts(new StringReader("Test [b]code[/b]")).toString(), "[Test , [b], code, [/b]]");
        assertEquals(p.splitByParts(new StringReader("[img]Test[/img]")).toString(), "[[img], Test, [/img]]");
        assertEquals(p.splitByParts(new StringReader("[url=link]Test[/url]")).toString(), "[[url=link], Test, [/url]]");
        assertEquals(p.splitByParts(new StringReader("[url=\"link\"]Test[/url]")).toString(), "[[url=\"link\"], Test, [/url]]");
        assertEquals(p.splitByParts(new StringReader("[url=\"link[]\"]Test[/url]")).toString(), "[[url=\"link[]\"], Test, [/url]]");
        assertEquals(p.splitByParts(new StringReader("[b]Test[/b][i]test[/i]")).toString(), "[[b], Test, [/b], [i], test, [/i]]");
        assertEquals(p.splitByParts(new StringReader("[b][i]Test[/i][/b]")).toString(), "[[b], [i], Test, [/i], [/b]]");
        assertEquals(p.splitByParts(new StringReader("Test[img]test[/img]test")).toString(), "[Test, [img], test, [/img], test]");

        assertEquals(p.splitByParts(new StringReader("[url=link[]]Test[/url]")).toString(), "[[url=link[], ]Test, [/url]]");
    }
    
    public void testSplitByPartsI() throws IOException {
        BBDomParser p = new BBDomParser();

        assertEquals(p.splitByPartsI(new StringReader("Test")).toString(), "[Test]");
        assertEquals(p.splitByPartsI(new StringReader("Test [b]code[/b]")).toString(), "[Test , [b], code, [/b]]");
        assertEquals(p.splitByPartsI(new StringReader("[img]Test[/img]")).toString(), "[[img], Test, [/img]]");
        assertEquals(p.splitByPartsI(new StringReader("[url=link]Test[/url]")).toString(), "[[url=link], Test, [/url]]");
        assertEquals(p.splitByPartsI(new StringReader("[url=\"link\"]Test[/url]")).toString(), "[[url=\"link\"], Test, [/url]]");
        assertEquals(p.splitByPartsI(new StringReader("[url=\"link[]\"]Test[/url]")).toString(), "[[url=\"link[]\"], Test, [/url]]");
        assertEquals(p.splitByPartsI(new StringReader("[b]Test[/b][i]test[/i]")).toString(), "[[b], Test, [/b], [i], test, [/i]]");
        assertEquals(p.splitByPartsI(new StringReader("[b][i]Test[/i][/b]")).toString(), "[[b], [i], Test, [/i], [/b]]");
        assertEquals(p.splitByPartsI(new StringReader("Test[img]test[/img]test")).toString(), "[Test, [img], test, [/img], test]");

        assertEquals(p.splitByPartsI(new StringReader("[url=link[]]Test[/url]")).toString(), "[[url=link[], ]Test, [/url]]");
    }
}
