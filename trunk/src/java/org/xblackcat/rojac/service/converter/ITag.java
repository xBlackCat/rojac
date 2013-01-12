package org.xblackcat.rojac.service.converter;

/**
 * @author xBlackCat
 */

public interface ITag<T extends ITag<T>> extends Comparable<T> {
    ITagInfo<T> find(String text, String lower);
}
