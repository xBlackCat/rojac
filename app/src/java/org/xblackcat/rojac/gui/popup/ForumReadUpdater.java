package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.SetForumReadPacket;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.IStorage;
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
        IStorage storage = ServiceFactory.getInstance().getStorage();
        IForumAH fah = storage.getForumAH();
        fah.setForumRead(forumId, readFlag);
        return null;
    }

    @Override
    protected void done() {
        new SetForumReadPacket(readFlag, forumId).dispatch();
    }
}
