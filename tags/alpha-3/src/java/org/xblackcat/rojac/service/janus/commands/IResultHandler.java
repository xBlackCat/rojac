package org.xblackcat.rojac.service.janus.commands;

/**
 * @author xBlackCat
 */

public interface IResultHandler<T> {
    void process(T data);
}
