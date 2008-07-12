package org.xblackcat.sunaj.service.options;

import org.apache.commons.lang.StringUtils;

/**
 * Date: 8 лип 2008
 *
 * @author xBlackCat
 */

public class ClassConverter implements IConverter<Class<?>> {
    public Class<?> convert(String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        }
        try {
            return getClass().getClassLoader().loadClass(s);
        } catch (ClassNotFoundException e) {
            throw new PropertyLoadException("Can not load class", e);
        }
    }

    public String toString(Class<?> o) {
        if (o == null) {
            return null;
        }

        return o.getName();
    }
}
