package org.xblackcat.rojac.service.commands;

import org.xblackcat.rojac.RojacException;

/**
 * @author xBlackCat
 */

public interface IResultHandler<T> {
    void process(T results) throws RojacException;
}
