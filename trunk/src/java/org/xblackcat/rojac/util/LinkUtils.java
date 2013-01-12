package org.xblackcat.rojac.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import java.net.URL;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xBlackCat
 */

public final class LinkUtils {
    private static final Log log = LogFactory.getLog(LinkUtils.class);

    private static final String URL_PREFIX = "http://((www|gzip)\\.)?rsdn\\.ru/";
    private static final Pattern YOUTUBE_LINK = Pattern.compile("(&|^)v=([-\\w]+)($|&)");

    /**
     * Set of rsdn link to message patterns.
     */
    private final static Pattern[] rsdnMessageLinkPatterns = new Pattern[]{
            Pattern.compile(URL_PREFIX + "forum/[\\w\\.]+/(\\d+)\\.1(\\.aspx)?(\\?.+)?", Pattern.CASE_INSENSITIVE),
            Pattern.compile(URL_PREFIX + "forum/message.aspx\\?mid=(\\d+)&only=1", Pattern.CASE_INSENSITIVE)
    };

    /**
     * Set of rsdn link to messages thread patterns.
     */
    private final static Pattern[] rsdnThreadLinkPatterns = new Pattern[]{
            Pattern.compile(URL_PREFIX + "forum/[\\w\\.]+/(\\d+)\\.(flat|all)(\\.\\d+)?(\\.aspx)?(\\?.+)?", Pattern.CASE_INSENSITIVE),
            Pattern.compile(URL_PREFIX + "forum/message.aspx\\?mid=(\\d+)(&all=1)?", Pattern.CASE_INSENSITIVE)
    };

    private static Pattern[] rsdnLinkAllPatterns = new Pattern[]{
            Pattern.compile(URL_PREFIX + "forum/[\\w\\.]+/(\\d+)(\\..+)?(\\.aspx)?(\\?.+)?", Pattern.CASE_INSENSITIVE),
            Pattern.compile(URL_PREFIX + "forum/message.aspx\\?mid=(\\d+).*?", Pattern.CASE_INSENSITIVE)
    };
    private static final Pattern YOUTUBE_EMBED_LINK = Pattern.compile("/embed/([-\\w]+)(/|$)");

    private LinkUtils() {
    }

    /**
     * Extracts message id either from to-message or to-thread url
     *
     * @param link link to check
     * @return extracted id or <code>null</code> if url is invalid.
     */
    public static Integer getMessageIdFromUrl(String link) {
        return checkLink(link, rsdnLinkAllPatterns);
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
     * @return message id or <code>null</code> if id cannot be extracted from link.
     */
    private static Integer checkLink(String link, Pattern... patterns) {
        if (link == null) {
            return null;
        }

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

    public static boolean isYoutubeLink(URL url) {
        String host = url.getHost();
        if (host == null) {
            return false;
        }

        if (host.endsWith("youtube.com")) {
            return true;
        } else if (host.endsWith("youtu.be")) {
            return true;
        }

        return false;
    }

    public static String getYoutubeVideoId(URL url) {
        String host = url.getHost();
        if (host == null) {
            return null;
        }

        host = host.toLowerCase(Locale.ROOT);

        if (host.endsWith("youtube.com")) {
            if (url.getQuery() != null) {
                Matcher m = YOUTUBE_LINK.matcher(url.getQuery());
                if (m.find()) {
                    return m.group(2);
                }
            } else {
                // is embed?
                Matcher m = YOUTUBE_EMBED_LINK.matcher(url.getFile());
                if (m.find()) {
                    return m.group(1);
                }
            }
        } else if (host.endsWith("youtu.be")) {
            return url.getPath().substring(1);
        }

        return null;
    }

    public static boolean isImageLink(URL url) {
        String path = url.getPath();
        if (path == null) {
            return false;
        }

        path = path.toLowerCase(Locale.ROOT);

        return path.endsWith(".png") ||
                path.endsWith(".jpg") ||
                path.endsWith(".jpeg") ||
                path.endsWith(".gif") ||
                path.endsWith(".bmp");
    }
}
