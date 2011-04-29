package org.xblackcat.rojac.service.converter.tag;
/**
 *
 * @author xBlackCat
 */


import junit.framework.TestCase;
import org.xblackcat.rojac.service.converter.ITag;

public class RsdnTagConverterTest extends TestCase {
    public void testSimpleTagConverter() {
        ITag boldTag = RsdnTagList.Bold.getTag();
        ITag italicTag = RsdnTagList.Italic.getTag();

        {
            String rsdnMessage = "This is a [b]test[/b] for [b]simple[/b] bold tag.";
            String htmlMessage = "This is a <b>test</b> for <b>simple</b> bold tag.";

            assertEquals(htmlMessage, TestUtils.applyTags(rsdnMessage, boldTag));
        }

        {
            String rsdnMessage = "This is a [b]test[/B] for [i]simple[/i] bold tag.";
            String htmlMessage = "This is a <b>test</b> for [i]simple[/i] bold tag.";

            assertEquals(htmlMessage, TestUtils.applyTags(rsdnMessage, boldTag));
        }

        {
            String rsdnMessage = "This is a test for [b]bold[/b], [i]italic[/I] and [B][I]bolded italic[/i][/b] tags.";
            String htmlMessage = "This is a test for <b>bold</b>, <i>italic</i> and <b><i>bolded italic</i></b> tags.";

            // Test commutative property
            assertEquals(htmlMessage, TestUtils.applyTags(TestUtils.applyTags(rsdnMessage, italicTag), boldTag));
            assertEquals(htmlMessage, TestUtils.applyTags(TestUtils.applyTags(rsdnMessage, boldTag), italicTag));
        }
    }

    public void testListsSimpleTagConverter() {
        ITag listTag = RsdnTagList.List.getTag();
        ITag numListTag = RsdnTagList.ListNumeric.getTag();
        ITag alphListTag = RsdnTagList.ListAlphabetic.getTag();
        ITag listItemTag = RsdnTagList.ListItem.getTag();
        {
            String rsdnMessage = "This is a test for [list][*]item[*]item[/list]tags.";
            String htmlMessage = "This is a test for <ul style='margin-top:0; margin-bottom:0;'><li/>item<li/>item</ul>tags.";

            assertEquals(htmlMessage, TestUtils.applyTags(TestUtils.applyTags(rsdnMessage, listTag), listItemTag));
        }

        {
            String rsdnMessage = "This is a test for [list=1][*]item[*]item[/list]tags.";
            String htmlMessage = "This is a test for <ol type='1' start='1' style='margin-top:0; margin-bottom:0;'><li/>item<li/>item</ol>tags.";

            assertEquals(htmlMessage, TestUtils.applyTags(rsdnMessage, numListTag, listItemTag));
        }

        {
            String rsdnMessage = "This is a test for [list=a][*]item[*]item[/list]tags.";
            String htmlMessage = "This is a test for <ol type='a' style='margin-top:0; margin-bottom:0;'><li/>item<li/>item</ol>tags.";

            assertEquals(htmlMessage, TestUtils.applyTags(rsdnMessage, alphListTag, listItemTag));
        }
    }

    public void testUrlTagConverter() {
        ITag urlTag = RsdnTagList.Url.getTag();

        {
            String rsdnMessage = "This is a [URL=http://some.place/url]link[/url] tag.";
            String htmlMessage = "This is a <a href='http://some.place/url'>link</a> tag.";

            assertEquals(htmlMessage, TestUtils.applyTags(rsdnMessage, urlTag));
        }
    }

    public void testImageTagConverter() {
        ITag urlTag = RsdnTagList.Image.getTag();

        {
            String rsdnMessage = "This is an image [Img]http://i.ru-board.com/avatars/BlackCat.gif[/IMG] tag.";
            String htmlMessage = "This is an image <img border='0' src='http://i.ru-board.com/avatars/BlackCat.gif'/> tag.";

            assertEquals(htmlMessage, TestUtils.applyTags(rsdnMessage, urlTag));
        }
    }

    public void testQuoteTagConverter() {
        ITag urlTag = RsdnTagList.Quote.getTag();

        {
            String rsdnMessage = "This is a [q]quote[/q] tag.";
            String htmlMessage = "This is a <blockquote class='q'><p>quote</p></blockquote> tag.";

            assertEquals(htmlMessage, TestUtils.applyTags(rsdnMessage, urlTag));
        }
    }

    public void testLineTagConverter() {
        ITag hrTag = RsdnTagList.HorizontalLine.getTag();

        {
            String rsdnMessage = "This is a [hr] horizontal line tag.";
            String htmlMessage = "This is a <hr/> horizontal line tag.";

            assertEquals(htmlMessage, TestUtils.applyTags(rsdnMessage, hrTag));
        }
    }

    public void testEmailTagConverter() {
        ITag emailTag = RsdnTagList.Email.getTag();

        {
            String rsdnMessage = "This is an email [email]support@rsdn.ru[/email] tag.";
            String htmlMessage = "This is an email <a href='mailto:support@rsdn.ru'>support@rsdn.ru</a> tag.";

            assertEquals(htmlMessage, TestUtils.applyTags(rsdnMessage, emailTag));
        }

        {
            String rsdnMessage = "This is emails [email]first@rsdn.ru[/email] [email]second@rsdn.ru[/email] tags.";
            String htmlMessage = "This is emails <a href='mailto:first@rsdn.ru'>first@rsdn.ru</a> <a href='mailto:second@rsdn.ru'>second@rsdn.ru</a> tags.";

            assertEquals(htmlMessage, TestUtils.applyTags(rsdnMessage, emailTag));
        }
    }

    public void testTableConverter() {
        ITag tableTag = RsdnTagList.Table.getTag();
        ITag trTag = RsdnTagList.TableRow.getTag();
        ITag tdTag = RsdnTagList.TableCell.getTag();

        {
            String rsdnMessage = "This is a table: [T][tr][td]1[/td][td]2[/td][/tr][/T]";
            String htmlMessage = "This is a table: <table class='formatter' border='0' cellspacing='2' cellpadding='5'><tr class='formatter'><td class='formatter'>1</td><td class='formatter'>2</td></tr></table>";

            assertEquals(htmlMessage, TestUtils.applyTags(rsdnMessage, tdTag, trTag, tableTag));
        }
    }

    public void testMsdnConverter() {
        ITag msdnConverter = RsdnTagList.Msdn.getTag();

        {
            String rsdnMessage = "This is a msdn search link [msdn]Поиск в MSDN[/MSDN]";
            String htmlMessage = "This is a msdn search link <a target='_blank' class='m' href='http://search.microsoft.com/search/results.aspx?View=msdn&amp;c=4&amp;qu=%D0%9F%D0%BE%D0%B8%D1%81%D0%BA+%D0%B2+MSDN'>Поиск в MSDN</a>";

            assertEquals(htmlMessage, TestUtils.applyTags(rsdnMessage, msdnConverter));
        }

        {
            String rsdnMessage = "This is a msdn double search link [msdn]Поиск в MSDN[/MSDN] [msdn]search[/msdn]";
            String htmlMessage = "This is a msdn double search link <a target='_blank' class='m' href='http://search.microsoft.com/search/results.aspx?View=msdn&amp;c=4&amp;qu=%D0%9F%D0%BE%D0%B8%D1%81%D0%BA+%D0%B2+MSDN'>Поиск в MSDN</a> <a target='_blank' class='m' href='http://search.microsoft.com/search/results.aspx?View=msdn&amp;c=4&amp;qu=search'>search</a>";

            assertEquals(htmlMessage, TestUtils.applyTags(rsdnMessage, msdnConverter));
        }
    }
}
