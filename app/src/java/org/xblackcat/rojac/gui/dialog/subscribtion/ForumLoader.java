package org.xblackcat.rojac.gui.dialog.subscribtion;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacWorker;

/**
 * @author xBlackCat
 */
class ForumLoader extends RojacWorker<Void, Forum> {
    private static final Log log = LogFactory.getLog(ForumLoader.class);
    private static final IStorage storage = ServiceFactory.getInstance().getStorage();

    private final SubscribeForumModel model;

    public ForumLoader(SubscribeForumModel model) {
        this.model = model;
        model.clear();
    }

    @Override
    protected Void perform() throws Exception {
        try {
            for (Forum f : storage.getForumAH().getAllForums()) {
                publish(f);
            }
        } catch (StorageException e) {
            log.error("Can not load forum list", e);
            throw e;
        }

        return null;
    }

    @Override
    protected void process(java.util.List<Forum> forums) {
        model.addForums(forums);
    }

    @Override
    protected void done() {
        model.fireTableDataChanged();
    }
}
