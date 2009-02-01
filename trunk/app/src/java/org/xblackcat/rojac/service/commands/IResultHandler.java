package org.xblackcat.rojac.service.commands;

import org.xblackcat.rojac.RojacException;

/**
 * Date: 5 זמגע 2008
 *
 * @author xBlackCat
 */

public interface IResultHandler<T> {
    void process(T results) throws RojacException;
}
