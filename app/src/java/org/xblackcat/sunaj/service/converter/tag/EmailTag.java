package org.xblackcat.sunaj.service.converter.tag;

import org.xblackcat.sunaj.service.converter.ITagData;
import org.xblackcat.sunaj.service.converter.ITagInfo;

/**
 * Date: 20 лют 2008
 *
 * @author xBlackCat
 */

public class EmailTag extends SimpleTag {
    private final String openTextTagEnd;

    public EmailTag() {
        super("email", "<a href='mailto:", "</a>");
        this.openTextTagEnd = "'>";
    }

    protected ITagInfo getTagInfo(final String text, final String lower, final int startPos) {
        return new EmailTagInfo(startPos, lower, text);
    }

    protected class EmailTagData extends SimpleTagData {
        public EmailTagData(int startPos, int endPos, String text) {
            super(startPos, endPos, text);
        }

        public String getHead() {
            return openTextTag + getBody() + openTextTagEnd;
        }
    }

    protected class EmailTagInfo extends SimpleTagInfo {
        public EmailTagInfo(int startPos, String lower, String text) {
            super(startPos, lower, text);
        }

        public ITagData process() {
            final int endPos = lower.indexOf(closeTag, startPos);

            if (endPos == -1) {
                return null;
            }

            return new EmailTagData(startPos, endPos, text);
        }
    }
}