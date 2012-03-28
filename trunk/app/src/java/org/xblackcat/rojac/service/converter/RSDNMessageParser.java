package org.xblackcat.rojac.service.converter;

import org.apache.commons.lang3.ArrayUtils;
import org.xblackcat.rojac.service.converter.tag.RsdnTagList;
import org.xblackcat.rojac.util.MessageUtils;
import org.xblackcat.utils.ResourceUtils;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author xBlackCat
 */

public class RSDNMessageParser implements IMessageParser {
    private static final Pattern PRE_IMG_TAG_CLEANER_PATTERN = Pattern.compile("\\[img\\][\\n\\s]+");
    private static final String PRE_IMG_TAG_CLEANER_REPLACEMENT = "[img]";

    private static final Pattern PRE_QUOTATION_PATTERN = Pattern.compile("^([[^\\s\\p{Punct}]_]*?)>(.*?)$", Pattern.MULTILINE);
    private static final String PRE_QUOTATION_REPLACEMENT = "[span]$1>$2[/span]";

//    private static final Pattern QUOTATION_PATTERN = Pattern.compile("^\\[span\\](.*)\\[/span\\]$", Pattern.MULTILINE);
    private static final Pattern HYPERLINKS_PATTERN = Pattern.compile("([^\\]=]|^)(http(s?)://\\S+?)[\\.,!:]?(\\[|\\]|\\s|$)");

    private static final String HYPERLINKS_REPLACEMENT = "$1[url=$2]$2[/url]$4";

    private final Map<ITag, ITag[]> availableSubTags;
    private static final Comparator<ITagInfo> TAG_COMPARATOR = new Comparator<ITagInfo>() {
        @SuppressWarnings("unchecked")
        public int compare(ITagInfo o1, ITagInfo o2) {
            int m = o1.start() - o2.start();
            if (m == 0) {
                return o1.getTag().compareTo(o2.getTag());
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
        String htmlBody = processText(preHtml, mergeAvailableTags(null));
        htmlBody = processText(htmlBody, RsdnTagList.Original.getTag());
        return "<html>" +
                "<head>" +
                "<link href='" +
                ResourceUtils.getFullPathToResource("/message/message.css") +
                "' rel='stylesheet' type='text/css'/>" +
                "</head>" +
                "<body>" +
                htmlBody +
                "<div class='message_footer'>&nbsp;</div>" +
                "</body>" +
                "</html>";
    }

    /**
     * Searches and highlight all the quotation lines in message. Does not converts line breaks or tags.
     *
     * @param rsdn
     * @return a new string with highlighted quotation lines.
     */
    protected String preProcessText(String rsdn) {
        rsdn = PRE_IMG_TAG_CLEANER_PATTERN.matcher(rsdn).replaceAll(PRE_IMG_TAG_CLEANER_REPLACEMENT);
        rsdn = HYPERLINKS_PATTERN.matcher(rsdn).replaceAll(HYPERLINKS_REPLACEMENT);
        rsdn = PRE_QUOTATION_PATTERN.matcher(rsdn).replaceAll(PRE_QUOTATION_REPLACEMENT);
        rsdn = MessageUtils.escapeHTML(rsdn);
//        rsdn = QUOTATION_PATTERN.matcher(rsdn).replaceAll("<span class='lineQuote'>$1</span>");

        return rsdn;
    }

    private ITag[] mergeAvailableTags(ITag currentTag, ITag... parentTags) {
        if (ArrayUtils.isEmpty(parentTags)) {
            parentTags = availableSubTags.get(null);
        }

        ITag[] subTags = availableSubTags.get(currentTag);
        if (subTags == null) {
            subTags = availableSubTags.get(null); // Rule not set so it is allowed use any of tags
        }

        // Make intersection of two tag sets
        Collection<ITag> tags = new ArrayList<>();
        tags.addAll(Arrays.asList(parentTags));
        tags.retainAll(Arrays.asList(subTags));

        return tags.toArray(new ITag[tags.size()]);
    }

    private String processText(String text, ITag... availableTags) {
        final Collection<ITagInfo<?>> tags = getFoundTags(text, availableTags);

        ITagData tagData = null;
        ITag<?>[] t = null;
        for (ITagInfo<?> ti : tags) {
            tagData = ti.process();
            if (tagData != null) {
                t = mergeAvailableTags(ti.getTag(), availableTags);
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
            if (tagData.hasBody()) {
                res.append(processText(tagData.getBody(), t));
            }
            // Add tail of the tag (close tag)
            res.append(tagData.getTail());
            // Process remaining text with parent tag
            if (tagData.end() < text.length()) {
                res.append(processText(text.substring(tagData.end()), availableTags));
            }
            return res.toString();
        } else {
            return text;
        }
    }

    private Collection<ITagInfo<?>> getFoundTags(String text, ITag... availableTags) {
        if (!ArrayUtils.isEmpty(availableTags)) {
            List<ITagInfo<?>> tags = new LinkedList<>();

            String lowedText = text.toLowerCase();
            for (ITag t : availableTags) {
                ITagInfo<?> ti = t.find(text, lowedText);
                if (ti != null) {
                    tags.add(ti);
                }
            }

            if (tags.size() > 0) {
                Collections.sort(tags, TAG_COMPARATOR);

                return tags;
            }
        }
        return Collections.emptyList();
    }

    /**
     * Only for testing purposes
     */
    Map<ITag, ITag[]> getAvailableSubTags() {
        return availableSubTags;
    }
}
