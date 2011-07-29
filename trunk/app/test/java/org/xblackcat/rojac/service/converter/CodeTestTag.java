package org.xblackcat.rojac.service.converter;

/**
 * @author xBlackCat
 */

public class CodeTestTag implements ITag<CodeTestTag> {
    private static final String OPEN_TAG = "[code]";
    private static final String CLOSE_TAG = "[/code]";

    public ITagInfo<CodeTestTag> find(final String text, final String lower) {
        final int startPos = lower.indexOf(OPEN_TAG);

        if (startPos == -1) {
            return null;
        }

        return new ITagInfo<CodeTestTag>() {
            public int start() {
                return startPos;
            }

            public CodeTestTag getTag() {
                return CodeTestTag.this;
            }

            public ITagData process() {
                final int endPos = lower.indexOf(CLOSE_TAG, startPos);

                if (endPos == -1) {
                    return null;
                }

                return new ITagData() {
                    public int start() {
                        return startPos;
                    }

                    public int end() {
                        return endPos + CLOSE_TAG.length();
                    }

                    public String getHead() {
                        return "<p>";
                    }

                    public String getTail() {
                        return "</p>";
                    }

                    public String getBody() {
                        return text.substring(startPos + OPEN_TAG.length(), endPos);
                    }
                };
            }
        };
    }

    public int compareTo(CodeTestTag o) {
        return 0;
    }
}
