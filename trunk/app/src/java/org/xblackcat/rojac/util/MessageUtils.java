package org.xblackcat.rojac.util;

import org.apache.commons.lang.StringUtils;
import org.xblackcat.rojac.i18n.Messages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xBlackCat
 */

public final class MessageUtils {
    private static final Pattern SUBJ_PATTERN = Pattern.compile("^Re(?:\\[(\\d+)\\])?:\\s+(.+)$");
    private static final Pattern RESP_PATTERN = Pattern.compile("^([A-Z_]>)(.+)$");
    protected static final Pattern TAGLINE_PATTERN = Pattern.compile("\\[tagline\\].+\\[/tagline\\]");


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
}
