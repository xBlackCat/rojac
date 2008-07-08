package org.xblackcat.sunaj.service.options;

import org.apache.commons.lang.StringUtils;
import org.xblackcat.utils.ResourceUtils;

/**
 * Date: 8 лип 2008
 *
 * @author xBlackCat
 */

public class ObjectConverter implements IConverter<Object> {
    public Object convert(String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        }
        try {
            return ResourceUtils.loadClassOrEnum(s);
        } catch (Exception e) {
            throw new PropertyLoadException("Can not initialize object.", e);
        }
    }

    public String toString(Object o) {
        return o.getClass().getName();
    }
}
