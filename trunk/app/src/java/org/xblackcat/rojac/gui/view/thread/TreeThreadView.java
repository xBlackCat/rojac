package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.view.ViewId;
import org.xblackcat.rojac.gui.view.model.IModelControl;
import org.xblackcat.rojac.gui.view.model.Post;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;

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
        updateRootVisible();

        threads.getSelectionModel().addTreeSelectionListener(new PostSelector());
        threads.addTreeExpansionListener(new ThreadExpander());

        threads.addMouseListener(new ItemListener());

        return threads;
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
    protected void expandPath(TreePath parentPath) {
        threads.expandPath(parentPath);
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
