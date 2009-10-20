package org.xblackcat.rojac.service.options.converter;

/**
 * @author xBlackCat
 */

public class StringConverter implements IConverter<String> {
    public String convert(String s) {
        return s;
    }

    public String toString(String o) {
        return o;
    }
}
