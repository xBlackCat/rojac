package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.gui.dialog.db.CheckProcessDialog;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.datahandler.ReloadDataPacket;
import org.xblackcat.rojac.service.progress.ProgressChangeEvent;
import org.xblackcat.rojac.service.progress.ProgressState;

import java.awt.*;

/**
 * 10.04.12 17:24
 *
 * @author xBlackCat
 */
public class DatabaseCleaner extends DatabaseWorker {
    private final long period;

    public DatabaseCleaner(Window owner, long period) {
        super(
                null,
                owner,
                new CheckProcessDialog(
                        owner,
                        Message.Dialog_ImportProgress_Title,
                        Message.Dialog_ImportProgress_Label,
                        true
                )
        );
        this.period = period;
    }

    @Override
    protected boolean doWork() throws StorageException {
        publish(new ProgressChangeEvent(this, ProgressState.Work, 0));

        IUtilAH utilAH = Storage.get(IUtilAH.class);

        utilAH.updateLastPostId();
        utilAH.updateLastPostId();

        int total = utilAH.getTopicsAmountToClean(period, false, false);

        int current = 0;
        try (IResult<Integer> idsToClean = utilAH.getTopicIdsToClean(period, false, false)) {
            for (Integer topicId : idsToClean) {
                publish(new ProgressChangeEvent(this, ProgressState.Work, current++, total));
                utilAH.removeTopic(topicId);
            }
        }

        utilAH.updateForumsStat();

        return true;
    }

    @Override
    protected void onSuccess() {
        new ReloadDataPacket().dispatch();
    }
}
