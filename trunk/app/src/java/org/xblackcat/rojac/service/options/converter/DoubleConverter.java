package org.xblackcat.rojac.service.options.converter;

/**
 * @author xBlackCat
 */

public class DoubleConverter extends AScalarConverter<Double> {
    public Double convert(String s) {
        try {
            if (s != null) {
                return Double.parseDouble(s);
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }
}