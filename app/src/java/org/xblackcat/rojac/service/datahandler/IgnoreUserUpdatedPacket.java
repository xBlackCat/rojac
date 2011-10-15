package org.xblackcat.rojac.service.datahandler;

/**
 * @author xBlackCat
 */
public class IgnoreUserUpdatedPacket extends APacket {
    private final int userId;
    private final boolean ignored;

    public IgnoreUserUpdatedPacket(int userId, boolean ignored) {
        this.userId = userId;
        this.ignored = ignored;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isIgnored() {
        return ignored;
    }
}
