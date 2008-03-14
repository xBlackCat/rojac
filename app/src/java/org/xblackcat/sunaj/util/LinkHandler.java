package org.xblackcat.sunaj.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date: 7 ñ³÷ 2008
 *
 * @author xBlackCat
 */

public final class LinkHandler {
    private static final Log log = LogFactory.getLog(LinkHandler.class);

    private static final String URL_PREFIX = "http://((www|gzip)\\.)?rsdn\\.ru/";

    /**
     * Set of rsdn link to message patterns.
     */
    private static Pattern[] rsdnMessageLinkPatterns = new Pattern[]{
            Pattern.compile(URL_PREFIX + "forum/message/(\\d+)\\.1\\.aspx", Pattern.CASE_INSENSITIVE),
            Pattern.compile(URL_PREFIX + "forum/message.aspx\\?mid=(\\d+)&only=1", Pattern.CASE_INSENSITIVE)
    };

    /**
     * Set of rsdn link to messages thread patterns.
     */
    private static Pattern[] rsdnThreadLinkPatterns = new Pattern[]{
            Pattern.compile(URL_PREFIX + "forum/message/(\\d+)\\.flat\\.(\\d+\\.)?aspx", Pattern.CASE_INSENSITIVE),
            Pattern.compile(URL_PREFIX + "forum/message.aspx\\?mid=(\\d+)(&all=1)?", Pattern.CASE_INSENSITIVE)
    };

    private LinkHandler() {
    }

    public static Integer getMessageId(String link) {
        return checkLink(link, rsdnMessageLinkPatterns);
    }

    public static Integer getThreadId(String link) {
        return checkLink(link, rsdnThreadLinkPatterns);
    }

    /**
     * Tests a link with given set of patterns and extracts message id if match has been found. The <code>null</code>
     * value will be returns if no match has been found.
     *
     * @param link link to test
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
}
