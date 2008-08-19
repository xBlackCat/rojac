package org.xblackcat.sunaj.service.options;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.sunaj.service.options.converter.IConverter;
import org.xblackcat.sunaj.util.SunajUtils;
import org.xblackcat.utils.ResourceUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Date: 13 квіт 2007
 *
 * @author Alexey
 */

abstract class AnOptionsService implements IOptionsService {
    private static final Log log = LogFactory.getLog(AnOptionsService.class);

    private final Map<Class<?>, IConverter<?>> converters;

    protected AnOptionsService() throws OptionsServiceException {
        // Load all available converters
        HashMap<Class<?>, IConverter<?>> map = new HashMap<Class<?>, IConverter<?>>();

        Properties parsers = new Properties();

        try {
            parsers.load(ResourceUtils.getResourceAsStream("/config/value-parsers.config"));
        } catch (IOException e) {
            throw new OptionsServiceException("Can not load parsers for property values", e);
        }

        for (String className : parsers.stringPropertyNames()) {
            String parserName = parsers.getProperty(className);

            if (StringUtils.isBlank(parserName)) {
                throw new OptionsServiceException("There is no parser for " + className + " class instances");
            }

            Class<?> aClass;
            try {
                aClass = AnOptionsService.class.getClassLoader().loadClass(className);
            } catch (ClassNotFoundException e) {
                throw new OptionsServiceException("Can not load object class " + className, e);
            }

            IConverter<?> parser = null;
            try {
                parser = (IConverter<?>) ResourceUtils.loadObjectOrEnum(parserName);
            } catch (ClassNotFoundException e) {
                throw new OptionsServiceException("Can not load parser for " + className + " class instances", e);
            } catch (IllegalAccessException e) {
                throw new OptionsServiceException("Can not load parser for " + className + " class instances", e);
            } catch (InstantiationException e) {
                throw new OptionsServiceException("Can not load parser for " + className + " class instances", e);
            }

            map.put(aClass, parser);
        }

        converters = Collections.unmodifiableMap(map);
    }

    public <T> T getProperty(Property<T> key) {
        String name = key.getName();
        Class<?> type = key.getType();

        String val = getProperty(name);

        // Handle enums in special way
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

        // Handle enums in special way
        if (Enum.class.isAssignableFrom(type)) {
            String v = newValue == null ? null : ((Enum) newValue).name();
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
