package org.xblackcat.rojac.service.converter.tag;

import org.apache.commons.lang3.StringUtils;
import org.xblackcat.rojac.service.converter.ITag;
import org.xblackcat.rojac.service.converter.ITagData;
import org.xblackcat.rojac.service.converter.ITagInfo;
import org.xblackcat.utils.ResourceUtils;

/**
 * @author xBlackCat
 */

public final class TestUtils {
    private TestUtils() {
    }

    /**
     * Applies once given tag to given text if it possible to do
     *
     * @param text
     * @param tag
     *
     * @return
     */
    public static String applyTag(String text, ITag tag) {
        final ITagInfo tagInfo = tag.find(text, text.toLowerCase());

        if (tagInfo == null) {
            return text;
        }

        final ITagData tagData = tagInfo.process();

        if (tagData == null) {
            return text;
        }

        StringBuilder res = new StringBuilder();
        // Add begining text without modifications
        if (tagData.start() > 0) {
            res.append(text.substring(0, tagData.start()));
        }
        // Add head of the tag (open tag)
        res.append(tagData.getHead());
        // Add processed body tags
        if (StringUtils.isNotEmpty(tagData.getBody())) {
            res.append(tagData.getBody());
        }
        // Add tail of the tag (close tag)
        res.append(tagData.getTail());
        // Process remaining text with parent tag
        if (tagData.end() < text.length()) {
            res.append(text.substring(tagData.end()));
        }
        return res.toString();
    }

    public static String applyTags(String text, ITag... tags) {
        for (ITag t : tags) {
            while (t.find(text, text.toLowerCase()) != null) {
                text = applyTag(text, t);
            }
        }
        return text;
    }

    public static String makeExpected(String expected) {
        return "<html><head><link href='" +
                ResourceUtils.getFullPathToResource("/message/message.css")
                + "' rel='stylesheet' type='text/css'/></head><body>" +
                expected +
                "<div class='message_footer'>&nbsp;</div></body></html>";
    }
}
