package org.xblackcat.rojac.gui.frame.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.gui.frame.message.AMessageView;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.StorageException;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;

/**
 * Date: 20 ρεπο 2008
 *
 * @author xBlackCat
 */

public abstract class TreeThreadView extends AMessageView {
    private static final Log log = LogFactory.getLog(TreeThreadView.class);
    
    protected final JTree messages = new JTree();
    private final JLabel forumName = new JLabel();
    protected final AThreadTreeModel model;

    public TreeThreadView() {
        super(new BorderLayout());

        model = createModel();
        initializeLayout();
    }

    protected abstract AThreadTreeModel createModel();

    protected void initializeLayout() {
        add(forumName, BorderLayout.NORTH);

        // Initialize tree
        messages.setEditable(false);
        messages.setModel(model);
        messages.setCellRenderer(new MessageTreeCellRenderer());
        messages.setShowsRootHandles(true);
        messages.setRootVisible(true);

        messages.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                MessageItem mi = (MessageItem) e.getPath().getLastPathComponent();
                fireMessageGotFocus(mi.getMessageId());
            }
        });

        JScrollPane sp = new JScrollPane(messages);
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
}
