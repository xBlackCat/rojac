package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.service.datahandler.SetForumReadPacket;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.RojacWorker;

/**
 * @author xBlackCat
 */
class ForumReadUpdater extends RojacWorker<Void, Void> {
    private final int forumId;
    private final boolean readFlag;

    public ForumReadUpdater(int forumId, boolean readFlag) {
        this.forumId = forumId;
        this.readFlag = readFlag;
    }

    @Override
    protected Void perform() throws Exception {
        IForumAH fah = Storage.get(IForumAH.class);
        fah.setForumRead(forumId, readFlag);
        return null;
    }

    @Override
    protected void done() {
        new SetForumReadPacket(readFlag, forumId).dispatch();
    }
}
