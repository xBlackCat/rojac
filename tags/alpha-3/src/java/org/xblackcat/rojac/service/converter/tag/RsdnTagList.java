package org.xblackcat.rojac.service.converter.tag;

import org.xblackcat.rojac.data.Smile;
import org.xblackcat.rojac.service.converter.ITag;

/**
 * @author xBlackCat
 */

public enum RsdnTagList {
    Bold(new SimpleTag("b")),
    Italic(new SimpleTag("i")),
    List(new SimpleTag("[list]", "[/list]", "<ul style='margin-top:0; margin-bottom:0;'>", "</ul>")),
    ListNumeric(new SimpleTag("[list=1]", "[/list]", "<ol type='1' start='1' style='margin-top:0; margin-bottom:0;'>", "</ol>")),
    ListAlphabetic(new SimpleTag("[list=a]", "[/list]", "<ol type='a' style='margin-top:0; margin-bottom:0;'>", "</ol>")),
    ListItem(new SingleTag("[*]", "<li/>")),
    Url(new ParameterizedTag("[url=", "]", "[/url]", "<a href='", "'>", "</a>")),
    Image(new SimpleTag("img", "<img border='0' src='", "'/>")),
    Quote(new SimpleTag("q", "<blockquote class='q'><p>", "</p></blockquote>")),
    HorizontalLine(new SingleTag("[hr]", "<hr/>")),
    Table(new SimpleTag("t", "<table class='formatter' border='0' cellspacing='2' cellpadding='5'>", "</table>")),
    TableRow(new SimpleTag("tr", "<tr class='formatter'>", "</tr>")),
    TableCell(new SimpleTag("td", "<td class='formatter'>", "</td>")),
    Email(new EmailTag()),
    Msdn(new MsdnTag()),
    NewLine(new SingleTag("\n", "<br>")),
    Moderator(new SimpleTag("moderator", "<div class='mod'>", "</div>")),
    TagLine(new SimpleTag("tagline", "<div class='tagline'>", "</div>")),
    // Smile tags
    SmileSmile(new SmileTag(Smile.Smile)),
    SmileSad(new SmileTag(Smile.Sad)),
    SmileWink(new SmileTag(Smile.Wink)),
    SmileBigGrin(new SmileTag(Smile.BigGrin)),
    SmileLol(new SmileTag(Smile.Lol)),
    SmileSmirk(new SmileTag(Smile.Smirk)),
    SmileConfused(new SmileTag(Smile.Confused)),
    SmileNo(new SmileTag(Smile.No)),
    SmileUp(new SmileTag(Smile.Up)),
    SmileDown(new SmileTag(Smile.Down)),
    SmileSuper(new SmileTag(Smile.Super)),
    SmileShuffle(new SmileTag(Smile.Shuffle)),
    SmileWow(new SmileTag(Smile.Wow)),
    SmileCrash(new SmileTag(Smile.Crash)),
    SmileUser(new SmileTag(Smile.User)),
    SmileManiac(new SmileTag(Smile.Maniac)),
    SmileDoNotKnow(new SmileTag(Smile.DoNotKnow)),
    SmileBeer(new SmileTag(Smile.Beer)),
    // Language tags
    CSharp(new LanguageTag("c#", "//", "/*", "*/", "'\"", '\\')),
    /**
     * The keywords was taken from <a href="http://download.microsoft.com/download/D/C/1/DC1B219F-3B11-4A05-9DA3-2D0F98B20917/Partition%20III%20CIL.doc">FrameworkSDK\Tool
     * Developers Guide\docs\Partition III CIL.doc</a
     */
    MSIL(new LanguageTag("msil", "//", "/*", "*/", "'\"", '\\')),
    /**
     * The keywords was taken from <a href="http://msdn2.microsoft.com/en-us/library/aa367088.aspx">MSDN article</a
     */
    MIDL(new LanguageTag("midl", "//", "/*", "*/", "'\"", '\\')),
    Asm(new LanguageTag("asm", ";", null, null, "'\"", '\\')),
    Cpp(new LanguageTag("ccode", "//", "/*", "*/", "'\"", '\\')),
    Code(new LanguageTag("code", "//", "/*", "*/", "'\"", '\\')),
    Pascal(new LanguageTag("pascal", "//", "/*", "*/", "'", '\\')),
    /**
     * The keywords was taken from <a href="http://msdn2.microsoft.com/en-us/library/ksh7h19t(VS.71).aspx">MSDN
     * article</a
     */
    VisualBasic(new LanguageTag("vb", "'", "/*", "*/", "'\"", '\\')),
    /**
     * Remark: used <a href="http://www.contrib.andrew.cmu.edu/~shadow/sql/sql1992.txt">SQL'92</a> standart.
     */
    SQL(new LanguageTag("sql", "--", null, null, "'\"", '\\')),
    /**
     * List of reserved words was taken from <a href="http://java.sun.com/docs/books/tutorial/java/nutsandbolts/_keywords.html">Sun
     * tuturial</a>
     */
    Java(new LanguageTag("java", "//", "/*", "*/", "'\"", '\\')),
    /**
     * List of reserved words was taken from the <a href="http://www.perl.org.ru:8000/documentation/docs/perlspec/perlspec.htm">Perl.org.ru
     * site</a>
     */
    Perl(new LanguageTag("perl", "#", null, null, "'\"", '\\')),
    /**
     * List of reserved words was taken from <a href="http://php.mirror.camelnetwork.com/manual/ru/reserved.php">PHP
     * site</a>
     */
    PHP(new LanguageTag("php", "//", "/*", "*/", "'\"", '\\')),
    Original(new SimpleTag("span", "<span class='lineQuote'>", "</span>"));

    private final ITag tag;

    RsdnTagList(ITag tag) {
        this.tag = tag;
    }

    public ITag getTag() {
        return tag;
    }
}
