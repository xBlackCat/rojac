package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;

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
            }
        });
        threads.addTreeExpansionListener(new TreeExpansionListener() {
            public void treeExpanded(TreeExpansionEvent event) {
                TreePath path = event.getPath();
                ITreeItem item = (ITreeItem) path.getLastPathComponent();

                if (item.getLoadingState() == LoadingState.NotLoaded) {
                    threadControl.loadChildren(model, item);
                }

                if (item.getLoadingState() == LoadingState.Loaded ) {
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
    protected void selectFirstItem() {
        threads.setSelectionRow(0);
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
            return PopupMenuBuilder.getTreeViewPopup(mi.getMessageId(), mainFrame);
        }

    }
}
