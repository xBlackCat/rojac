package org.xblackcat.rojac.gui.view.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.gui.component.ShortCut;
import org.xblackcat.rojac.gui.view.MessageChecker;
import org.xblackcat.rojac.gui.view.message.AItemView;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.datahandler.IPacketProcessor;
import org.xblackcat.rojac.service.datahandler.IThreadsUpdatePacket;
import org.xblackcat.rojac.service.datahandler.SetForumReadPacket;
import org.xblackcat.rojac.service.datahandler.SetPostReadPacket;
import org.xblackcat.rojac.service.datahandler.SetThreadReadPacket;
import org.xblackcat.rojac.service.executor.IExecutor;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacWorker;
import org.xblackcat.rojac.util.ShortCutUtils;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * @author xBlackCat
 */

public abstract class AThreadView extends AItemView {
    private static final Log log = LogFactory.getLog(TreeThreadView.class);

    protected final IThreadControl<Post> threadControl;
    protected final AThreadModel<Post> model = new SortedThreadsModel();
    protected Forum forum;
    protected int forumId;

    protected AThreadView(IRootPane mainFrame, IThreadControl<Post> threadControl) {
        super(mainFrame);
        this.threadControl = threadControl;
    }

    protected void initializeLayout() {
        // Initialize tree
        JComponent threadsContainer = getThreadsContainer();
        JScrollPane sp = new JScrollPane(threadsContainer);
        add(sp, BorderLayout.CENTER);

        JButton newThreadButton = WindowsUtils.registerImageButton(this, "new_thread", new NewThreadAction());
        JButton prevUnreadButton = WindowsUtils.registerImageButton(this, "prev_unread", new PreviousUnreadAction());
        JButton nextUnreadButton = WindowsUtils.registerImageButton(this, "next_unread", new NextUnreadAction());

        JToolBar toolbar = WindowsUtils.createToolBar(newThreadButton, null, prevUnreadButton, nextUnreadButton);

        threadsContainer.setInputMap(
                WHEN_ANCESTOR_OF_FOCUSED_COMPONENT,
                ShortCutUtils.mergeInputMaps(
                        getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT),
                        threadsContainer.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                )
        );
        add(toolbar, BorderLayout.NORTH);
    }

    @Override
    public void loadItem(int forumId) {
        this.forumId = forumId;
        threadControl.fillModelByItemId(model, forumId);

        new ForumInfoLoader(forumId).execute();
    }

    @Override
    @SuppressWarnings({"unchecked"})
    protected IPacketProcessor<IPacket>[] getProcessors() {
        return new IPacketProcessor[]{
                new IPacketProcessor<SetForumReadPacket>() {
                    @Override
                    public void process(SetForumReadPacket p) {
                        if (p.getForumId() == forumId) {
                            threadControl.markForumRead(model, p.isRead());
                        }
                    }
                },
                new IPacketProcessor<SetThreadReadPacket>() {
                    @Override
                    public void process(SetThreadReadPacket p) {
                        if (p.getForumId() == forumId) {
                            threadControl.markThreadRead(model, p.getThreadId(), p.isRead());
                        }
                    }
                },
                new IPacketProcessor<SetPostReadPacket>() {
                    @Override
                    public void process(SetPostReadPacket p) {
                        if (p.getForumId() == forumId) {
                            threadControl.markPostRead(model, p.getPostId(), p.isRead());
                        }
                    }
                },
                new IPacketProcessor<IThreadsUpdatePacket>() {
                    @Override
                    public void process(IThreadsUpdatePacket p) {
                        for (int threadId : p.getThreadIds()) {
                            Thread t = model.getRoot().getMessageById(threadId).getThreadRoot();

                            threadControl.loadChildren(model, t, null);
                        }
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

//    private void updateMessages(ProcessPacket ids) {
//        AffectedMessage[] messageIds = (AffectedMessage[]) ArrayUtils.addAll(
//                ids.getAffectedMessages(forumId),
//                ids.getAffectedMessages(AffectedMessage.DEFAULT_FORUM)
//        );
//
//        Post currentPost = getSelectedItem();
//
//        threadControl.updateItem(model, messageIds);
//
//        selectItem(currentPost);
//    }

    protected abstract void selectItem(Post post);

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
            threadControl.loadChildren(model, post, new LoadNextUnread());
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
                            threadControl.loadChildren(model, p, new LoadNextUnread());
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
                threadControl.loadChildren(model, post, new LoadPreviousUnread());
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
                SetMessageReadFlag target = new SetMessageReadFlag(true, mi);
                IExecutor executor = ServiceFactory.getInstance().getExecutor();
                if (delay > 0) {
                    executor.setupTimer("Forum_" + forumId, target, delay);
                } else {
                    executor.execute(target);
                }
            }
        }
    }

    @Override
    public String getTabTitle() {
        if (forum == null) {
            return "#" + forumId;
        } else {
            return forum.getForumName();
        }
    }

    private class ForumInfoLoader extends RojacWorker<Void, Forum> {
        private final int forumId;

        public ForumInfoLoader(int forumId) {
            this.forumId = forumId;
        }

        @Override
        protected Void perform() throws Exception {
            IForumAH fah = ServiceFactory.getInstance().getStorage().getForumAH();

            try {
                publish(fah.getForumById(forumId));
            } catch (StorageException e) {
                log.error("Can not load forum information for forum id = " + forumId, e);
            }

            return null;
        }

        @Override
        protected void process(List<Forum> chunks) {
            for (Forum f : chunks) {
                forum = f;
                fireItemUpdated(forumId, null);
            }
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
                threadControl.loadChildren(model, root.getMessageById(data.getTopicId()), new IItemProcessor<Post>() {
                    @Override
                    public void processItem(Post item) {
                        selectItem(root.getMessageById(messageId));
                    }
                });
            } else {
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
            mainFrame.editMessage(forumId, null);
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
}
