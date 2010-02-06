package org.xblackcat.rojac.gui.view.thread;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.view.message.AItemView;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ProcessPacket;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.janus.commands.AffectedMessage;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacWorker;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author xBlackCat
 */

public abstract class AThreadView extends AItemView {
    private static final Log log = LogFactory.getLog(TreeThreadView.class);

    protected final IThreadControl threadControl;
    protected final JLabel forumName = new JLabel();
    protected final AThreadModel<Post> model = new SortedThreadsModel();
    protected int forumId;

    protected AThreadView(IRootPane mainFrame, IThreadControl threadControl) {
        super(mainFrame);
        this.threadControl = threadControl;
    }

    protected void initializeLayout() {
        // Initialize tree
        add(forumName, BorderLayout.NORTH);

        JPanel internalPane = new JPanel(new BorderLayout(0, 0));

        JComponent threadsContainer = getThreadsContainer();
        JScrollPane sp = new JScrollPane(threadsContainer);
        internalPane.add(sp, BorderLayout.CENTER);

        JButton newThreadButton = WindowsUtils.setupImageButton("new_thread", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.editMessage(forumId, null);
            }
        }, Messages.VIEW_THREAD_BUTTON_NEW_THREAD);
        JToolBar toolbar = WindowsUtils.createToolBar(newThreadButton);
        internalPane.add(toolbar, BorderLayout.NORTH);

        add(internalPane, BorderLayout.CENTER);
    }

    protected void loadForumInfo(final int forumId) {
        this.forumId = forumId;

        executor.execute(new ForumInfoLoader(forumId));
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void loadItem(AffectedMessage itemId) {
        int forumId = threadControl.loadThreadByItem(model, itemId);
        loadForumInfo(forumId);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void processPacket(ProcessPacket ids) {
        if (ids.containsForum(forumId)) {
            switch (ids.getType()) {
                case ForumsLoaded:
                    // Do nothing
                    break;
                case AddMessages:
                case UpdateMessages:
                    updateMessages(ids);
                    break;
                case SetForumRead:
                    threadControl.markForumRead(model, true);
                    break;
                case SetForumUnread:
                    threadControl.markForumRead(model, false);
                    break;
                case SetThreadRead:
                    for (AffectedMessage threadRootId : ids.getAffectedMessages(forumId)) {
                        threadControl.markThreadRead(model, threadRootId.getMessageId(), true);
                    }
                    break;
                case SetThreadUnread:
                    for (AffectedMessage threadRootId : ids.getAffectedMessages(forumId)) {
                        threadControl.markThreadRead(model, threadRootId.getMessageId(), false);
                    }
                    break;
                case SetPostRead:
                    for (AffectedMessage postId : ids.getAffectedMessages(forumId)) {
                        threadControl.markPostRead(model, postId.getMessageId(), true);
                    }
                    break;
                case SetPostUnread:
                    for (AffectedMessage postId : ids.getAffectedMessages(forumId)) {
                        threadControl.markPostRead(model, postId.getMessageId(), false);
                    }
                    break;
            }
        }
    }

    private void updateMessages(ProcessPacket ids) {
        AffectedMessage[] messageIds = (AffectedMessage[]) ArrayUtils.addAll(
                ids.getAffectedMessages(forumId),
                ids.getAffectedMessages(AffectedMessage.DEFAULT_FORUM)
        );

        Post currentPost = getSelectedItem();

        threadControl.updateItem(model, messageIds);

        selectItem(currentPost);
    }

    protected abstract void selectItem(Post post);

    protected abstract Post getSelectedItem();

    protected abstract JComponent getThreadsContainer();

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
                forumName.setText(f.getForumName() + "/" + f.getShortForumName());
            }
        }
    }
}
