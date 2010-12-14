package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.gui.view.ViewId;
import org.xblackcat.rojac.gui.view.model.IModelControl;
import org.xblackcat.rojac.gui.view.model.ITreeItem;
import org.xblackcat.rojac.gui.view.model.LoadingState;
import org.xblackcat.rojac.gui.view.model.Post;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author xBlackCat
 */

public class TreeThreadView extends AThreadView {
    protected final JTree threads = new JTree();

    public TreeThreadView(ViewId id, IAppControl appControl, IModelControl<Post> modelControl) {
        super(id, appControl, modelControl);

        initializeLayout();
    }

    @Override
    protected JComponent getThreadsContainer() {
        threads.setEditable(false);
        threads.setModel(model);
        threads.setRowHeight(0);
        threads.setCellRenderer(new MultiLineThreadItemRenderer());
        threads.setShowsRootHandles(true);
        threads.setRootVisible(modelControl.isRootVisible());

        model.addTreeModelListener(new TreeModelListener() {
            @Override
            public void treeNodesChanged(TreeModelEvent e) {
            }

            @Override
            public void treeNodesInserted(TreeModelEvent e) {
            }

            @Override
            public void treeNodesRemoved(TreeModelEvent e) {
            }

            @Override
            public void treeStructureChanged(TreeModelEvent e) {
                threads.setRootVisible(modelControl.isRootVisible());
            }
        });

        threads.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                Post mi = (Post) e.getPath().getLastPathComponent();
                setSelectedPost(mi);
            }
        });
        threads.addTreeExpansionListener(new TreeExpansionListener() {
            public void treeExpanded(TreeExpansionEvent event) {
                TreePath path = event.getPath();
                Post item = (Post) path.getLastPathComponent();

                if (item.getLoadingState() == LoadingState.NotLoaded) {
                    modelControl.loadThread(model, item, null);
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
    protected void selectItem(Post post, boolean collapseChildren) {
        if (post != null) {
            TreePath path = model.getPathToRoot(post);

            TreePath parentPath = path.getParentPath();

            if (parentPath != null && threads.isCollapsed(parentPath)) {
                threads.expandPath(parentPath);
            }
            threads.setSelectionPath(path);
            Rectangle bounds = threads.getPathBounds(path);
            bounds.setLocation(0, bounds.y);
            threads.scrollRectToVisible(bounds);
            threads.scrollPathToVisible(path);

            if (collapseChildren) {
                threads.collapsePath(path);
            }
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
                    Post mi = (Post) path.getLastPathComponent();

                    JPopupMenu m = createMenu(mi);

                    m.show(e.getComponent(), p.x, p.y);
                }
            }
        }

        private JPopupMenu createMenu(Post mi) {
            return PopupMenuBuilder.getTreeViewMenu(mi, model, appControl);
        }

    }

}
