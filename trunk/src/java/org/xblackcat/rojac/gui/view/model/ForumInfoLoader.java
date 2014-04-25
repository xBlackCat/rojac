package org.xblackcat.rojac.gui.view.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumMessageData;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.sjpu.storage.StorageException;

import javax.swing.*;

/**
 * @author xBlackCat
 */
class ForumInfoLoader extends ThreadsLoader {
    private static final Log log = LogFactory.getLog(ForumInfoLoader.class);

    private final int forumId;
    private Forum forum;

    public ForumInfoLoader(SortedThreadsModel model, int forumId) {
        super(null, model, forumId);
        this.forumId = forumId;
    }

    @Override
    protected Void perform() throws Exception {
        IForumAH fah = Storage.get(IForumAH.class);

        boolean failed = true;
        try {
            forum = fah.getForumById(forumId);

            failed = false;
        } catch (StorageException e) {
            log.error("Can not load forum information for forum id = " + forumId, e);
        }

        if (failed || forum == null) {
            SwingUtilities.invokeLater(() -> model.setRoot(null));
            return null;
        }

        SwingUtilities.invokeLater(
                () -> {
                    MessageData fd = new ForumMessageData(forum);
                    model.getRoot().setMessageData(fd);
                    model.nodeChanged(model.getRoot());
                }
        );

        // Load threads
        super.perform();

        return null;
    }
}
