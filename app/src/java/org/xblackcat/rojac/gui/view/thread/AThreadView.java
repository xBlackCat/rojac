package org.xblackcat.rojac.gui.view.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.view.message.AItemView;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.janus.commands.AffectedIds;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author xBlackCat
 */

public abstract class AThreadView extends AItemView {
    private static final Log log = LogFactory.getLog(TreeThreadView.class);
    
    protected final IThreadControl threadControl;
    protected final JLabel forumName = new JLabel();
    protected final AThreadModel model = new SortedThreadsModel();
    protected int forumId;

    protected AThreadView(IRootPane mainFrame, IThreadControl threadControl) {
        super(mainFrame);
        this.threadControl = threadControl;
    }

    protected void initializeLayout() {
        // Initialize tree
        add(forumName, BorderLayout.NORTH);

        JPanel internalPane = new JPanel(new BorderLayout(0, 0));

        JComponent threadsContainer = getThreadsContainer();
        JScrollPane sp = new JScrollPane(threadsContainer);
        internalPane.add(sp, BorderLayout.CENTER);

        JButton newThreadButton = WindowsUtils.setupImageButton("new_thread", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.editMessage(forumId, null);
            }
        }, Messages.VIEW_THREAD_BUTTON_NEW_THREAD);
        JToolBar toolbar = WindowsUtils.createToolBar(newThreadButton);
        internalPane.add(toolbar, BorderLayout.NORTH);

        add(internalPane, BorderLayout.CENTER);
    }

    protected void loadForumInfo(int forumId) {
        this.forumId = forumId;
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
        int forumId = threadControl.loadThreadByItem(model, itemId);
        loadForumInfo(forumId);
    }

    @Override
    public void updateData(AffectedIds ids) {
        if (ids.containsForum(forumId)) {
            threadControl.updateItem(model, ids.getMessageIds());
        }
    }

    protected abstract JComponent getThreadsContainer();
}
