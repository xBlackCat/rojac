package org.xblackcat.sunaj.service.options;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.sunaj.util.SunajUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 13 квіт 2007
 *
 * @author Alexey
 */

abstract class AnOptionsService implements IOptionsService {
    private static final Log log = LogFactory.getLog(AnOptionsService.class);

    private final Map<Class<?>, IConverter<?>> converters;

    public AnOptionsService() {
        HashMap<Class<?>, IConverter<?>> map = new HashMap<Class<?>, IConverter<?>>();

        map.put(Boolean.class, new BooleanConverter());
        map.put(Byte.class, new ByteConverter());
        map.put(Character.class, new CharacterConverter());
        map.put(Double.class, new DoubleConverter());
        map.put(Float.class, new FloatConverter());
        map.put(Integer.class, new IntegerConverter());
        map.put(Long.class, new LongConverter());
        map.put(Short.class, new ShortConverter());
        map.put(String.class, new StringConverter());
        map.put(Class.class, new ClassConverter());
        map.put(Password.class, new PasswordConverter());

        // Global converter
        map.put(Object.class, new ObjectConverter());

        converters = Collections.unmodifiableMap(map);
    }

    public <T> T getProperty(Property<T> key) {
        String name = key.getName();
        Class<?> type = key.getType();

        String val = getProperty(name);

        if (Enum.class.isAssignableFrom(type)) {
            return (T) SunajUtils.convertToEnum((Class<Enum>) type, val);
        }
        IConverter<?> conv;

        do {
            conv = converters.get(type);
            if (conv != null) {
                try {
                    return (T) conv.convert(val);
                } catch (RuntimeException e) {
                    log.error("Can not load property " + name, e);
                    throw e;
                }
            }

            type = type.getSuperclass();
        } while (conv == null);

        throw new UnknownPropertyTypeException("Can not identify the property " + key);
    }

    public <T> T setProperty(Property<T> key, T newValue) {
        String name = key.getName();
        Class<?> type = key.getType();

        if (Enum.class.isAssignableFrom(type)) {
            String v = newValue == null ?  null : ((Enum) newValue).name();
            String val = setProperty(name, v);
            return (T) SunajUtils.convertToEnum((Class<Enum>) type, val);
        }
        IConverter conv;

        do {
            conv = converters.get(type);
            if (conv != null) {
                try {
                    String val = setProperty(name, conv.toString(newValue));
                    return (T) conv.convert(val);
                } catch (RuntimeException e) {
                    log.error("Can not load property " + name, e);
                    throw e;
                }
            }

            type = type.getSuperclass();
        } while (conv == null);

        throw new UnknownPropertyTypeException("Can not identify the property " + key);
    }

    protected abstract String getProperty(String key);

    protected abstract String setProperty(String key, String value);
}
