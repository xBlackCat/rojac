package org.xblackcat.sunaj.service.options;

/**
 * Date: 28 лют 2008
 *
 * @author xBlackCat
 */

class ByteConverter extends AScalarConverter<Byte> {
    public Byte convert(String s) {
        try {
            if (s != null) {
                return Byte.decode(s);
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }
}