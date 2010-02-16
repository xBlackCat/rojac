package org.xblackcat.rojac.service.options.converter;

/**
 * @author xBlackCat
 */

public class LongConverter extends AScalarConverter<Long> {
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