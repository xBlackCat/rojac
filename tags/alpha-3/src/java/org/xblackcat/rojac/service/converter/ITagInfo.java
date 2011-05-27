package org.xblackcat.rojac.service.converter;

/**
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
