package org.xblackcat.rojac.gui.frame.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.gui.frame.message.AMessageView;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.ClipboardUtils;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
            final int messageId = mi.getMessageId();
            final JPopupMenu p = new JPopupMenu("#" + messageId);

            JMenuItem item = new JMenuItem("#" + messageId);
            item.setEnabled(false);

            p.add(item);
            p.addSeparator();

            ActionListener copyMessageUrlAction = new CopyUrlAction("http://rsdn.ru/forum/message/" + messageId + ".1.aspx");
            ActionListener copyThreadUrlAction = new CopyUrlAction("http://rsdn.ru/forum/message/" + messageId + ".aspx");
            ActionListener copyFlatThreadUrlAction = new CopyUrlAction("http://rsdn.ru/forum/message/" + messageId + ".flat.aspx");

            JMenu copy = new JMenu();
            copy.setText(Messages.VIEW_THREADS_TREE_MENU_COPYURL.getMessage());

            JMenuItem copyUrl = new JMenuItem();
            copyUrl.setText(Messages.VIEW_THREADS_TREE_MENU_COPYURL_MESSAGE.getMessage());
            copyUrl.addActionListener(copyMessageUrlAction);
            copy.add(copyUrl);

            JMenuItem copyFlatUrl = new JMenuItem();
            copyFlatUrl.setText(Messages.VIEW_THREADS_TREE_MENU_COPYURL_FLAT.getMessage());
            copyFlatUrl.addActionListener(copyFlatThreadUrlAction);
            copy.add(copyFlatUrl);

            JMenuItem copyThreadUrl = new JMenuItem();
            copyThreadUrl.setText(Messages.VIEW_THREADS_TREE_MENU_COPYURL_THREAD.getMessage());
            copyThreadUrl.addActionListener(copyThreadUrlAction);
            copy.add(copyThreadUrl);


            p.add(copy);

            return p;
        }

        private class CopyUrlAction implements ActionListener {
            protected String url;

            public CopyUrlAction(String url) {
                this.url = url;
            }

            public void actionPerformed(ActionEvent e) {
                ClipboardUtils.copyToClipboard(url);
            }
        }
    }
}
