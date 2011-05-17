package org.xblackcat.rojac.gui.view.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacDebugException;
import org.xblackcat.rojac.gui.*;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.gui.component.ShortCut;
import org.xblackcat.rojac.gui.view.AView;
import org.xblackcat.rojac.gui.view.MessageChecker;
import org.xblackcat.rojac.gui.view.ThreadState;
import org.xblackcat.rojac.gui.view.ViewId;
import org.xblackcat.rojac.gui.view.message.MessageView;
import org.xblackcat.rojac.gui.view.model.*;
import org.xblackcat.rojac.gui.view.model.Thread;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.ShortCutUtils;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;

/**
 * @author xBlackCat
 */

public abstract class AThreadView extends AView implements IItemView {
    private static final Log log = LogFactory.getLog(TreeThreadView.class);

    protected final IModelControl<Post> modelControl;
    protected final AThreadModel<Post> model = new SortedThreadsModel();
    protected int rootItemId;

    protected ThreadState state;
    private JToolBar toolbar;

    public AThreadView(ViewId id, IAppControl appControl, IModelControl<Post> modelControl) {
        super(id, appControl);
        this.modelControl = modelControl;

        model.addTreeModelListener(new DataIntegrityMonitor());

        initializeLayout();
    }

    /**
     * Initializes a common layout part. Do not forget
     */
    private void initializeLayout() {
        JComponent threadsContainer = getThreadsContainer();
        // Initialize tree
        JScrollPane sp = new JScrollPane(threadsContainer);
        add(sp, BorderLayout.CENTER);

        JButton newThreadButton = WindowsUtils.registerImageButton(this, "new_thread", new NewThreadAction());
        JButton toRootButton = WindowsUtils.registerImageButton(this, "to_root", new ToThreadRootAction());
        JButton prevButton = WindowsUtils.registerImageButton(this, "prev", new PreviousAction());
        JButton nextButton = WindowsUtils.registerImageButton(this, "next", new NextAction());
        JButton prevUnreadButton = WindowsUtils.registerImageButton(this, "prev_unread", new PreviousUnreadAction());
        JButton nextUnreadButton = WindowsUtils.registerImageButton(this, "next_unread", new NextUnreadAction());

        toolbar = WindowsUtils.createToolBar(newThreadButton, null, toRootButton, prevButton, nextButton, prevUnreadButton, nextUnreadButton);

        threadsContainer.setInputMap(
                WHEN_ANCESTOR_OF_FOCUSED_COMPONENT,
                ShortCutUtils.mergeInputMaps(
                        getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT),
                        threadsContainer.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                )
        );
        add(toolbar, BorderLayout.NORTH);

        addPropertyChangeListener(MessageView.MESSAGE_VIEWED_FLAG, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Post post = model.getRoot().getMessageById((Integer) evt.getNewValue());

                selectNextPost(post, true);
            }
        });
    }

    @Override
    public void loadItem(int forumId) {
        this.rootItemId = forumId;

        modelControl.fillModelByItemId(model, forumId);
    }

    @Override
    public ThreadState getState() {
        assert RojacUtils.checkThread(true);

        Post p = getSelectedItem();
        int messageId = p == null ? 0 : p.getMessageId();

        return new ThreadState(messageId);
    }

    @Override
    public void setState(IViewState state) {
        assert RojacUtils.checkThread(true);

        if (state == null) {
            return;
        }
        if (!(state instanceof ThreadState)) {
            RojacUtils.fireDebugException("Invalid state object " + state.toString() + " [" + state.getClass() + "]");
            return;
        }

        this.state = (ThreadState) state;

        applyState();
    }

    @Override
    public void makeVisible(int messageId) {
        setState(new ThreadState(messageId));
    }

    @Override
    public boolean containsItem(int messageId) {
        return modelControl.allowSearch() && model.getRoot().getMessageById(messageId) != null;
    }

    @Override
    public String getTabTitle() {
        return modelControl.getTitle(model);
    }

    @Override
    public JPopupMenu getTabTitleMenu() {
        return modelControl.getTitlePopup(model, appControl);
    }

    @Override
    public Icon getTabTitleIcon() {
        return modelControl.getTitleIcon(model);
    }

    protected Object getToolbarPlacement() {
        return ((BorderLayout) getLayout()).getConstraints(toolbar);
    }

    @Override
    public ThreadViewLayout storeLayout() {
        return new ThreadViewLayout(
                getToolbarPlacement(),
                toolbar.getOrientation()
        );
    }

    @Override
    public void setupLayout(IViewLayout o) {
        if (o instanceof ThreadViewLayout) {
            ThreadViewLayout l = (ThreadViewLayout) o;

            remove(toolbar);
            toolbar.setOrientation(l.getToolbarOrientation());
            add(toolbar, l.getToolbarPosition());
        }
    }

    protected final void selectItem(Post post) {
        selectItem(post, false);
    }

    protected abstract void selectItem(Post post, boolean collapseChildren);

    protected abstract Post getSelectedItem();

    protected abstract JComponent getThreadsContainer();

    protected abstract Enumeration<TreePath> getExpandedThreads();

    protected abstract void updateRootVisible();

    protected abstract void expandPath(TreePath parentPath);

    protected abstract TreePath getPathForLocation(Point p);

    private void applyState() {
        assert RojacUtils.checkThread(true);

        if (state == null) {
            return;
        }

        final int messageId = state.openedMessageId();

        if (!model.isInitialized()) {
            // Forum not yet loaded.
            model.addTreeModelListener(new ForumLoadWaiter(messageId));
        } else {
            expandThread(messageId);
        }
    }

    private void selectNextPost(Post currentPost, boolean unread) {
        Post next = findNextPost(currentPost, 0, unread);
        if (next != null) {
            selectItem(next);
        }
    }

    private void selectPrevPost(Post currentPost, boolean unread) {
        Post prevUnread = findPrevPost(currentPost, unread);
        if (prevUnread != null) {
            selectItem(prevUnread);
        }
    }

    private Post findNextPost(Post post, int idx, boolean unread) {
        if (post == null) {
            post = model.getRoot();
            if (post.getSize() == 0) {
                return null;
            }
        }

        if (post.getLoadingState() == LoadingState.NotLoaded) {
            if (!unread || post.isRead() != ReadStatus.Read) {
                // Has unread children but their have not loaded yet.
                modelControl.loadThread(model, post, new LoadNextPost(post, unread));
                // Change post selection when children are loaded
                return null;
            }
        }

        if (post.getLoadingState() == LoadingState.Loaded && idx >= post.getSize() ||
                (unread && post.isRead() == ReadStatus.Read)) {
            // All items in the sub-tree are read.
            // Go to parent and search again
            Post parent = post.getParent();
            if (parent != null) {
                int nextIdx = parent.getIndex(post) + 1;
                return findNextPost(parent, nextIdx, unread);
            } else {
                return null;
            }
        }

        int i = idx;
        while (i < post.getSize()) {
            Post p = post.getChild(i);
            if (!unread) {
                return p;
            }

            switch (p.isRead()) {
                case Read:
                    // Go to next child of post
                    i++;
                    break;
                case ReadPartially:
                    switch (p.getLoadingState()) {
                        case Loaded:
                            // Go deep to next child of the child
                            i = 0;
                            post = p;
                            break;
                        case NotLoaded:
                            modelControl.loadThread(model, p, new LoadNextPost(post, unread));
                        case Loading:
                            return null;
                    }
                    break;
                case Unread:
                    return p;
            }
        }

        // Thread has unread posts only before the selected post. Go to parent and continue the search.
        Post parent = post.getParent();
        if (unread && parent != null) {
            int nextIdx = parent.getIndex(post) + 1;
            return findNextPost(parent, nextIdx, unread);
        } else {
            return null;
        }
    }

    private Post findPrevPost(Post post, boolean unread) {
        try {
            if (post == null) {
                return findLastPost(model.getRoot(), unread);
            }

            Post parent = post.getParent();
            if (parent == null) {
                return null;
            }

            int idx = parent.getIndex(post) - 1;

            if (unread) {
                while (idx >= 0) {
                    Post p = parent.getChild(idx);
                    switch (p.isRead()) {
                        case Read:
                            idx--;
                            break;
                        case ReadPartially:
                        case Unread:
                            return findLastPost(p, unread);
                    }
                }

                switch (parent.isRead()) {
                    case Read:
                    case ReadPartially:
                        return findPrevPost(parent, true);
                    case Unread:
                        return parent;
                }
            } else if (idx >= 0) {
                return findLastPost(parent.getChild(idx), unread);
            } else {
                return parent;
            }
        } catch (RuntimeException e) {
            // Just go through to restart search later
        }

        return null;
    }

    /**
     * Searches for the last unread post in the tree thread.
     *
     * @param post   root of sub-tree.
     * @param unread
     *
     * @return last unread post in sub-tree or <code>null</code> if no unread post is exist in sub-tree.
     *
     * @throws RuntimeException will be thrown in case when data loading is needed to make correct search.
     */
    private Post findLastPost(Post post, boolean unread) throws RuntimeException {
        if (unread && post.isRead() == ReadStatus.Read) {
            return null;
        }

        switch (post.getLoadingState()) {
            case NotLoaded:
                modelControl.loadThread(model, post, new LoadPreviousPost(post, unread));
            case Loading:
                throw new RojacDebugException("Restart search later");
        }

        int idx = post.getSize() - 1;
        while (idx >= 0) {
            Post p = findLastPost(post.getChild(idx), unread);
            if (p != null) {
                return p;
            }
            idx--;
        }

        if (!unread || post.isRead() == ReadStatus.Unread) {
            return post;
        }

        return null;
    }

    private void expandThread(final int messageId) {
        // Check for threads
        Post post = model.getRoot().getMessageById(messageId);
        if (post != null) {
            selectItem(post);
        } else {
            new ThreadChecker(messageId).execute();
        }
    }

    private void resortAndReloadModel() {
        Post selected = getSelectedItem();

        boolean skipSaveState = Property.VIEW_THREAD_COLLAPSE_THREADS_AFTER_SYNC.get();

        Enumeration<TreePath> expanded = skipSaveState ? null : getExpandedThreads();

        modelControl.resortModel(model);

        if (expanded != null) {
            while (expanded.hasMoreElements()) {
                expandPath(expanded.nextElement());
            }
        }

        if (selected != null) {
            selectItem(selected);
        }
    }

    @Override
    public final void processPacket(IPacket packet) {
        // Just in case store a current selection
        final Post curSelection = getSelectedItem();
        Runnable postProcessor = new Runnable() {
            public void run() {
                if (curSelection != null &&
                        !curSelection.equals(getSelectedItem())) {
                    selectItem(curSelection);
                }
            }
        };

        modelControl.processPacket(model, packet, postProcessor);
    }

    private class LoadNextPost implements Runnable {
        private final Post item;
        private final boolean unread;

        public LoadNextPost(Post item, boolean unread) {
            this.item = item;
            this.unread = unread;
        }

        @Override
        public void run() {
            selectNextPost(item, unread);
        }
    }

    private class LoadPreviousPost implements Runnable {
        private final Post item;
        private final boolean unread;

        public LoadPreviousPost(Post item, boolean unread) {
            this.item = item;
            this.unread = unread;
        }

        @Override
        public void run() {
            Post prevUnread = findLastPost(item, unread);
            if (prevUnread != null) {
                selectItem(prevUnread);
            }
        }
    }

    private class ThreadChecker extends MessageChecker {
        public ThreadChecker(int messageId) {
            super(messageId);
        }

        @Override
        protected void done() {
            if (data != null) {
                final Post root = model.getRoot();
                Post rootMessage = root.getMessageById(data.getThreadRootId());
                if (rootMessage != null) {
                    modelControl.loadThread(model, rootMessage, new Runnable() {
                        @Override
                        public void run() {
                            selectItem(root.getMessageById(messageId));
                        }
                    });
                }
            } else {
                if (sourceStackTrace != null) {
                    log.error("Can't load message #" + messageId, sourceStackTrace);
                }
                JLOptionPane.showMessageDialog(
                        SwingUtilities.windowForComponent(AThreadView.this),
                        Messages.ErrorDialog_MessageNotFound_Message.get(messageId),
                        Messages.ErrorDialog_MessageNotFound_Title.get(messageId),
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

    private class NewThreadAction extends AButtonAction {
        public NewThreadAction() {
            super(ShortCut.NewThread);
        }

        public void actionPerformed(ActionEvent e) {
            appControl.editMessage(rootItemId, null);
        }
    }

    private class PreviousUnreadAction extends AButtonAction {
        private PreviousUnreadAction() {
            super(ShortCut.PrevUnreadMessage);
        }

        public void actionPerformed(ActionEvent e) {
            Post currentPost = getSelectedItem();
            selectPrevPost(currentPost, true);
        }
    }

    private class NextUnreadAction extends AButtonAction {
        private NextUnreadAction() {
            super(ShortCut.NextUnreadMessage);
        }

        public void actionPerformed(ActionEvent e) {
            Post currentPost = getSelectedItem();
            selectNextPost(currentPost, true);
        }
    }

    private class PreviousAction extends AButtonAction {
        private PreviousAction() {
            super(ShortCut.PrevMessage);
        }

        public void actionPerformed(ActionEvent e) {
            Post currentPost = getSelectedItem();
            selectPrevPost(currentPost, false);
        }
    }

    private class NextAction extends AButtonAction {
        private NextAction() {
            super(ShortCut.NextMessage);
        }

        public void actionPerformed(ActionEvent e) {
            Post currentPost = getSelectedItem();
            selectNextPost(currentPost, false);
        }
    }

    private class ToThreadRootAction extends AButtonAction {
        private ToThreadRootAction() {
            super(ShortCut.ToThreadRoot);
        }

        public void actionPerformed(ActionEvent e) {
            Post currentPost = getSelectedItem();
            if (currentPost != null) {
                selectItem(modelControl.getTreeRoot(currentPost), true);
            }

        }
    }

    private class DataIntegrityMonitor implements TreeModelListener {
        @Override
        public void treeNodesChanged(TreeModelEvent e) {
            Post root = model.getRoot();
            if (e.getTreePath().getLastPathComponent() == root) {
                fireInfoChanged();
            }
        }

        @Override
        public void treeNodesInserted(TreeModelEvent e) {
        }

        @Override
        public void treeNodesRemoved(TreeModelEvent e) {
        }

        @Override
        public void treeStructureChanged(TreeModelEvent e) {
            Post root = model.getRoot();
            if (root == null) {
                AThreadView.this.appControl.closeTab(getId());
            } else {
                updateRootVisible();

                if (e.getTreePath() == null) {
                    // Resort model nodes
                    resortAndReloadModel();
                }

                fireInfoChanged();
            }
        }
    }

    protected class PostSelector implements TreeSelectionListener {
        public void valueChanged(TreeSelectionEvent e) {
            fireViewStateChanged();
        }
    }

    protected class ThreadExpander implements TreeExpansionListener {
        public void treeExpanded(TreeExpansionEvent event) {
            final TreePath path = event.getPath();

            Post item = (Post) path.getLastPathComponent();

            if (item.getLoadingState() == LoadingState.NotLoaded) {
                modelControl.loadThread(model, item, new Runnable() {
                    @Override
                    public void run() {
                        // Only threads could be loaded.
                        assert path.getLastPathComponent() instanceof Thread;

                        Thread item = (Thread) path.getLastPathComponent();

                        item.setLoadingState(LoadingState.Loaded);

                        model.nodeStructureChanged(item);

                        expandPath(path);
                    }
                });
            }

            if (item.getLoadingState() == LoadingState.Loaded) {
                if (item.getSize() == 1) {
                    ITreeItem child = item.getChild(0);

                    expandPath(path.pathByAddingChild(child));
                }
            }
        }

        public void treeCollapsed(TreeExpansionEvent event) {
        }
    }

    protected class ItemListener extends PopupMouseAdapter {
        @Override
        protected void triggerDoubleClick(MouseEvent e) {
        }

        @Override
        protected void triggerPopup(MouseEvent e) {
            Point p = e.getPoint();

            TreePath path = getPathForLocation(p);

            if (path != null) {
                Post mi = (Post) path.getLastPathComponent();

                JPopupMenu m = modelControl.getItemMenu(mi, appControl);

                if (m != null) {
                    m.show(e.getComponent(), p.x, p.y);
                }
            }
        }
    }

    /**
     * Util class: waits until forum will be loaded and then select specified message in thread view
     */
    private class ForumLoadWaiter implements TreeModelListener {
        private final int messageId;

        public ForumLoadWaiter(int messageId) {
            this.messageId = messageId;
        }

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
            if (e.getPath() == null && model.isInitialized() && model.getRoot() != null) {
                model.removeTreeModelListener(this);
                if (messageId != 0) {
                    expandThread(messageId);
                }
            }
        }
    }
}
