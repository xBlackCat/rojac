package org.xblackcat.rojac.service.options.converter;

import org.apache.commons.lang3.BooleanUtils;

/**
 * @author xBlackCat
 */

public class BooleanConverter implements IConverter<Boolean> {
    public Boolean convert(String s) {
        return BooleanUtils.toBooleanObject(s);
    }

    @Override
    public String toString(Boolean o) {
        return BooleanUtils.toStringYesNo(o);
    }
}
