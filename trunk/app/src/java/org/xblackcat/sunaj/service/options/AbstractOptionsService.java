package org.xblackcat.sunaj.service.options;

/**
 * Date: 13 квіт 2007
 *
 * @author Alexey
 */

abstract class AbstractOptionsService implements IOptionsService {
    public <T> T getProperty(Property<T> key) {
        String name = key.getName();
        Class<T> type = key.getType();

        String val = getProperty(name);

        // Holding string parameters
        if (type.equals(String.class)) {
            return (T) val;
        }

        // Holding primitive parameters
        if (type.equals(Boolean.class)) {
            return (T) convertToBoolean(val);
        }
        if (type.equals(Integer.class)) {
            return (T) convertToInt(val);
        }
        if (type.equals(Long.class)) {
            return (T) convertToLong(val);
        }
        if (type.equals(Short.class)) {
            return (T) convertToShort(val);
        }
        if (type.equals(Byte.class)) {
            return (T) convertToByte(val);
        }
        if (type.equals(Character.class)) {
            return (T) convertToChar(val);
        }
        if (type.equals(Float.class)) {
            return (T) convertToFloat(val);
        }
        if (type.equals(Double.class)) {
            return (T) convertToDouble(val);
        }

        // Holding Enums
        if (Enum.class.isAssignableFrom(type)) {
            return (T) convertToEnum((Class<Enum>) type, val);
        }

        throw new UnknownPropertyTypeException("Can not identify the property " + key);
    }

    public <T> T setProperty(Property<T> key, T newValue) {
        String name = key.getName();
        Class<T> type = key.getType();

        // Holding string parameters
        if (type.equals(String.class)) {
            return (T) setProperty(name, (String) newValue);
        }

        // Holding primitive parameters
        if (type.equals(Boolean.class)) {
            String val = setProperty(name, String.valueOf(newValue));
            return (T) convertToBoolean(val);
        }
        if (type.equals(Integer.class)) {
            String val = setProperty(name, String.valueOf(newValue));
            return (T) convertToInt(val);
        }
        if (type.equals(Long.class)) {
            String val = setProperty(name, String.valueOf(newValue));
            return (T) convertToLong(val);
        }
        if (type.equals(Short.class)) {
            String val = setProperty(name, String.valueOf(newValue));
            return (T) convertToShort(val);
        }
        if (type.equals(Byte.class)) {
            String val = setProperty(name, String.valueOf(newValue));
            return (T) convertToByte(val);
        }
        if (type.equals(Character.class)) {
            String val = setProperty(name, String.valueOf(newValue));
            return (T) convertToChar(val);
        }
        if (type.equals(Float.class)) {
            String val = setProperty(name, String.valueOf(newValue));
            return (T) convertToFloat(val);
        }
        if (type.equals(Double.class)) {
            String val = setProperty(name, String.valueOf(newValue));
            return (T) convertToDouble(val);
        }

        // Holding Enums
        if (Enum.class.isAssignableFrom(type)) {
            String val = setProperty(name, ((Enum) newValue).name());
            return (T) convertToEnum((Class<Enum>) type, val);
        }

        throw new UnknownPropertyTypeException("Can not identify the property " + key);
    }

    protected abstract String getProperty(String key);

    protected abstract String setProperty(String key, String value);

    /*
      * Util methods for converting values.
      */
    private static <T extends Enum<T>> T convertToEnum(Class<T> enumClass, String val) {
        if (val != null) {
            return Enum.valueOf(enumClass, val);
        } else {
            return null;
        }
    }

    private static Long convertToLong(String s) {
        try {
            if (s != null) {
                return Long.decode(s);
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            // Zero is the default value for long argument.
            return 0l;
        }
    }

    private static Boolean convertToBoolean(String s) {
        if (s != null) {
            if ("true".equalsIgnoreCase(s) ||
                    "yes".equalsIgnoreCase(s) ||
                    "on".equalsIgnoreCase(s)) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } else {
            return null;
        }
    }

    private static Integer convertToInt(String s) {
        try {
            if (s != null) {
                return Integer.decode(s);
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            // Zero is the default value for int argument.
            return 0;
        }
    }

    private static Short convertToShort(String s) {
        try {
            if (s != null) {
                return Short.decode(s);
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            // Zero is the default value for short argument.
            return 0;
        }
    }

    private static Byte convertToByte(String s) {
        try {
            if (s != null) {
                return Byte.decode(s);
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            // Zero is the default value for byte argument.
            return 0;
        }
    }

    private static Character convertToChar(String s) {
        try {
            if (s != null && s.length() > 0) {
                return s.charAt(0);
            } else {
                return null;
            }
        } catch (IndexOutOfBoundsException e) {
            // Zero is the default value for char argument.
            return Character.MIN_VALUE;
        }
    }

    private static Double convertToDouble(String s) {
        try {
            if (s != null) {
                return Double.parseDouble(s);
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            // NAN is the default value for double argument.
            return Double.NaN;
        }
    }

    private static Float convertToFloat(String s) {
        try {
            if (s != null) {
                return Float.parseFloat(s);
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            // NAN is the default value for double argument.
            return Float.NaN;
        }
    }
}
