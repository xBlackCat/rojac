package org.xblackcat.sunaj.service.options.converter;

/**
 * Date: 28 лют 2008
 *
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