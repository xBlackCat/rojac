package org.xblackcat.rojac.service.converter.tag;

import org.xblackcat.rojac.service.converter.ITag;
import org.xblackcat.rojac.service.converter.ITagData;
import org.xblackcat.rojac.service.converter.ITagInfo;

/**
 * @author xBlackCat
 */

public class SimpleTag implements ITag<SimpleTag> {
    protected final String openTag;
    protected final String closeTag;
    protected final String openTextTag;
    protected final String closeTextTag;

    protected SimpleTag(String tag) {
        this(tag, tag);
    }

    protected SimpleTag(String tag, String textTag) {
        this(tag, "<" + textTag + ">", "</" + textTag + ">");
    }

    protected SimpleTag(String tag, String openTextTag, String closeTextTag) {
        this("[" + tag + "]", "[/" + tag + "]", openTextTag, closeTextTag);
    }

    protected SimpleTag(String openTag, String closeTag, String openTextTag, String closeTextTag) {
        this.openTag = openTag.toLowerCase();
        this.closeTag = closeTag.toLowerCase();
        this.openTextTag = openTextTag;
        this.closeTextTag = closeTextTag;
    }

    public ITagInfo<SimpleTag> find(final String text, String lower) {
        final int startPos = lower.indexOf(openTag);

        if (startPos == -1) {
            return null;
        }

        return getTagInfo(text, lower, startPos);
    }

    /**
     * Constructs a tag info object.
     *
     * @param text     original text.
     * @param lower    lowercased original text (used for case-insensitive search)
     * @param startPos position in text where open tag is located
     * @return tag info object
     */
    protected ITagInfo<SimpleTag> getTagInfo(String text, String lower, int startPos) {
        return new SimpleTagInfo(startPos, lower, text);
    }

    public int compareTo(SimpleTag o) {
        return 0;
    }

    protected class SimpleTagInfo implements ITagInfo<SimpleTag> {
        protected final int startPos;
        protected final String lower;
        protected final String text;

        public SimpleTagInfo(int startPos, String lower, String text) {
            this.startPos = startPos;
            this.lower = lower;
            this.text = text;
        }

        public int start() {
            return startPos;
        }

        public SimpleTag getTag() {
            return SimpleTag.this;
        }

        public ITagData process() {
            final int endPos = lower.indexOf(closeTag, startPos);

            if (endPos == -1) {
                return null;
            }

            return getTagData(endPos, startPos, text);
        }

        /**
         * Constructs tag data object.
         *
         * @param endPos   position in text where close tag is located
         * @param startPos position in text where open tag is located
         * @param text     original text to parse.
         * @return tag data object
         */
        protected ITagData getTagData(int endPos, int startPos, String text) {
            return new SimpleTagData(startPos, endPos, text);
        }
    }

    protected class SimpleTagData implements ITagData {
        protected final int startPos;
        protected final int endPos;
        protected final String text;

        public SimpleTagData(int startPos, int endPos, String text) {
            this.startPos = startPos;
            this.endPos = endPos;
            this.text = text;
        }

        public int start() {
            return startPos;
        }

        public int end() {
            return endPos + closeTag.length();
        }

        public String getHead() {
            return openTextTag;
        }

        public String getTail() {
            return closeTextTag;
        }

        public String getBody() {
            return text.substring(startPos + openTag.length(), endPos);
        }

        @Override
        public boolean hasBody() {
            return startPos + openTag.length() < endPos;
        }
    }
}
