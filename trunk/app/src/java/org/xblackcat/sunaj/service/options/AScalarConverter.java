package org.xblackcat.sunaj.service.options;

/**
 * Date: 28 лют 2008
 *
 * @author xBlackCat
 */

abstract class AScalarConverter<T> implements IConverter<T> {
    public String toString(T o) {
        return o == null ? null : String.valueOf(o);
    }
}
