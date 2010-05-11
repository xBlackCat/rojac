package org.xblackcat.rojac.gui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IMiscAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.RojacWorker;
import org.xblackcat.rojac.util.SynchronizationUtils;

import java.awt.*;

/**
* @author xBlackCat
*/
class ExtraMessageLoader extends RojacWorker<Void, Void> {
    private static final Log log = LogFactory.getLog(ExtraMessageLoader.class);

    private final int messageId;
    private final boolean loadAtOnce;
    private final Window frame;

    public ExtraMessageLoader(int messageId, boolean loadAtOnce, Window frame) {
        this.messageId = messageId;
        this.loadAtOnce = loadAtOnce;
        this.frame = frame;
    }

    @Override
    protected Void perform() throws Exception {
        try {
            IMiscAH s = ServiceFactory.getInstance().getStorage().getMiscAH();

            s.storeExtraMessage(messageId);
        } catch (StorageException e) {
            log.error("Can not store extra message #" + messageId, e);
            RojacUtils.showExceptionDialog(e);
        }

        return null;
    }

    @Override
    protected void done() {
        if (loadAtOnce) {
            SynchronizationUtils.loadExtraMessages(frame);
        }
    }
}
