package org.xblackcat.rojac.service.synchronizer;

/**
 * Date: 5 זמגע 2008
 *
 * @author xBlackCat
 */

public interface IResultHandler<T> {
    void process(T results) throws Exception;
}
