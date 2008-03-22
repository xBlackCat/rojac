package org.xblackcat.sunaj.data;

/**
 * Date: 26 квіт 2007
 *
 * @author ASUS
 */

public class VersionInfo {
    private final Version version;
    private final VersionType type;

    public VersionInfo(Version version, VersionType type) {
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
