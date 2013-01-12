package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.service.datahandler.IgnoreUserUpdatedPacket;
import org.xblackcat.rojac.service.storage.IMiscAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.List;

/**
* 03.05.12 16:49
*
* @author xBlackCat
*/
public class UserIgnoreFlagSetter extends RojacWorker<Void, Void> {
    private final boolean ignored;
    private final int userId;

    public UserIgnoreFlagSetter(boolean ignored, int userId) {
        this.ignored = ignored;
        this.userId = userId;
    }

    @Override
    protected Void perform() throws Exception {
        IMiscAH miscAH = Storage.get(IMiscAH.class);

        if (ignored) {
            miscAH.removeFromIgnoredUserList(userId);
        } else {
            miscAH.addToIgnoredUserList(userId);
        }

        publish();

        return null;
    }

    @Override
    protected void process(List<Void> chunks) {
        new IgnoreUserUpdatedPacket(userId, !ignored).dispatch();
    }
}
