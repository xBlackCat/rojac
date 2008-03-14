package org.xblackcat.sunaj.service.converter;

/**
 * Date: 19 лют 2008
 *
 * @author xBlackCat
 */

public interface ITagInfo {
    int start();

    ITag getTag();

    /**
     * Processes tag. Returns <code>null</code> if tag has illegal format or incomplete
     *
     * @return
     */
    ITagData process();
}
