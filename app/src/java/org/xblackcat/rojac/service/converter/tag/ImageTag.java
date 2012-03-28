package org.xblackcat.rojac.service.converter.tag;

import org.xblackcat.rojac.service.converter.ITagData;
import org.xblackcat.rojac.service.converter.ITagInfo;

/**
 * @author xBlackCat
 */

public class ImageTag extends SimpleTag {
    public ImageTag() {
        super("img", "<img border='0' src='", "'/>");
    }

    protected ITagInfo<SimpleTag> getTagInfo(final String text, final String lower, final int startPos) {
        return new ImageTagInfo(startPos, lower, text);
    }

    protected class TrimmedTagData extends SimpleTagData {
        private final int startBodyPos;
        private final int endBodyPos;

        public TrimmedTagData(int startPos, int endPos, int startBodyPos, int endBodyPos, String text) {
            super(startPos, endPos, text);
            this.startBodyPos = startBodyPos;
            this.endBodyPos = endBodyPos;
        }

        @Override
        public String getBody() {
            return text.substring(startBodyPos, endBodyPos);
        }

        @Override
        public boolean hasBody() {
            return startBodyPos < endBodyPos;
        }
    }

    protected class ImageTagInfo extends SimpleTagInfo {
        public ImageTagInfo(int startPos, String lower, String text) {
            super(startPos, lower, text);
        }

        public ITagData process() {
            final int endPos = lower.indexOf(closeTag, startPos);

            if (endPos == -1) {
                return null;
            }


            int startBodyPos = startPos + openTag.length();

            int endBodyPos = endPos;

            while ((startBodyPos < endBodyPos) && (text.charAt(startBodyPos) <= ' ')) {
                startBodyPos++;
            }
            while ((startBodyPos < endBodyPos) && (text.charAt(endBodyPos - 1) <= ' ')) {
                endBodyPos--;
            }

            return new TrimmedTagData(startPos, endPos, startBodyPos, endBodyPos, text);
        }
    }
}
