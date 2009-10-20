package org.xblackcat.rojac.service.options.converter;

/**
 * @author xBlackCat
 */

public class IntegerConverter extends AScalarConverter<Integer> {
    public Integer convert(String s) {
        try {
            if (s != null) {
                return Integer.decode(s);
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }
}