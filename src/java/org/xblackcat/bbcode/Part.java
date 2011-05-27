package org.xblackcat.bbcode;

/**
 * @author xBlackCat
 */
class Part {
    private final String base;
    private final int startOffset;
    private final int endOffset;
    private final int length;

    Part(String base, int startOffset, int length) {
        if (base == null) {
            throw new NullPointerException("Base can not be null!");
        }
        if (startOffset < 0 || length <= 0 || startOffset + length > base.length()) {
            throw new IllegalArgumentException("Invalid offset(s): start = " + startOffset + ", length = " + length + ", base length = " + base.length());
        }

        this.base = base;
        this.startOffset = startOffset;
        this.length = length;
        this.endOffset = startOffset + length;
    }

    Part(String base) {
        this(base, 0, base.length());
    }

    boolean isTag() {
        return base.charAt(startOffset) == '[' && base.charAt(endOffset - 1) == ']';
    }

    @Override
    public String toString() {
        return base.substring(startOffset, endOffset);
    }
}
