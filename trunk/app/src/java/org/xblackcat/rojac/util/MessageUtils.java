package org.xblackcat.rojac.util;

import gnu.trove.TIntObjectHashMap;
import org.apache.commons.lang.StringUtils;
import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.i18n.Messages;

import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xBlackCat
 */

public final class MessageUtils {
    public final static Mark[] NO_MARKS = new Mark[0];

    private static final Pattern SUBJ_PATTERN = Pattern.compile("^Re(?:\\[(\\d+)\\])?:\\s+(.+)$");
    private static final Pattern RESP_PATTERN = Pattern.compile("^([A-Z_]>)(.+)$");
    protected static final Pattern TAGLINE_PATTERN = Pattern.compile("\\[tagline\\].+\\[/tagline\\]");

    private static final TIntObjectHashMap<String> elements;

    static {
        elements = new TIntObjectHashMap<String>();
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
        body = TAGLINE_PATTERN.matcher(body).replaceAll("");

        StringBuilder res = new StringBuilder();

        res.append(Messages.MESSAGE_RESPONSE_HEADER.get(userName));

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

    static String abbreviateUserName(String userName) {
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
        body = TAGLINE_PATTERN.matcher(body).replaceAll("");

        body += "[tagline] " + RojacUtils.VERSION_STRING + " [/tagline]";

        return body;
    }

    /**
     * The method is copied from String utils to prevent modify russian letters into &#&lt;code&gt; form
     *
     * @param str string to escape.
     *
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

    public static String buildRateString(Mark[] ratings) {
        int smiles = 0;
        int agrees = 0;
        int disagrees = 0;
        int plusOnes = 0;
        int rate = 0;
        int rateAmount = 0;

        for (Mark r : ratings) {
            switch (r) {
                case Agree:
                    ++agrees;
                    break;
                case Disagree:
                    ++disagrees;
                    break;
                case PlusOne:
                    ++plusOnes;
                    break;
                case Remove:
                    // Do nothig
                    break;
                case Smile:
                    ++smiles;
                    break;
                case x3:
                    ++rate;
                case x2:
                    ++rate;
                case x1:
                    ++rate;
                    ++rateAmount;
                    break;
            }
        }

        StringBuilder text = new StringBuilder("<html><body>");

        if (rateAmount > 0) {
            text.append("<b>");
            text.append(rate);
            text.append("(");
            text.append(rateAmount);
            text.append(")</b> ");
        }

        text.append(addInfo(Mark.PlusOne, plusOnes));
        text.append(addInfo(Mark.Agree, agrees));
        text.append(addInfo(Mark.Disagree, disagrees));
        text.append(addInfo(Mark.Smile, smiles));

        return text.toString();
    }

    public static String addInfo(Mark m, int amount) {
        if (amount > 0) {
            String res = "&nbsp;<img src='" + m.getUrl().toString() + "'>";
            if (amount > 1) {
                return res + "<i>x" + amount + "</i>";
            } else {
                return res;
            }
        }
        return "";
    }
}
