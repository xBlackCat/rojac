package org.xblackcat.rojac.service.options.converter;

import org.apache.commons.lang.BooleanUtils;

/**
 * @author xBlackCat
 */

public class BooleanConverter extends AScalarConverter<Boolean> {
    public Boolean convert(String s) {
        return BooleanUtils.toBooleanObject(s);
    }
}
