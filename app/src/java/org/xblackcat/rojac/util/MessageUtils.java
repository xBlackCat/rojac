package org.xblackcat.rojac.util;

import gnu.trove.map.hash.TIntObjectHashMap;
import org.apache.commons.lang3.StringUtils;
import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.data.MarkStat;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.RatingCache;
import org.xblackcat.rojac.gui.view.ViewId;
import org.xblackcat.rojac.gui.view.thread.MessageReadFlagSetter;
import org.xblackcat.rojac.i18n.LocaleControl;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.executor.IExecutor;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.IRatingAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.service.storage.StorageException;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xBlackCat
 */

public final class MessageUtils {
    private static final Pattern SUBJ_PATTERN = Pattern.compile("^Re(?:\\[(\\d+)\\])?:\\s+(.+)$");
    private static final Pattern RESP_PATTERN = Pattern.compile("^([A-Z_]>)(.+)$");

    protected static final Pattern TAGLINE_PATTERN = Pattern.compile("\\[tagline\\].+\\[/tagline\\]");
    private static final TIntObjectHashMap<String> elements;
    public static final Pattern DOTS_PATTERN = Pattern.compile("\\.{2,}");

    static {
        elements = new TIntObjectHashMap<>();
        elements.put(38, "amp"); // & - ampersand
        elements.put(60, "lt"); // < - less-than
        elements.put(62, "gt"); // > - greater-than
    }

    private MessageUtils() {
    }

    public static String correctSubject(String subj) {
        subj = subj.trim();

        Matcher matcher = SUBJ_PATTERN.matcher(subj);
        if (matcher.matches()) {
            String ind = matcher.group(1);
            int i = StringUtils.isEmpty(ind) ? 2 : (Integer.parseInt(ind) + 1);

            subj = "Re[" + i + "]: " + matcher.group(2).trim();
        } else {
            subj = "Re: " + subj;
        }
        return subj;
    }

    public static String correctBody(String body, String userName) {
        // Remove all taglines from the body
        body = removeTagline(body);

        StringBuilder res = new StringBuilder();

        res.append(Message.Message_Response_Header.get(userName));

        String abbr = abbreviateUserName(userName);

        for (String row : StringUtils.split(body, '\n')) {
            Matcher respMatcher = RESP_PATTERN.matcher(row);
            if (respMatcher.matches()) {
                res.append(respMatcher.group(1));
                res.append('>');
                res.append(respMatcher.group(2));
            } else {
                res.append(abbr);
                res.append("> ");
                res.append(row);
            }
            res.append('\n');
        }

        return res.toString();
    }

    public static String removeTagline(String body) {
        body = TAGLINE_PATTERN.matcher(body).replaceAll("");
        return body;
    }

    static String abbreviateUserName(String userName) {
        if (StringUtils.isEmpty(userName)) {
            return "A";
        }

        StringBuilder res = new StringBuilder();

        char[] chars = userName.toCharArray();
        for (char c : chars) {
            if (Character.isLetter(c) &&
                    Character.isUpperCase(c) ||
                    c == '_') {
                res.append(c);
            }
        }

        if (res.length() == 0) {
            res.append(Character.toUpperCase(chars[0]));
        }

        return res.toString();
    }

    public static String addOwnTagLine(String body) {
        body = removeTagline(body);

        body += "[tagline] [url=http://code.google.com/p/rojac/]" + RojacUtils.VERSION_STRING + "[/url] [/tagline]";

        return body;
    }

    public static RatingCache updateRatingCache(int id) throws StorageException {
        IMessageAH mAH = Storage.get(IMessageAH.class);
        IRatingAH rAH = Storage.get(IRatingAH.class);

        Collection<MarkStat> marks = rAH.getMarkStatByMessageId(id);
        RatingCache ratingCache = new RatingCache(marks);
        if (!ratingCache.isEmpty()) {
            mAH.updateMessageRatingCache(id, ratingCache.asString());
        }
        return ratingCache;
    }


    /**
     * The method is copied from String utils to prevent modify russian letters into &#&lt;code&gt; form
     *
     * @param str string to escape.
     * @return escaped string.
     */
    public static String escapeHTML(String str) {
        StringWriter writer = new StringWriter((int) (str.length() * 1.2));

        int len = str.length();
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            String entityName = elements.get(c);
            if (entityName == null) {
                writer.write(c);
            } else {
                writer.write('&');
                writer.write(entityName);
                writer.write(';');
            }
        }

        return writer.toString();
    }

    public static ImageIcon buildRateImage(RatingCache ratings, Font targetFont, Color textColor) {
        if (ratings.isEmpty()) {
            return null;
        }

        int rate = ratings.getRating(Mark.x1) + 2 * ratings.getRating(Mark.x2) + 3 * ratings.getRating(Mark.x3);
        int rateAmount = ratings.getRating(Mark.x1) + ratings.getRating(Mark.x2) + ratings.getRating(Mark.x3);

        int width = 0;
        if (rate > 0) {
            width += 90;
        }
        if (ratings.getRating(Mark.Agree) > 0) {
            width += 70;
        }
        if (ratings.getRating(Mark.Disagree) > 0) {
            width += 70;
        }
        if (ratings.getRating(Mark.Smile) > 0) {
            width += 70;
        }
        if (ratings.getRating(Mark.PlusOne) > 0) {
            width += 70;
        }

        if (width == 0) {
            return null;
        }
        BufferedImage im = new BufferedImage(width, 16, BufferedImage.TYPE_INT_ARGB);

        Graphics g = im.getGraphics();
        int offset = 0;

        g.setColor(textColor);
        g.setFont(targetFont);
        if (rateAmount > 0) {
            String rateStr = rate + "(" + rateAmount + ")";
            Rectangle2D bounds = g.getFontMetrics().getStringBounds(rateStr, g);
            g.drawString(rateStr, 0, (int) bounds.getHeight() - 2);
            offset += bounds.getWidth();
        }

        offset += addIcon(g, offset, Mark.PlusOne, ratings.getRating(Mark.PlusOne));
        offset += addIcon(g, offset, Mark.Agree, ratings.getRating(Mark.Agree));
        offset += addIcon(g, offset, Mark.Disagree, ratings.getRating(Mark.Disagree));
        offset += addIcon(g, offset, Mark.Smile, ratings.getRating(Mark.Smile));

        im.flush();

        return new ImageIcon(im.getSubimage(0, 0, offset, im.getHeight()));
    }

    private static int addIcon(Graphics g, int offset, Mark mark, int amount) {
        if (amount == 0) {
            return 0;
        }

        g.drawImage(mark.getIcon().getImage(), offset + 2, 0, null);
        int width = 2 + mark.getIcon().getIconWidth();

        if (amount > 1) {
            String amountString = "x" + amount;
            Rectangle2D bounds = g.getFontMetrics().getStringBounds(amountString, g);
            g.drawString(amountString, offset + width, (int) bounds.getHeight());
            width += bounds.getWidth();
        }

        return width;
    }

    /**
     * Set message read flag instantly (delay = 0) or after some time.
     *
     * @param id    view id to associate a new timer with.
     * @param data  message data to identify message
     * @param delay period in seconds to be waited before read flag is set. Zero means set the flag instantly. Negative
     *              value means automatic setting flag is disabled.
     */
    public static void markMessageRead(ViewId id, MessageData data, long delay) {
        if (delay < 0) {
            // Negative value means "disable"
            return;
        }

        MessageReadFlagSetter target = new MessageReadFlagSetter(true, data);
        IExecutor executor = ServiceFactory.getInstance().getExecutor();
        if (delay > 0) {
            executor.setupTimer(id.getAnchor(), target, delay);
        } else {
            executor.killTimer(id.getAnchor());
            executor.execute(target);
        }
    }

    public static String constantCamelToPropertyName(String s) {
        if (s == null) {
            return null;
        }
        StringBuilder result = new StringBuilder(s.length() << 1);
        boolean wasDot = true;
        for (char c : s.toCharArray()) {
            if (Character.isUpperCase(c)) {
                if (!wasDot) {
                    result.append('_');
                }
                c = Character.toLowerCase(c);
            }
            if (c == '_') {
                result.append('.');
                wasDot = true;
            } else {
                result.append(c);
                wasDot = false;
            }
        }

        return DOTS_PATTERN.matcher(result).replaceAll(".");
    }

    public static String formatDate(long messageDate) {
        Date date;
        if (messageDate > 0) {
            date = new Date(messageDate);
        } else {
            date = new Date();
        }
        return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, LocaleControl.getInstance().getLocale()).format(date);
    }

    public static String formatDuration(Long duration) {
        if (duration == null) {
            return "--:--:--";
        }

        long hours = duration;

        int seconds = (int) hours % 60;
        hours /= 60;
        int minutes = (int) hours % 60;
        hours /= 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
