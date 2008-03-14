package org.xblackcat.sunaj.service.converter;

/**
 * Date: 19 лют 2008
 *
 * @author xBlackCat
 */

public interface ITag<T extends ITag> extends Comparable<T> {
    ITagInfo find(String text, String lower);
}
