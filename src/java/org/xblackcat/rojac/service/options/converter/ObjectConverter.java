package org.xblackcat.rojac.service.options.converter;

import org.apache.commons.lang.StringUtils;
import org.xblackcat.rojac.service.options.PropertyLoadException;
import org.xblackcat.utils.ResourceUtils;

/**
 * @author xBlackCat
 */

public class ObjectConverter implements IConverter<Object> {
    public Object convert(String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        }
        try {
            return ResourceUtils.loadObjectOrEnum(s);
        } catch (Exception e) {
            throw new PropertyLoadException("Can not initialize object.", e);
        }
    }

    public String toString(Object o) {
        if (o == null) {
            return null;
        }

        return o.getClass().getName();
    }
}
