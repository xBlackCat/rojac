package org.xblackcat.rojac.service.converter;

/**
 * @author xBlackCat
 */

public interface ITagInfo<T extends ITag<T>> {
    int start();

    T getTag();

    /**
     * Processes tag. Returns {@code null} if tag has illegal format or incomplete
     *
     * @return
     */
    ITagData process();
}
