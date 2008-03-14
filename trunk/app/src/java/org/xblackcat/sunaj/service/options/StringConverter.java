package org.xblackcat.sunaj.service.options;

/**
 * Date: 28 лют 2008
 *
 * @author xBlackCat
 */

class StringConverter implements IConverter<String > {
    public String convert(String s) {
        return s;
    }

    public String toString(String o) {
        return o;
    }
}
