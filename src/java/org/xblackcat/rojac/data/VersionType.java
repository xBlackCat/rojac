package org.xblackcat.rojac.data;

/**
 * @author ASUS
 */

public enum VersionType {
    MESSAGE_ROW_VERSION(0),
    MODERATE_ROW_VERSION(1),
    RATING_ROW_VERSION(2),
    USERS_ROW_VERSION(3),
    FORUM_ROW_VERSION(4),;

    private final int id;

    VersionType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static VersionType getType(int id) {
        for (VersionType t : values()) {
            if (t.id == id) return t;
        }

        throw new IllegalArgumentException("Unknown version type: " + id);
    }
}
