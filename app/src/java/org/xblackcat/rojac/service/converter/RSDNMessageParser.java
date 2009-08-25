package org.xblackcat.rojac.service.converter;

import org.apache.commons.lang.ArrayUtils;
import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;
import org.apache.commons.lang.StringUtils;
import org.xblackcat.utils.ResourceUtils;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Date: 19 лют 2008
 *
 * @author xBlackCat
 */

public class RSDNMessageParser implements IMessageParser {
    private static final Pattern PRE_QUOTATION_PATTERN = Pattern.compile("^([[^\\s\\p{Punct}]_]+?)>(.*?)$", Pattern.MULTILINE);
    private static final Pattern QUOTATION_PATTERN = Pattern.compile("^\\[span\\](.*)\\[/span\\]$", Pattern.MULTILINE);
    private static final Pattern HYPERLINKS_PATTERN = Pattern.compile("([^\\]=]|^)(http(s?)://\\S+)[\\.,\\!\\:]?$", Pattern.MULTILINE);
    private static final ITagInfo[] EMPTY_TAG_INFO = new ITagInfo[0];

    private final Map<ITag, ITag[]> availableSubTags;
    private static final Comparator<ITagInfo> TAG_INFO_COMPARATOR = new Comparator<ITagInfo>() {
        public int compare(ITagInfo o1, ITagInfo o2) {
            return new Integer(o1.start()).compareTo(o2.start());
        }
    };
    private static final Comparator<ITagInfo> TAG_COMPARATOR = new Comparator<ITagInfo>() {
        public int compare(ITagInfo o1, ITagInfo o2) {
            int m = o1.start() - o2.start();
            if (m == 0) {
                return o1.getTag().compareTo(o2.getTag()) ;
            } else {
                return m;
            }
        }
    };

    RSDNMessageParser(Map<ITag, ITag[]> tagRelationMap) {
        availableSubTags = Collections.unmodifiableMap(tagRelationMap);
    }

    public String convert(String rsdn) {
        String preHtml = preProcessText(rsdn);
        String htmlBody = processText(mergeAvailableTags(null, null), preHtml);
        return "<html>" +
                "<head>" +
                "<link href='" +
                ResourceUtils.getFullPathToResource("/message/message.css") +
                "' rel='stylesheet' type='text/css'/>" +
                "</head>" +
                "<body>" +
                htmlBody+
                "</body>" +
                "</html>";
    }

    /**
     * Searchs and highlight all the quotation lines in message. Does not converts line breaks or tags.
     *
     * @param rsdn
     *
     * @return a new string with highlihgted quotation lines.
     */
    protected String preProcessText(String rsdn) {
        rsdn = HYPERLINKS_PATTERN.matcher(rsdn).replaceAll("$1[url=$2]$2[/url]");
        rsdn = PRE_QUOTATION_PATTERN.matcher(rsdn).replaceAll("[span]$1>$2[/span]");
        rsdn = escapeHtml(rsdn);
        rsdn = QUOTATION_PATTERN.matcher(rsdn).replaceAll("<span class='lineQuote'>$1</span>");
        
        return rsdn;
    }

    private ITag[] mergeAvailableTags(ITag[] parentTags, ITag currentTag) {
        if (parentTags == null) {
            parentTags = availableSubTags.get(null);
        }

        ITag[] subTags = availableSubTags.get(currentTag);
        if (subTags == null) {
            subTags = availableSubTags.get(null); // Rule not set so it is allowed use any of tags
        }

        // Make intersection of two tag sets
        Collection<ITag> tags = new ArrayList<ITag>();
        tags.addAll(Arrays.asList(parentTags));
        tags.retainAll(Arrays.asList(subTags));

        return tags.toArray(new ITag[tags.size()]);
    }

    private String processText(ITag[] availableTags, String text) {
        final ITagInfo[] tags = getFoundTags(availableTags, text);

        Arrays.sort(tags, TAG_INFO_COMPARATOR);

        ITagData tagData = null;
        ITag[] t = null;
        for (ITagInfo ti : tags) {
            tagData = ti.process();
            if (tagData != null) {
                t = mergeAvailableTags(availableTags, ti.getTag());
                break;
            }
        }

        if (tagData != null) {
            StringBuilder res = new StringBuilder();
            // Add begining text without modifications
            if (tagData.start() > 0) {
                res.append(text.substring(0, tagData.start()));
            }
            // Add head of the tag (open tag)
            res.append(tagData.getHead());
            // Add processed body tags
            if (StringUtils.isNotEmpty(tagData.getBody())) {
                res.append(processText(t, tagData.getBody()));
            }
            // Add tail of the tag (close tag)
            res.append(tagData.getTail());
            // Process remaining text with parent tag
            if (tagData.end() < text.length()) {
                res.append(processText(availableTags, text.substring(tagData.end())));
            }
            return res.toString();
        } else {
            return text;
        }
    }

    private ITagInfo[] getFoundTags(ITag[] availableTags, String text) {
        if (!ArrayUtils.isEmpty(availableTags)) {
            Collection<ITagInfo> tags = new LinkedList<ITagInfo>();

            String lowedText = text.toLowerCase();
            for (ITag t : availableTags) {
                ITagInfo ti = t.find(text, lowedText);
                if (ti != null) {
                    tags.add(ti);
                }
            }

            ITagInfo[] tagInfos = tags.toArray(new ITagInfo[tags.size()]);

            Arrays.sort(tagInfos, TAG_COMPARATOR);

            return tagInfos;
        } else {
            return EMPTY_TAG_INFO;
        }
    }

    /**
     * Only for testing purposes
     */
    Map<ITag, ITag[]> getAvailableSubTags() {
        return availableSubTags;
    }
}
