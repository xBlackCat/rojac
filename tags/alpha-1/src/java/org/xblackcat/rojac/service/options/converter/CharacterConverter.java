package org.xblackcat.rojac.service.options.converter;

/**
 * @author xBlackCat
 */

public class CharacterConverter extends AScalarConverter<Character> {
    public Character convert(String s) {
        try {
            if (s != null && s.length() > 0) {
                return s.charAt(0);
            } else {
                return null;
            }
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}
