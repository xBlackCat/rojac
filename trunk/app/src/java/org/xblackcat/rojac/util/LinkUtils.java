package org.xblackcat.rojac.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xBlackCat
 */

public final class LinkUtils {
    private static final Log log = LogFactory.getLog(LinkUtils.class);

    private static final String URL_PREFIX = "http://((www|gzip)\\.)?rsdn\\.ru/";

    /**
     * Set of rsdn link to message patterns.
     */
    private static Pattern[] rsdnMessageLinkPatterns = new Pattern[]{
            Pattern.compile(URL_PREFIX + "forum/\\w+/(\\d+)\\.(1\\.)?aspx", Pattern.CASE_INSENSITIVE),
            Pattern.compile(URL_PREFIX + "forum/message.aspx\\?mid=(\\d+)&only=1", Pattern.CASE_INSENSITIVE)
    };

    /**
     * Set of rsdn link to messages thread patterns.
     */
    private static Pattern[] rsdnThreadLinkPatterns = new Pattern[]{
            Pattern.compile(URL_PREFIX + "forum/\\w+/(\\d+)\\.flat\\.(\\d+\\.)?aspx", Pattern.CASE_INSENSITIVE),
            Pattern.compile(URL_PREFIX + "forum/message.aspx\\?mid=(\\d+)(&all=1)?", Pattern.CASE_INSENSITIVE)
    };

    private LinkUtils() {
    }

    public static Integer getMessageId(String link) {
        return checkLink(link, rsdnMessageLinkPatterns);
    }

    public static Integer getThreadId(String link) {
        return checkLink(link, rsdnThreadLinkPatterns);
    }

    /**
     * Extracts from text element an URL description text.
     *
     * @param el element of link.
     *
     * @return description of URL from text. If description can not be obtained the <code>null</code> will be returned.
     */
    public static String getUrlText(Element el) {
        int start = el.getStartOffset();
        int length = el.getEndOffset() - start;

        try {
            return el.getDocument().getText(start, length);
        } catch (BadLocationException e) {
            return null;
        }
    }

    /**
     * Tests a link with given set of patterns and extracts message id if match has been found. The <code>null</code>
     * value will be returns if no match has been found.
     *
     * @param link     link to test
     * @param patterns set of patterns
     *
     * @return message id or <code>null</code> if id cannot be extracted from link.
     */
    private static Integer checkLink(String link, Pattern[] patterns) {
        for (Pattern p : patterns) {
            Matcher matcher = p.matcher(link);
            if (matcher.matches()) {
                String id = matcher.group(3);
                try {
                    return Integer.parseInt(id);
                } catch (NumberFormatException e) {
                    if (log.isWarnEnabled()) {
                        log.warn("Link '" + link + "' matches the pattern '" + p.pattern() + "' but has invalid forum id = '" + id);
                    }
                }
            }
        }
        return null;
    }

    public static String buildFlatThreadLink(int messageId) {
        return "http://rsdn.ru/forum/message/" + messageId + ".flat.aspx";
    }

    public static String buildThreadLink(int messageId) {
        return "http://rsdn.ru/forum/message/" + messageId + ".aspx";
    }

    public static String buildMessageLink(int messageId) {
        return "http://rsdn.ru/forum/message/" + messageId + ".1.aspx";
    }
}
