package org.xblackcat.sunaj.service.options;

/**
 * Date: 28 лют 2008
 *
 * @author xBlackCat
 */

class LongConverter extends AScalarConverter<Long> {
    public Long convert(String s) {
        try {
            if (s != null) {
                return Long.decode(s);
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }
}