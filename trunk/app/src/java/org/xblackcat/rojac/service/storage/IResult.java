package org.xblackcat.rojac.service.storage;

/**
 * 07.04.12 15:52
 *
 * @author xBlackCat
 */
public interface IResult<T> extends Iterable<T> {
    <T> T[] toArray();
}
