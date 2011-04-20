package org.xblackcat.rojac.gui.view.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumMessageData;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.List;

/**
* @author xBlackCat
*/
class ForumInfoLoader extends RojacWorker<Void, Forum> {
    private static final Log log = LogFactory.getLog(ForumInfoLoader.class);

    private final int forumId;
    private AThreadModel<Post> model;

    public ForumInfoLoader(AThreadModel<Post> model, int forumId) {
        this.forumId = forumId;
        this.model = model;
    }

    @Override
    protected Void perform() throws Exception {
        IForumAH fah = ServiceFactory.getInstance().getStorage().getForumAH();

        try {
            Forum forum = fah.getForumById(forumId);
            if (forum != null) {
                publish(forum);
            }
        } catch (StorageException e) {
            log.error("Can not load forum information for forum id = " + forumId, e);
        }

        return null;
    }

    @Override
    protected void process(List<Forum> chunks) {
        for (Forum f : chunks) {
            MessageData fd = new ForumMessageData(f);
            model.getRoot().setMessageData(fd);
            model.nodeChanged(model.getRoot());
        }
    }
}
