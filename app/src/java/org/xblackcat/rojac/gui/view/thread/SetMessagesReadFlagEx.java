package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.gui.dialog.extendmark.DateDirection;
import org.xblackcat.rojac.gui.dialog.extendmark.NewState;
import org.xblackcat.rojac.gui.dialog.extendmark.Scope;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.util.RojacWorker;


/**
 * @author xBlackCat
 */
public class SetMessagesReadFlagEx extends RojacWorker<Void, Void> {
    private final long dateline;
    private final int forumId;
    private final int threadId;
    private final boolean read;
    private final Scope scope;
    private final DateDirection dateDirection;

    public SetMessagesReadFlagEx(NewState read, DateDirection dateDirection, long dateline, int forumId, int threadId, Scope scope) {
        this.dateDirection = dateDirection;
        this.read = read == NewState.Read;
        this.dateline = dateline;
        this.forumId = forumId;
        this.threadId = threadId;
        this.scope = scope;
    }

    @Override
    protected Void perform() throws Exception {
        IStorage storage = ServiceFactory.getInstance().getStorage();
        switch (scope) {
            case All:
                break;
            case Forum:
//                storage.getMessageAH().updateMessagesReadFlagEx(dateline, read, forumId);
                break;
            case Thread:
//                storage.getMessageAH().updateMessagesReadFlagEx(dateline, read, forumId, threadId);
                break;
        }
        return null;
    }

    @Override
    protected void done() {
    }

}
