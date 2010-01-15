package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.service.executor.TaskType;
import org.xblackcat.rojac.service.janus.commands.AffectedIds;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacWorker;

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
 * @author xBlackCat
 */

public class TreeThreadView extends AThreadView {
    protected final JTree threads = new JTree();

    public TreeThreadView(IRootPane mainFrame, IThreadControl threadControl) {
        super(mainFrame, threadControl);

        initializeLayout();
    }

    @Override
    protected JComponent getThreadsContainer() {
        threads.setEditable(false);
        threads.setModel(model);
        threads.setRowHeight(0);
        threads.setCellRenderer(new MultiLineThreadItemRenderer());
        threads.setShowsRootHandles(true);
        threads.setRootVisible(threadControl.isRootVisible());

        threads.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                ITreeItem mi = (ITreeItem) e.getPath().getLastPathComponent();
                fireMessageGotFocus(mi.getMessageId());

                Long delay = Property.VIEW_THREAD_AUTOSET_READ.get();
                SetMessageReadFlag target = new SetMessageReadFlag(forumId, mi.getMessageId());
                if (delay > 0) {
                    executor.setupTimer("Forum_" + forumId, target, delay);
                } else {
                    executor.execute(target, TaskType.MessageLoading);
                }
            }
        });
        threads.addTreeExpansionListener(new TreeExpansionListener() {
            public void treeExpanded(TreeExpansionEvent event) {
                TreePath path = event.getPath();
                ITreeItem item = (ITreeItem) path.getLastPathComponent();

                if (item.getLoadingState() == LoadingState.NotLoaded) {
                    threadControl.loadChildren(model, item);
                }

                if (item.getLoadingState() == LoadingState.Loaded) {
                    if (item.getSize() == 1) {
                        ITreeItem child = item.getChild(0);

                        threads.expandPath(path.pathByAddingChild(child));
                    }
                }
            }

            public void treeCollapsed(TreeExpansionEvent event) {
            }
        });

        threads.addMouseListener(new ItemListener());

        return threads;
    }

    @Override
    protected void selectItem(Post post) {
        if (post != null) {
            TreePath path = model.getPathToRoot(post);

            TreePath parentPath = path.getParentPath();
            
            if (parentPath != null && threads.isCollapsed(parentPath)) {
                threads.expandPath(parentPath);
            }
            threads.setSelectionPath(path);
        } else {
            threads.clearSelection();
        }
    }

    @Override
    protected Post getSelectedItem() {
        TreePath path = threads.getSelectionPath();
        return path == null ? null : (Post) path.getLastPathComponent();
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

                TreePath path = threads.getPathForLocation(p.x, p.y);

                if (path != null) {
                    ITreeItem mi = (ITreeItem) path.getLastPathComponent();

                    JPopupMenu m = createMenu(mi);

                    m.show(TreeThreadView.this, p.x, p.y);
                }
            }
        }

        private JPopupMenu createMenu(ITreeItem mi) {
            return PopupMenuBuilder.getTreeViewPopup(mi, mainFrame);
        }

    }

    private class SetMessageReadFlag extends RojacWorker<Void, Void> {
        private final int forumId;
        private final int messageId;

        public SetMessageReadFlag(int forumId, int messageId) {
            this.forumId = forumId;
            this.messageId = messageId;
        }

        @Override
        protected Void perform() throws Exception {
            try {
                storage.getMessageAH().updateMessageReadFlag(messageId, true);
            } catch (StorageException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void done() {
            Post post = model.getRoot().getMessageById(messageId);
            if (post != null) {
                post.setRead(true);

                AffectedIds affectedIds = new AffectedIds();
                affectedIds.addMessageId(forumId, messageId);
                mainFrame.updateData(affectedIds);
            }
        }
    }

}
