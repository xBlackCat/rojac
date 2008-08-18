package org.xblackcat.sunaj.gui.frame.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.sunaj.data.Forum;
import org.xblackcat.sunaj.gui.frame.message.AMessageView;
import org.xblackcat.sunaj.service.ServiceFactory;
import org.xblackcat.sunaj.service.storage.IForumAH;
import org.xblackcat.sunaj.service.storage.StorageException;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;

/**
 * Date: 22 бер 2008
 *
 * @author xBlackCat
 */

public class TreeThreadView extends AMessageView {
    private static final Log log = LogFactory.getLog(TreeThreadView.class);

    private final JTree messages = new JTree();
    private final JLabel forumName = new JLabel();

    private final AThreadTreeModel model;

    public TreeThreadView() {
        super(new BorderLayout());

        model = new SingleThreadTreeModel();
        initializeLayout();
    }

    private void initializeLayout() {
        add(forumName, BorderLayout.NORTH);

        // Initialize tree
        messages.setEditable(false);
        messages.setModel(model);
        messages.setCellRenderer(new MessageTreeCellRenderer());

        messages.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                MessageItem mi = (MessageItem) e.getPath().getLastPathComponent();
                fireMessageGotFocus(mi.getMessageId());
            }
        });

        JScrollPane sp = new JScrollPane(messages);
        add(sp, BorderLayout.CENTER);
    }

    public void viewItem(int rootMessageId) {
        model.showItem(rootMessageId);
        MessageItem mi = (MessageItem) model.getRoot();
        loadForumInfo(mi.getMessage().getForumId());

        messages.setSelectionRow(0);
    }

    public void updateItem(int messageId) {
    }

    private void loadForumInfo(int forumId) {
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
