package org.xblackcat.sunaj.service.options.converter;

/**
 * Date: 28 лют 2008
 *
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
