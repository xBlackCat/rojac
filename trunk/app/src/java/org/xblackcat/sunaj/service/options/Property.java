package org.xblackcat.sunaj.service.options;

/**
 * Class for holding application properties names.
 * <p/>
 * Date: 13 квіт 2007
 *
 * @author Alexey
 */

public final class Property<T> {
	/*
	 * Janus synchronizator properties
	 */

	/**
	 * This boolean property indicated is should be used GZip compression while retrieving information from Janus WS.
	 */
	public static final Property<Boolean> WEB_SERVICE_USE_GZIP = new Property("sunaj.service.janusws.use_gzip", Boolean.class);

	private final String name;
	private final Class<T> type;

	Property(String name, Class<T> type) {
		if (name == null) throw new NullPointerException("Property name can not be null.");
		if (type == null) throw new NullPointerException("Class type can not be null."); 

		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public Class<T> getType() {
		return type;
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Property property = (Property) o;

		if (!type.equals(property.type)) return false;
		if (!name.equals(property.name)) return false;

		return true;
	}

	public int hashCode() {
		return 31 * name.hashCode() + type.hashCode();
	}

	public String toString() {
		return "Property[" + name + '(' + type.getName() + ")]"; 
	}
}
