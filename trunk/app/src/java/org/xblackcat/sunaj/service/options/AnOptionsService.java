package org.xblackcat.sunaj.service.options;

import org.xblackcat.sunaj.util.DataUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 13 квіт 2007
 *
 * @author Alexey
 */

abstract class AnOptionsService implements IOptionsService {
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
        
        converters = Collections.unmodifiableMap(map);
    }

    public <T> T getProperty(Property<T> key) {
        String name = key.getName();
        Class<T> type = key.getType();

        String val = getProperty(name);

        IConverter<T> conv = (IConverter<T>) converters.get(type);

        if (conv != null) {
            return conv.convert(val);
        } else {
            // Check if the type of property is enum
            if (Enum.class.isAssignableFrom(type)) {
                return (T) DataUtils.convertToEnum((Class<Enum>) type, val);
            }
        }

        throw new UnknownPropertyTypeException("Can not identify the property " + key);
    }

    public <T> T setProperty(Property<T> key, T newValue) {
        String name = key.getName();
        Class<T> type = key.getType();

        IConverter<T> conv = (IConverter<T>) converters.get(type);

        if (conv != null) {
            String val = setProperty(name, conv.toString(newValue));
            return conv.convert(val);
        } else {
            // Check if the type of property is enum
            if (Enum.class.isAssignableFrom(type)) {
                String val = setProperty(name, ((Enum) newValue).name());
                return (T) DataUtils.convertToEnum((Class<Enum>) type, val);
            }
        }

        throw new UnknownPropertyTypeException("Can not identify the property " + key);
    }

    protected abstract String getProperty(String key);

    protected abstract String setProperty(String key, String value);
}
