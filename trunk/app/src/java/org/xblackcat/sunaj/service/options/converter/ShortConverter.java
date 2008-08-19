package org.xblackcat.sunaj.service.options.converter;

/**
 * Date: 28 лют 2008
 *
 * @author xBlackCat
 */

public class ShortConverter extends AScalarConverter<Short> {
    public Short convert(String s) {
        try {
            if (s != null) {
                return Short.decode(s);
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }
}