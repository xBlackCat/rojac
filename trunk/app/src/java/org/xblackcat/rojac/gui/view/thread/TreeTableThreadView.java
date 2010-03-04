package org.xblackcat.rojac.gui.view.thread;

import org.jdesktop.swingx.JXTreeTable;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author xBlackCat
 */

public class TreeTableThreadView extends AThreadView {
    protected final JXTreeTable threads = new JXTreeTable();

    public TreeTableThreadView(IRootPane mainFrame, IThreadControl<Post> threadControl) {
        super(mainFrame, threadControl);

        initializeLayout();
    }

    @Override
    protected JComponent getThreadsContainer() {
        threads.setEditable(false);
        threads.setTreeTableModel(model);
        threads.setRowHeight(0);
        threads.setShowsRootHandles(true);
        threads.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        threads.setEditable(false);
        threads.setSortable(false);
        threads.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        threads.setRowSelectionAllowed(true);
        threads.setRootVisible(threadControl.isRootVisible());

        threads.setDefaultRenderer(Post.class, new PostTableCellRenderer());

        threads.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                TreePath path = threads.getPathForRow(e.getFirstIndex());
                Post mi = (Post) path.getLastPathComponent();
                setSelectedPost(mi);
            }
        });
        threads.addTreeExpansionListener(new TreeExpansionListener() {
            public void treeExpanded(TreeExpansionEvent event) {
                TreePath path = event.getPath();
                Post item = (Post) path.getLastPathComponent();

                if (item.getLoadingState() == LoadingState.NotLoaded) {
                    threadControl.loadChildren(model, item, null);
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
            int row = threads.getRowForPath(path);
            threads.setRowSelectionInterval(row, row);
            Rectangle bounds = threads.getCellRect(
                    row,
                    0, //threads.convertColumnIndexToView(threads.getHierarchicalColumn()), 
                    true);
            bounds.setLocation(0, bounds.y);
            threads.scrollRectToVisible(bounds);
            threads.scrollPathToVisible(path);
        } else {
            threads.clearSelection();
        }
    }

    @Override
    protected Post getSelectedItem() {
        TreePath path = threads.getPathForRow(threads.getSelectedRow());
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

                    m.show(TreeTableThreadView.this, p.x, p.y);
                }
            }
        }

        private JPopupMenu createMenu(ITreeItem mi) {
            return PopupMenuBuilder.getTreeViewPopup(mi, model, mainFrame);
        }

    }

    private static class PostTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);


            return this;
        }
    }
}