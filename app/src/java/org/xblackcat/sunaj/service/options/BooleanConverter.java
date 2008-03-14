package org.xblackcat.sunaj.service.options;

import org.apache.commons.lang.BooleanUtils;

/**
 * Date: 28 лют 2008
 *
 * @author xBlackCat
 */

class BooleanConverter extends AScalarConverter<Boolean> {
    public Boolean convert(String s) {
        return BooleanUtils.toBooleanObject(s);
    }
}
