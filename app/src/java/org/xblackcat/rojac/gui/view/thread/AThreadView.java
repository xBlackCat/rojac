package org.xblackcat.rojac.gui.view.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.gui.component.ShortCut;
import org.xblackcat.rojac.gui.view.AnItemView;
import org.xblackcat.rojac.gui.view.MessageChecker;
import org.xblackcat.rojac.gui.view.ViewId;
import org.xblackcat.rojac.gui.view.message.MessageView;
import org.xblackcat.rojac.gui.view.model.AThreadModel;
import org.xblackcat.rojac.gui.view.model.IModelControl;
import org.xblackcat.rojac.gui.view.model.LoadingState;
import org.xblackcat.rojac.gui.view.model.Post;
import org.xblackcat.rojac.gui.view.model.ReadStatus;
import org.xblackcat.rojac.gui.view.model.SortedThreadsModel;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.datahandler.IPacketProcessor;
import org.xblackcat.rojac.service.datahandler.SetForumReadPacket;
import org.xblackcat.rojac.service.datahandler.SetPostReadPacket;
import org.xblackcat.rojac.service.datahandler.SynchronizationCompletePacket;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.util.MessageUtils;
import org.xblackcat.rojac.util.ShortCutUtils;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author xBlackCat
 */

public abstract class AThreadView extends AnItemView {
    private static final Log log = LogFactory.getLog(TreeThreadView.class);

    protected final IModelControl<Post> modelControl;
    protected final AThreadModel<Post> model = new SortedThreadsModel();
    protected String title;
    protected int rootItemId;

    protected AThreadView(ViewId id, IAppControl appControl, IModelControl<Post> modelControl) {
        super(id, appControl);
        this.modelControl = modelControl;
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

                if (Property.VIEW_THREAD_SET_READ_ON_SCROLL.get()) {
                    MessageUtils.markForumMessageRead(post, 0);
                }

                selectNextUnread(post);
            }
        });
    }

    @Override
    public void loadItem(int forumId) {
        this.rootItemId = forumId;
        model.addTreeModelListener(new TreeModelListener() {
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
                if (e.getTreePath().getLastPathComponent() == root) {
                    fireItemUpdated(root.getForumId(), root.getMessageId());
                    selectItem(root);
                }
            }
        });

        modelControl.fillModelByItemId(model, forumId);
    }

    @Override
    public int getVisibleId() {
        Post p = getSelectedItem();
        return p == null ? 0 : p.getMessageId();
    }

    @Override
    @SuppressWarnings({"unchecked"})
    protected IPacketProcessor<IPacket>[] getProcessors() {
        return new IPacketProcessor[]{
                new IPacketProcessor<SetForumReadPacket>() {
                    @Override
                    public void process(SetForumReadPacket p) {
                        if (p.getForumId() == rootItemId) {
                            modelControl.markForumRead(model, p.isRead());
                        }
                    }
                },
                new IPacketProcessor<SetPostReadPacket>() {
                    @Override
                    public void process(SetPostReadPacket p) {
                        if (p.getForumId() == rootItemId) {
                            if (p.isRecursive()) {
                                // Post is a root of marked thread
                                modelControl.markThreadRead(model, p.getPostId(), p.isRead());
                            } else {
                                // Mark as read only the post
                                modelControl.markPostRead(model, p.getPostId(), p.isRead());
                            }
                        }
                    }
                },
                new IPacketProcessor<SynchronizationCompletePacket>() {
                    @Override
                    public void process(SynchronizationCompletePacket p) {
                        if (!p.isForumAffected(rootItemId)) {
                            // Current forum is not changed - have a rest
                            return;
                        }

                        Post curSelection = getSelectedItem();

                        modelControl.updateModel(model, p.getThreadIds());

                        selectItem(curSelection);
                    }
                },
        };
    }

    @Override
    public void makeVisible(final int messageId) {
        Post post = model.getRoot().getMessageById(messageId);
        if (post != null) {
            selectItem(post);
        } else {
            if (!model.isInitialized()) {
                // Forum not yet loaded.
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
                        model.removeTreeModelListener(this);
                        expandThread(messageId);
                    }
                });
            } else {
                expandThread(messageId);
            }
        }
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

    @Override
    public boolean containsItem(int messageId) {
        return model.getRoot().getMessageById(messageId) != null;
    }

    protected final void selectItem(Post post) {
        selectItem(post, false);
    }

    protected abstract void selectItem(Post post, boolean collapseChildren);

    protected abstract Post getSelectedItem();

    protected abstract JComponent getThreadsContainer();

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

        if (post.getLoadingState() == LoadingState.NotLoaded && post.isRead() == ReadStatus.ReadPartially) {
            // Has unread children but their have not loaded yet.
            modelControl.loadThread(model, post, new LoadNextUnread());
            // Change post selection when children are loaded
            return null;
        }

        if (post.getLoadingState() == LoadingState.Loaded && idx >= post.getSize() || post.isRead() == ReadStatus.Read) {
            // All items in the subtree are read.
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
     * @param post root of subtree.
     *
     * @return last unread post in subtree or <code>null</code> if no unread post is exist in subtree.
     *
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

    /**
     * Make common tasks for selected post: notify listeners about focus changing and aim a timer to make the post
     * readable after a specified time period.
     *
     * @param mi selected post.
     */
    protected void setSelectedPost(Post mi) {
        fireMessageGotFocus(mi.getForumId(), mi.getMessageId());

        if (mi.isRead() == ReadStatus.Unread) {
            Long delay = Property.VIEW_THREAD_AUTOSET_READ.get();
            if (delay != null && delay >= 0) {
                MessageUtils.markForumMessageRead(mi, delay);
            }
        }
    }

    @Override
    public String getTabTitle() {
        return modelControl.getTitle(model);
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
                modelControl.loadThread(model, rootMessage, new IItemProcessor<Post>() {
                    @Override
                    public void processItem(Post item) {
                        selectItem(root.getMessageById(messageId));
                    }
                });
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
                selectItem(currentPost.getThreadRoot(), true);
            }

        }
    }
}
