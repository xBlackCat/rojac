package org.xblackcat.rojac.data;

/**
 * @author ASUS
 */

public class VersionInfo {
    private final Version version;
    private final VersionType type;

    public VersionInfo(VersionType type, Version version) {
        this.version = version;
        this.type = type;
    }

    public Version getVersion() {
        return version;
    }

    public VersionType getType() {
        return type;
    }
}
