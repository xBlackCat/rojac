package org.xblackcat.rojac.gui.view.thread;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.table.TableColumnExt;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.IViewLayout;
import org.xblackcat.rojac.gui.view.ThreadState;
import org.xblackcat.rojac.gui.view.ViewId;
import org.xblackcat.rojac.gui.view.model.APostProxy;
import org.xblackcat.rojac.gui.view.model.Header;
import org.xblackcat.rojac.gui.view.model.ModelControl;
import org.xblackcat.rojac.gui.view.model.Post;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

/**
 * @author xBlackCat
 */

public class TreeTableThreadView extends AThreadView {
    protected JXTreeTable threads;

    public TreeTableThreadView(ViewId id, IAppControl appControl, ModelControl modelControl) {
        super(id, appControl, modelControl);
    }

    @Override
    protected JComponent getThreadsContainer() {
        if (threads == null) {
            threads = new JXTreeTable();
            threads.setAutoCreateColumnsFromModel(false);
            threads.setTreeTableModel(model);
            threads.setEditable(false);
            threads.setShowsRootHandles(true);
            threads.setSortable(false);
            threads.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            threads.setRowSelectionAllowed(true);
            threads.setColumnSelectionAllowed(false);
            threads.setScrollsOnExpand(true);
            threads.setRootVisible(modelControl.isRootVisible());
            threads.setToggleClickCount(2);

            threads.setDefaultRenderer(APostProxy.class, new PostTableCellRenderer());
            threads.setTreeCellRenderer(new PostTreeCellRenderer());

            threads.addTreeSelectionListener(new PostSelector());
            threads.addTreeExpansionListener(new ThreadExpander());
            threads.addMouseListener(new ItemListener());

            // Restore selection on any table data changing.
            threads.getModel().addTableModelListener(new SelectionHolder());

            threads.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            for (Header h : Header.values()) {
                TableColumnExt column = new TableColumnExt(h.ordinal());
                if (h.getWidth() > 0) {
                    column.setPreferredWidth(h.getWidth());
                    column.setMaxWidth(h.getWidth() << 2);
                }
                column.setMinWidth(10);
                column.setIdentifier(h);
                column.setToolTipText(h.getTitle());
                threads.addColumn(column);
            }

            // Handle keyboard events to emulate tree navigation in TreeTable
            threads.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "prevOrClose");
            threads.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "nextOrExpand");

            threads.getActionMap().put("prevOrClose", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int row = threads.getSelectedRow();
                    if (row == -1) {
                        return;
                    }

                    if (threads.isExpanded(row)) {
                        threads.collapseRow(row);
                        threads.scrollRowToVisible(row);
                    } else if (row > 0) {
                        threads.setRowSelectionInterval(row - 1, row - 1);
                        threads.scrollRowToVisible(row - 1);
                    }
                }
            });
            threads.getActionMap().put("nextOrExpand", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int row = threads.getSelectedRow();
                    if (row == -1) {
                        return;
                    }

                    if (!threads.isExpanded(row) && !model.isLeaf(threads.getPathForRow(row).getLastPathComponent())) {
                        threads.expandRow(row);
                        threads.scrollRowToVisible(row);
                    } else if (row < threads.getRowCount() - 1) {
                        threads.scrollRowToVisible(row + 1);
                        threads.setRowSelectionInterval(row + 1, row + 1);
                    }
                }
            });
        }
        return threads;
    }

    @Override
    public TableThreadViewLayout storeLayout() {
        TableColumnModel cm = threads.getColumnModel();

        TableThreadViewLayout.Column[] columns = new TableThreadViewLayout.Column[Header.values().length];
        int i = 0;

        for (Header h : Header.values()) {
            TableColumn c = cm.getColumn(h.ordinal());

            int width = c.getWidth();
            int columnIndex = cm.getColumnIndex(h);

            columns[i++] = new TableThreadViewLayout.Column(h, columnIndex, width);
        }

        return new TableThreadViewLayout(super.storeLayout(), columns);
    }

    @Override
    public void setupLayout(IViewLayout o) {
        super.setupLayout(o);

        if (o instanceof TableThreadViewLayout) {
            TableColumnModel cm = threads.getColumnModel();

            for (TableThreadViewLayout.Column c : ((TableThreadViewLayout) o).getColumns()) {
                int idx = cm.getColumnIndex(c.getAnchor());

                cm.getColumn(idx).setWidth(c.getWidth());
                cm.getColumn(idx).setPreferredWidth(c.getWidth());
                cm.moveColumn(idx, c.getIndex());
            }
        }
    }

    @SuppressWarnings({"unchecked"})
    @Override
    protected Enumeration<TreePath> getExpandedThreads() {
        return (Enumeration<TreePath>) threads.getExpandedDescendants(model.getPathToRoot(model.getRoot()));
    }

    @Override
    protected void updateRootVisible() {
        threads.setRootVisible(modelControl.isRootVisible());
    }

    @Override
    protected void selectItem(Post post, boolean collapseChildren) {
        if (post != null) {
            TreePath path = model.getPathToRoot(post);

            TreePath parentPath = path.getParentPath();

            if (parentPath != null && threads.isCollapsed(parentPath)) {
                expandPath(parentPath);
            }
            scrollPathToVisible(path);

            if (collapseChildren) {
                threads.collapsePath(path);
            }
        } else {
            threads.clearSelection();
        }
    }

    protected void scrollPathToVisible(TreePath path) {
        int row = threads.getRowForPath(path);
        if (row >= 0) {
            threads.setRowSelectionInterval(row, row);
            Rectangle bounds = threads.getCellRect(
                    row,
                    threads.convertColumnIndexToView(threads.getHierarchicalColumn()),
                    true);
            threads.scrollRectToVisible(bounds);
        }
        threads.scrollPathToVisible(path);
    }

    protected void expandPath(TreePath parentPath) {
        threads.expandPath(parentPath);
    }

    protected void collapsePath(TreePath path) {
        threads.collapsePath(path);
    }

    @Override
    protected Post getSelectedItem() {
        TreePath path = threads.getPathForRow(threads.getSelectedRow());
        return path == null ? null : (Post) path.getLastPathComponent();
    }

    protected TreePath getPathForLocation(Point p) {
        return threads.getPathForLocation(p.x, p.y);
    }

    private class SelectionHolder implements TableModelListener {
        @Override
        public void tableChanged(TableModelEvent e) {
            if (e.getType() == TableModelEvent.UPDATE &&
                    !(e.getColumn() == TableModelEvent.ALL_COLUMNS &&
                            e.getFirstRow() == TableModelEvent.HEADER_ROW &&
                            e.getLastRow() == TableModelEvent.HEADER_ROW)) {
                return;
            }

            ThreadState s = getObjectState();
            if (s.openedMessageId() == 0) {
                return;
            }

            Post post = model.getRoot().getMessageById(s.openedMessageId());
            if (post == null) {
                return;
            }

            final TreePath pathToRoot = model.getPathToRoot(post);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    scrollPathToVisible(pathToRoot);
                }
            });
        }
    }
}
