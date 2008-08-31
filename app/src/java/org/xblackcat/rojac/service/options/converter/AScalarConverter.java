package org.xblackcat.rojac.service.options.converter;

/**
 * Date: 28 лют 2008
 *
 * @author xBlackCat
 */

public abstract class AScalarConverter<T> implements IConverter<T> {
    public String toString(T o) {
        return o == null ? null : String.valueOf(o);
    }
}
