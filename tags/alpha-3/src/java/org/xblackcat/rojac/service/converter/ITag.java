package org.xblackcat.rojac.service.converter;

/**
 * @author xBlackCat
 */

public interface ITag<T extends ITag> extends Comparable<T> {
    ITagInfo find(String text, String lower);
}
