package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.gui.dialog.db.CheckProcessDialog;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.datahandler.ReloadDataPacket;
import org.xblackcat.rojac.service.progress.ProgressChangeEvent;
import org.xblackcat.rojac.service.progress.ProgressState;

import java.awt.*;

/**
 * 15.09.11 16:43
 *
 * @author xBlackCat
 */
public class DatabaseCacheUpdater extends DatabaseWorker {
    public DatabaseCacheUpdater(Window owner) {
        super(null, owner, new CheckProcessDialog(owner, Message.Dialog_CheckProgress_CacheUpdate_Title, Message.Dialog_CheckProgress_CacheUpdate_Label));
    }

    @Override
    protected boolean doWork() throws StorageException {
        IUtilAH uah = Storage.get(IUtilAH.class);

        // Replace storage engine before updating data in views.

        publish(new ProgressChangeEvent(this, ProgressState.Work, 0, 5));
        uah.updateParentUserId();
        publish(new ProgressChangeEvent(this, ProgressState.Work, 1, 5));
        uah.updateForumsStat();
        publish(new ProgressChangeEvent(this, ProgressState.Work, 2, 5));
        uah.updateThreadsStat();
        publish(new ProgressChangeEvent(this, ProgressState.Work, 3, 5));
        uah.updateLastPostId();
        publish(new ProgressChangeEvent(this, ProgressState.Work, 4, 5));
        uah.updateLastPostDate();
        publish(new ProgressChangeEvent(this, ProgressState.Work, 5, 5));

        return true;
    }

    @Override
    protected void onSuccess() {
        new ReloadDataPacket().dispatch();
    }
}
