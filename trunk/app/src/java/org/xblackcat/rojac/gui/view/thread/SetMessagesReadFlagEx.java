package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.gui.dialog.extendmark.DateDirection;
import org.xblackcat.rojac.gui.dialog.extendmark.NewState;
import org.xblackcat.rojac.gui.dialog.extendmark.Scope;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.SetReadExPacket;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.List;


/**
 * @author xBlackCat
 */
public class SetMessagesReadFlagEx extends RojacWorker<Void, SetReadExPacket> {
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
        final IMessageAH mAH = ServiceFactory.getInstance().getStorage().getMessageAH();
        SetReadExPacket result = null;

        switch (dateDirection) {
            case After:
                switch (scope) {
                    case All:
                        result = mAH.setReadAfterDate(dateline, read);
                        break;
                    case Forum:
                        result = mAH.setForumReadAfterDate(dateline, read, forumId);
                        break;
                    case Thread:
                        result = mAH.setThreadReadAfterDate(dateline, read, forumId, threadId);
                        break;
                }
                break;
            case Before:
                switch (scope) {
                    case All:
                        result = mAH.setReadBeforeDate(dateline, read);
                        break;
                    case Forum:
                        result = mAH.setForumReadBeforeDate(dateline, read, forumId);
                        break;
                    case Thread:
                        result = mAH.setThreadReadBeforeDate(dateline, read, forumId, threadId);
                        break;
                }
                break;
        }

        publish(result);

        return null;
    }

    @Override
    protected void process(List<SetReadExPacket> chunks) {
        for (SetReadExPacket p : chunks) {
            p.dispatch();
        }
    }
}
