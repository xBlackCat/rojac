package org.xblackcat.rojac.gui.view.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.IViewState;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.gui.component.ShortCut;
import org.xblackcat.rojac.gui.view.AnItemView;
import org.xblackcat.rojac.gui.view.MessageChecker;
import org.xblackcat.rojac.gui.view.ThreadState;
import org.xblackcat.rojac.gui.view.ViewId;
import org.xblackcat.rojac.gui.view.message.MessageView;
import org.xblackcat.rojac.gui.view.model.*;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.datahandler.IPacketProcessor;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.ShortCutUtils;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;

/**
 * @author xBlackCat
 */

public abstract class AThreadView extends AnItemView {
    private static final Log log = LogFactory.getLog(TreeThreadView.class);

    protected final IModelControl<Post> modelControl;
    protected final AThreadModel<Post> model = new SortedThreadsModel();
    protected String title;
    protected int rootItemId;

    protected ThreadState state;

    protected AThreadView(ViewId id, IAppControl appControl, IModelControl<Post> modelControl) {
        super(id, appControl);
        this.modelControl = modelControl;

        model.addTreeModelListener(new DataIntegrityMonitor());
    }

    private void completeUpdateModel() {
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

    protected void initializeLayout() {
        // Initialize tree
        JComponent threadsContainer = getThreadsContainer();
        JScrollPane sp = new JScrollPane(threadsContainer);
        add(sp, BorderLayout.CENTER);

        JButton newThreadButton = WindowsUtils.registerImageButton(this, "new_thread", new NewThreadAction());
        JButton toRootButton = WindowsUtils.registerImageButton(this, "to_root", new ToThreadRootAction());
        JButton prevUnreadButton = WindowsUtils.registerImageButton(this, "prev_unread", new PreviousUnreadAction());
        JButton nextUnreadButton = WindowsUtils.registerImageButton(this, "next_unread", new NextUnreadAction());

        JToolBar toolbar = WindowsUtils.createToolBar(newThreadButton, null, toRootButton, prevUnreadButton, nextUnreadButton);

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

                selectNextUnread(post);
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
        assert RojacUtils.checkThread(true, AThreadView.class);

        Post p = getSelectedItem();
        int messageId = p == null ? 0 : p.getMessageId();

        return new ThreadState(messageId);
    }

    @Override
    public void setState(IViewState state) {
        assert RojacUtils.checkThread(true, AThreadView.class);

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
    @SuppressWarnings({"unchecked"})
    protected IPacketProcessor<IPacket>[] getProcessors() {
        return new IPacketProcessor[]{
                new IPacketProcessor<IPacket>() {
                    @Override
                    public void process(IPacket p) {
                        // Just in case store a current selection
                        Post curSelection = getSelectedItem();

                        if (modelControl.processPacket(model, p)) {
                            if (curSelection != null) {
                                selectItem(curSelection);
                            }
                        }
                    }

                }
        };
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
        assert RojacUtils.checkThread(true, AThreadView.class);

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

    private void selectNextUnread(Post currentPost) {
        Post nextUnread = getNextUnread(currentPost, 0);
        if (nextUnread != null) {
            selectItem(nextUnread);
        }
    }

    private void selectPrevUnread(Post currentPost) {
        Post prevUnread = getPrevUnread(currentPost);
        if (prevUnread != null) {
            selectItem(prevUnread);
        }
    }

    private Post getNextUnread(Post post, int idx) {
        if (post == null) {
            post = model.getRoot();
            if (post.getSize() == 0) {
                return null;
            }
        }

        if (post.getLoadingState() == LoadingState.NotLoaded && post.isRead() != ReadStatus.Read) {
            // Has unread children but their have not loaded yet.
            modelControl.loadThread(model, post, new LoadNextUnread());
            // Change post selection when children are loaded
            return null;
        }

        if (post.getLoadingState() == LoadingState.Loaded && idx >= post.getSize() || post.isRead() == ReadStatus.Read) {
            // All items in the sub-tree are read.
            // Go to parent and search again
            Post parent = post.getParent();
            if (parent != null) {
                int nextIdx = parent.getIndex(post) + 1;
                return getNextUnread(parent, nextIdx);
            } else {
                return null;
            }
        }

        int i = idx;
        while (i < post.getSize()) {
            Post p = post.getChild(i);
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
                            modelControl.loadThread(model, p, new LoadNextUnread());
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
        if (parent != null) {
            int nextIdx = parent.getIndex(post) + 1;
            return getNextUnread(parent, nextIdx);
        } else {
            return null;
        }
    }

    private Post getPrevUnread(Post post) {
        try {
            if (post == null) {
                return findLastUnreadPost(model.getRoot());
            }

            Post parent = post.getParent();
            if (parent == null) {
                return null;
            }

            int idx = parent.getIndex(post) - 1;

            while (idx >= 0) {
                Post p = parent.getChild(idx);
                switch (p.isRead()) {
                    case Read:
                        idx--;
                        break;
                    case ReadPartially:
                    case Unread:
                        return findLastUnreadPost(p);
                }
            }

            switch (parent.isRead()) {
                case Read:
                case ReadPartially:
                    return getPrevUnread(parent);
                case Unread:
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
     * @param post root of sub-tree.
     * @return last unread post in sub-tree or <code>null</code> if no unread post is exist in sub-tree.
     * @throws RuntimeException will be thrown in case when data loading is needed to make correct search.
     */
    private Post findLastUnreadPost(Post post) throws RuntimeException {
        if (post.isRead() == ReadStatus.Read) {
            return null;
        }

        switch (post.getLoadingState()) {
            case NotLoaded:
                modelControl.loadThread(model, post, new LoadPreviousUnread());
            case Loading:
                throw new RuntimeException("Restart search later");
        }

        int idx = post.getSize() - 1;
        while (idx >= 0) {
            Post p = findLastUnreadPost(post.getChild(idx));
            if (p != null) {
                return p;
            }
            idx--;
        }

        if (post.isRead() == ReadStatus.Unread) {
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

    private class LoadNextUnread implements IItemProcessor<Post> {
        @Override
        public void processItem(Post item) {
            selectNextUnread(item);
        }
    }

    private class LoadPreviousUnread implements IItemProcessor<Post> {
        @Override
        public void processItem(Post item) {
            Post prevUnread = findLastUnreadPost(item);
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
                    modelControl.loadThread(model, rootMessage, new IItemProcessor<Post>() {
                        @Override
                        public void processItem(Post item) {
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
            super(Messages.View_Thread_Button_NewThread, ShortCut.NewThread);
        }

        public void actionPerformed(ActionEvent e) {
            appControl.editMessage(rootItemId, null);
        }
    }

    private class PreviousUnreadAction extends AButtonAction {
        private PreviousUnreadAction() {
            super(Messages.View_Thread_Button_PreviousUnread, ShortCut.PrevUnreadMessage);
        }

        public void actionPerformed(ActionEvent e) {
            Post currentPost = getSelectedItem();
            selectPrevUnread(currentPost);
        }
    }

    private class NextUnreadAction extends AButtonAction {
        private NextUnreadAction() {
            super(Messages.View_Thread_Button_NextUnread, ShortCut.NextUnreadMessage);
        }

        public void actionPerformed(ActionEvent e) {
            Post currentPost = getSelectedItem();
            selectNextUnread(currentPost);
        }
    }

    private class ToThreadRootAction extends AButtonAction {
        private ToThreadRootAction() {
            super(Messages.View_Thread_Button_ToThreadRoot, ShortCut.ToThreadRoot);
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
                fireItemUpdated(root.getForumId(), root.getMessageId());
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
                    completeUpdateModel();
                } else if (e.getTreePath().getLastPathComponent() == root) {
                    fireItemUpdated(root.getForumId(), root.getMessageId());
                    selectItem(root);
                }
            }
        }
    }

    protected class PostSelector implements TreeSelectionListener {
        public void valueChanged(TreeSelectionEvent e) {
            Post mi = (Post) e.getPath().getLastPathComponent();
            selectItem(mi);

            fireMessageGotFocus(mi.getForumId(), mi.getMessageId());
            fireViewStateChanged();
        }
    }

    protected class ThreadExpander implements TreeExpansionListener {
        public void treeExpanded(TreeExpansionEvent event) {
            TreePath path = event.getPath();
            Post item = (Post) path.getLastPathComponent();

            if (item.getLoadingState() == LoadingState.NotLoaded) {
                modelControl.loadThread(model, item, null);
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

    protected class ItemListener extends MouseAdapter {
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
            if (e.getPath() == null && model.isInitialized()) {
                model.removeTreeModelListener(this);
                if (messageId != 0) {
                    expandThread(messageId);
                }
                return;
            }

            if (model.getRoot() == null) {
                appControl.closeTab(getId());
            }
        }
    }
}
