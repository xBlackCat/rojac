package org.xblackcat.rojac.service.converter.tag;

import org.xblackcat.rojac.service.converter.ITagData;
import org.xblackcat.rojac.service.converter.ITagInfo;

/**
 * @author xBlackCat
 */

public class SingleTag extends SimpleTag {
    private final int priority;

    protected SingleTag(String tag) {
        this(tag, 0);
    }

    protected SingleTag(String tagString, String textTagString) {
        this(tagString, textTagString, 0);
    }

    protected SingleTag(String tag, int priority) {
        this(tag, tag, priority);
    }

    protected SingleTag(String tagString, String textTagString, int priority) {
        super(tagString, tagString, textTagString, textTagString);
        this.priority = priority;
    }

    protected ITagInfo getTagInfo(final String text, final String lower, final int startPos) {
        return new SingleTagInfo(startPos, lower, text);
    }

    protected class SinlgeTagData extends SimpleTagData {
        public SinlgeTagData(int startPos, String text) {
            super(startPos, startPos, text);
        }

        public String getTail() {
            return ""; // The tag has no tail
        }

        public String getBody() {
            return "";  // The tag has no body
        }
    }

    protected class SingleTagInfo extends SimpleTagInfo {
        public SingleTagInfo(int startPos, String lower, String text) {
            super(startPos, lower, text);
        }

        public ITagData process() {
            return new SinlgeTagData(startPos, text);
        }
    }

    public int compareTo(SimpleTag o) {
        if (o instanceof SingleTag) {
            return ((SingleTag) o).priority - priority;
        } else {
            return super.compareTo(o);
        }
    }
}
