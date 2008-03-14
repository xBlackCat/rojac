package org.xblackcat.sunaj.service.options;

/**
 * Date: 28 лют 2008
 *
 * @author xBlackCat
 */

class ShortConverter extends AScalarConverter<Short> {
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