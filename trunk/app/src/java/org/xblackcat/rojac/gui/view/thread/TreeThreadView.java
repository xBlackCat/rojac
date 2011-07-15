package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.view.ViewId;
import org.xblackcat.rojac.gui.view.model.ModelControl;
import org.xblackcat.rojac.gui.view.model.Post;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.Enumeration;

/**
 * @author xBlackCat
 */

public class TreeThreadView extends AThreadView {
    protected JTree threads;

    public TreeThreadView(ViewId id, IAppControl appControl, ModelControl modelControl) {
        super(id, appControl, modelControl);
    }

    @Override
    protected JComponent getThreadsContainer() {
        if (threads == null) {
            threads = new JTree(model);
            threads.setEditable(false);
            threads.setRowHeight(0);
            threads.setCellRenderer(new MultiLineThreadItemRenderer());
            threads.setShowsRootHandles(true);
            updateRootVisible();

            threads.getSelectionModel().addTreeSelectionListener(new PostSelector());
            threads.addTreeExpansionListener(new ThreadExpander());

            threads.addMouseListener(new ItemListener());
        }
        return threads;
    }

    @Override
    protected Enumeration<TreePath> getExpandedThreads() {
        return threads.getExpandedDescendants(model.getPathToRoot(model.getRoot()));
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
            threads.setSelectionPath(path);
            scrollPathToVisible(path);

            if (collapseChildren) {
                threads.collapsePath(path);
            }
        } else {
            threads.clearSelection();
        }
    }

    protected void scrollPathToVisible(TreePath path) {
        Rectangle bounds = threads.getPathBounds(path);
        bounds.setLocation(0, bounds.y);
        threads.scrollRectToVisible(bounds);
        threads.scrollPathToVisible(path);
    }

    @Override
    protected void expandPath(TreePath parentPath) {
        threads.expandPath(parentPath);
    }

    @Override
    protected void collapsePath(TreePath path) {
        threads.collapsePath(path);
    }

    @Override
    protected Post getSelectedItem() {
        TreePath path = threads.getSelectionPath();
        return path == null ? null : (Post) path.getLastPathComponent();
    }

    protected TreePath getPathForLocation(Point p) {
        return threads.getPathForLocation(p.x, p.y);
    }
}
