package org.xblackcat.sunaj.service.options;

/**
 * Date: 13 квіт 2007
 *
 * @author Alexey
 */

public abstract class AbstractOptionsService implements IOptionsService {
	public <T> T getProperty(Property<T> key) {
		String name = key.getName();
		Class<T> type = key.getType();

		String val = getProperty(name);

		// Holding string parameters
		if (type == String.class) {
			return (T) val;
		}

		// Holding primitive parameters
		if (type == Integer.class) {
			return (T) convertToInt(val);
		}
		if (type == Long.class) {
			return (T) convertToLong(val);
		}
		if (type == Short.class) {
			return (T) convertToShort(val);
		}
		if (type == Byte.class) {
			return (T) convertToByte(val);
		}
		if (type == Character.class) {
			return (T) convertToChar(val);
		}

		if (Enum.class.isAssignableFrom(type)) {
			return (T) convertToEnum((Class<Enum>) type, val);
		}

		throw new UnknownPropertyTyepException("Can not identify the property " + key);
	}

	public <T> T setProperty(Property<T> key, T newValue) {
		String name = key.getName();
		Class<T> type = key.getType();

		// Holding string parameters
		if (type == String.class) {
			return (T) setProperty(name, (String) newValue);
		}

		// Holding primitive parameters
		if (type == Integer.class) {
			String val = setProperty(name, String.valueOf(newValue));
			return (T) convertToInt(val);
		}
		if (type == Long.class) {
			String val = setProperty(name, String.valueOf(newValue));
			return (T) convertToLong(val);
		}
		if (type == Short.class) {
			String val = setProperty(name, String.valueOf(newValue));
			return (T) convertToShort(val);
		}
		if (type == Byte.class) {
			String val = setProperty(name, String.valueOf(newValue));
			return (T) convertToByte(val);
		}
		if (type == Character.class) {
			String val = setProperty(name, String.valueOf(newValue));
			return (T) convertToChar(val);
		}

		if (Enum.class.isAssignableFrom(type)) {
			String val = setProperty(name, ((Enum) newValue).name());
			return (T) convertToEnum((Class<Enum>) type, val);
		}

		throw new UnknownPropertyTyepException("Can not identify the property " + key);
	}

	protected abstract String getProperty(String key);

	protected abstract String setProperty(String key, String value);

	/*
	 * Util methods for converting values.
	 */
	private static Enum convertToEnum(Class<Enum> enumClass, String val) {
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
}
