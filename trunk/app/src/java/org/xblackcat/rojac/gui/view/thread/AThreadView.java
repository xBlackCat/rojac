package org.xblackcat.rojac.gui.view.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.view.message.AMessageView;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.StorageException;

import javax.swing.*;
import java.awt.*;

/**
 * @author xBlackCat
 */

public abstract class AThreadView extends AMessageView {
    private static final Log log = LogFactory.getLog(TreeThreadView.class);
    
    protected final IThreadControl threadControl;
    protected final JLabel forumName = new JLabel();
    protected final ThreadsModel model = new ThreadsModel();

    protected AThreadView(IRootPane mainFrame, IThreadControl threadControl) {
        super(mainFrame);
        this.threadControl = threadControl;
    }

    protected void initializeLayout() {
        // Initialize tree
        add(forumName, BorderLayout.NORTH);

        JComponent threadsContainer = getThreadsContainer();
        
        JScrollPane sp = new JScrollPane(threadsContainer);
        add(sp, BorderLayout.CENTER);
    }

    protected void loadForumInfo(int forumId) {
        IForumAH fah = ServiceFactory.getInstance().getStorage().getForumAH();

        Forum f;
        try {
            f = fah.getForumById(forumId);
        } catch (StorageException e) {
            log.error("Can not load forum information for forum id = " + forumId, e);
            return;
        }

        forumName.setText(f.getForumName() + "/" + f.getShortForumName());
    }

    protected abstract void selectFirstItem();

    public void loadItem(int itemId) {
        int forumId = threadControl.loadThreadByItem(itemId, model);
        loadForumInfo(forumId);

        selectFirstItem();
    }

    @Override
    public void updateData(int... messageId) {
        threadControl.updateItem(model);
    }

    protected abstract JComponent getThreadsContainer();
}
