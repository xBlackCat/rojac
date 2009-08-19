package org.xblackcat.rojac.gui.frame.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.gui.frame.message.AMessageView;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.StorageException;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Date: 20 ρεπο 2008
 *
 * @author xBlackCat
 */

public abstract class ATreeThreadView extends AMessageView {
    private static final Log log = LogFactory.getLog(ATreeThreadView.class);

    protected final JTree messages = new JTree();
    private final JLabel forumName = new JLabel();
    protected final AThreadTreeModel model;

    public ATreeThreadView() {
        super(new BorderLayout());

        model = createModel();
        initializeLayout();
    }

    protected abstract AThreadTreeModel createModel();

    protected void initializeLayout() {
        // Initialize tree
        messages.setEditable(false);
        messages.setModel(model);
        messages.setCellRenderer(new MessageTreeCellRenderer());
        add(forumName, BorderLayout.NORTH);

        messages.setShowsRootHandles(true);
        messages.setRootVisible(true);

        messages.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                MessageItem mi = (MessageItem) e.getPath().getLastPathComponent();
                fireMessageGotFocus(mi.getMessageId());
            }
        });
        messages.addTreeExpansionListener(new TreeExpansionListener() {
            public void treeExpanded(TreeExpansionEvent event) {
                TreePath path = event.getPath();
                MessageItem item = (MessageItem) path.getLastPathComponent();

                if (item.getChildren().length == 1) {
                    MessageItem child = item.getChildren()[0];

                    messages.expandPath(path.pathByAddingChild(child));
                }
            }

            public void treeCollapsed(TreeExpansionEvent event) {
            }
        });

        messages.addMouseListener(new ItemListener());

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

    private class ItemListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            checkMenu(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            checkMenu(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            checkMenu(e);
        }

        private void checkMenu(MouseEvent e) {
            if (e.isPopupTrigger()) {
                Point p = e.getPoint();

                TreePath path = messages.getPathForLocation(p.x, p.y);

                if (path != null) {
                    MessageItem mi = (MessageItem) path.getLastPathComponent();

                    JPopupMenu m = createMenu(mi);

                    m.show(ATreeThreadView.this, p.x, p.y);
                }
            }
        }

        private JPopupMenu createMenu(MessageItem mi) {
            return PopupMenuBuilder.getTreeViewPopup(mi.getMessageId());
        }

    }
}
