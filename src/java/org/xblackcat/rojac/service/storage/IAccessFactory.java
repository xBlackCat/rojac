package org.xblackcat.rojac.service.storage;

/**
 * 14.08.12 16:04
 *
 * @author xBlackCat
 */
public interface IAccessFactory {
    <T> T get(Class<T> base);
}
