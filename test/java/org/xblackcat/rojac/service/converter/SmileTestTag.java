package org.xblackcat.rojac.service.converter;

/**
 * Simple tag ':smile:'
 * <p/>
 *
 * @author xBlackCat
 */

public class SmileTestTag implements ITag<SmileTestTag> {
    private final static String TAG = ":smile:";

    public ITagInfo<SmileTestTag> find(String text, String lower) {
        final int pos = text.indexOf(TAG);

        if (pos == -1) {
            return null;
        }

        return new ITagInfo<SmileTestTag>() {
            public int start() {
                return pos;
            }

            public SmileTestTag getTag() {
                return SmileTestTag.this;
            }

            public ITagData process() {
                return new ITagData() {
                    public int start() {
                        return pos;
                    }

                    public int end() {
                        return pos + TAG.length();
                    }

                    public String getHead() {
                        return ":)";
                    }

                    public String getTail() {
                        return "";
                    }

                    public String getBody() {
                        return "";
                    }

                    @Override
                    public boolean hasBody() {
                        return false;
                    }
                };
            }
        };
    }

    public int compareTo(SmileTestTag o) {
        return 0;
    }
}
