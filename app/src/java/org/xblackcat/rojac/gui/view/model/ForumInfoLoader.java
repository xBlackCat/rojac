package org.xblackcat.rojac.gui.view.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumMessageData;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacWorker;

/**
 * @author xBlackCat
 */
class ForumInfoLoader extends RojacWorker<Void, Forum> {
    private static final Log log = LogFactory.getLog(ForumInfoLoader.class);

    private final int forumId;
    private SortedThreadsModel model;
    private Forum forum;

    public ForumInfoLoader(SortedThreadsModel model, int forumId) {
        this.forumId = forumId;
        this.model = model;
    }

    @Override
    protected Void perform() throws Exception {
        IForumAH fah = Storage.get(IForumAH.class);

        try {
            forum = fah.getForumById(forumId);
        } catch (StorageException e) {
            log.error("Can not load forum information for forum id = " + forumId, e);
        }

        return null;
    }

    @Override
    protected void done() {
        if (forum != null) {
            MessageData fd = new ForumMessageData(forum);
            model.getRoot().setMessageData(fd);
            model.nodeChanged(model.getRoot());
        } else {
            model.setRoot(null);
        }
    }
}
