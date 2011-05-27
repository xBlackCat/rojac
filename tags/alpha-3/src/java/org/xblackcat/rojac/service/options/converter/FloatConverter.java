package org.xblackcat.rojac.service.options.converter;

/**
 * @author xBlackCat
 */

public class FloatConverter extends AScalarConverter<Float> {
    public Float convert(String s) {
        try {
            if (s != null) {
                return Float.parseFloat(s);
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }
}