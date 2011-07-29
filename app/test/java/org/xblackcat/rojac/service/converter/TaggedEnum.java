package org.xblackcat.rojac.service.converter;

/**
 * @author xBlackCat
 */

public enum TaggedEnum implements ITag<TaggedEnum> {
    Smile(new SmileTestTag());

    private final ITag tag;

    TaggedEnum(ITag tag) {
        this.tag = tag;
    }

    @SuppressWarnings("unchecked")
    public ITagInfo<TaggedEnum> find(String text, String lower) {
        return tag.find(text, text.toLowerCase());
    }
}
