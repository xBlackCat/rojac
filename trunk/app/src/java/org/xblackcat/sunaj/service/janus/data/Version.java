package org.xblackcat.sunaj.service.janus.data;

import org.apache.commons.lang.ArrayUtils;

/**
 * Date: 15.04.2007
 *
 * @author ASUS
 */

public final class Version {
    private final byte[] base64Version;

    public Version(byte[] base64Version) {
        this.base64Version = base64Version;
    }

    public Version() {
        base64Version = ArrayUtils.EMPTY_BYTE_ARRAY;
    }

    public byte[] getBytes() {
        // Prevent modification of the data.
        return base64Version.clone();
    }

    private String getAsString() {
        return new String(base64Version);
    }

    public String toString() {
        return "Version[id=" + getAsString() + ']';
    }
}
