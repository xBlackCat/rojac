package org.xblackcat.rojac.service.options;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.dialog.options.PropertyUtils;
import org.xblackcat.rojac.service.options.converter.IConverter;
import org.xblackcat.utils.ResourceUtils;

import java.io.IOException;
import java.util.*;

/**
 * @author Alexey
 */

abstract class AnOptionsService implements IOptionsService {
    private static final Log log = LogFactory.getLog(AnOptionsService.class);

    private final Map<Class<?>, IConverter<?>> converters;

    protected AnOptionsService() throws OptionsServiceException {
        // Load all available converters
        HashMap<Class<?>, IConverter<?>> map = new HashMap<>();

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

            IConverter<?> parser;
            try {
                parser = (IConverter<?>) ResourceUtils.loadObjectOrEnum(parserName);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new OptionsServiceException("Can not load parser for " + className + " class instances", e);
            }

            map.put(aClass, parser);
        }

        converters = Collections.unmodifiableMap(map);
    }

    /**
     * Returns current value of a specified property or <code>null</code> if property is not set.
     *
     * @param key property object to identify a property.
     * @return a value of the specified property or <code>null</code> if property is not set.
     */
    @SuppressWarnings({"unchecked"})
    @Override
    public <T> T getProperty(Property<T> key) {
        T cache = key.getCache();
        if (cache != null) {
            return cache;
        }

        String name = key.getName();
        Class<?> type = key.getType();

        String val = getProperty(name);

        // Handle enums in special way
        if (Enum.class.isAssignableFrom(type)) {
            T value = (T) PropertyUtils.toEnum((Class<Enum>) type, val);
            key.setCache(value);
            return value;
        }
        // Handle enum sets in special way
        if (EnumSet.class.isAssignableFrom(type)) {
            T keyDefault = key.getDefault();
            if (keyDefault == null) {
                throw new RuntimeException("Parameter with EnumSet type should have default values!");
            }
            T value = (T) PropertyUtils.toEnumSet((EnumSet) keyDefault, val);
            key.setCache(value);
            return value;
        }
        IConverter<T> conv;

        do {
            conv = (IConverter<T>) converters.get(type);
            if (conv != null) {
                try {
                    T value = conv.convert(val);
                    key.setCache(value);
                    return value;
                } catch (RuntimeException e) {
                    log.error("Can not load property " + name, e);
                    throw e;
                }
            }

            type = type.getSuperclass();
        } while (conv == null);

        throw new UnknownPropertyTypeException("Can not identify the property " + key);
    }

    /**
     * Sets a property to a new value.
     *
     * @param key      property object to identify a property.
     * @param newValue new value to set.
     * @return a previous value of the property.
     */
    @Override
    @SuppressWarnings({"unchecked"})
    public <T> T setProperty(Property<T> key, T newValue) {
        if (key.getChecker() != null && !key.getChecker().isValueCorrect(newValue)) {
            throw new IllegalArgumentException(newValue + " is not valid value for property " + key.getName());
        }

        String name = key.getName();
        Class<?> type = key.getType();

        // Handle enums in special way
        if (Enum.class.isAssignableFrom(type)) {
            String v = newValue == null ? null : ((Enum) newValue).name();
            String val = setProperty(name, v);
            key.setCache(null);
            return (T) PropertyUtils.toEnum((Class<Enum>) type, val);
        }

        // Handle enum sets in special way
        if (EnumSet.class.isAssignableFrom(type)) {
            String v = newValue == null ? null : PropertyUtils.toString((EnumSet<? extends Enum>) newValue);
            String val = setProperty(name, v);
            key.setCache(null);
            T keyDefault = key.getDefault();
            if (keyDefault == null) {
                throw new RuntimeException("Parameter with EnumSet type should have default values!");
            }
            return (T) PropertyUtils.toEnumSet((EnumSet) keyDefault, val);
        }

        IConverter conv;

        do {
            conv = converters.get(type);
            if (conv != null) {
                try {
                    String val = setProperty(name, conv.toString(newValue));
                    key.setCache(null);
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

    /**
     * Implement method for getting property value from a storage.
     *
     * @param key property id.
     * @return property value as string or <code>null</code> if specified property is not set.
     */
    protected abstract String getProperty(String key);

    /**
     * Implement method for storing property value in a storage.
     *
     * @param key   property id.
     * @param value a new value of property.
     * @return previous value of the property.
     */
    protected abstract String setProperty(String key, String value);
}
