package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.theme.IconPack;
import org.xblackcat.rojac.gui.theme.ThreadIcon;
import org.xblackcat.rojac.gui.theme.ViewIcon;
import org.xblackcat.rojac.gui.view.MessageChecker;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.*;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.util.RojacUtils;

import javax.swing.*;

/**
 * Control class of all threads of the specified forum.
 *
 * @author xBlackCat
 */

public class SingleModelControl extends AThreadsModelControl {
    protected final IStorage storage = ServiceFactory.getInstance().getStorage();

    @Override
    public void fillModelByItemId(final AThreadModel<Post> model, int threadId) {
        assert RojacUtils.checkThread(true);

        MessageData fakeMessageData = new MessageData(threadId, 0, 0, 0, 0, null, "Loading...", 0L, 0L, false, null);
        final Thread rootItem = new Thread(fakeMessageData, null);

        model.setRoot(rootItem);

        new MessageChecker(threadId) {
            @Override
            protected void done() {
                rootItem.setMessageData(data);
                model.nodeChanged(rootItem);


                rootItem.setLoadingState(LoadingState.Loading);

                new ThreadLoader(rootItem, model, null) {
                    @Override
                    protected void done() {
                        super.done();

                        model.fireResortModel();
                    }
                }.execute();
            }
        }.execute();
    }

    private void markForumRead(AThreadModel<Post> model, boolean read) {
        assert RojacUtils.checkThread(true);

        // Root post is Thread object
        model.getRoot().setDeepRead(read);

        model.subTreeNodesChanged(model.getRoot());
    }


    @Override
    public boolean isRootVisible() {
        assert RojacUtils.checkThread(true);

        return true;
    }

    @Override
    public String getTitle(AThreadModel<Post> model) {
        assert RojacUtils.checkThread(true);

        // Root is Thread object
        Post root = model.getRoot();
        if (root != null) {
            if (root.getMessageData() != null) {
                if (root.getMessageData().getSubject() != null) {
                    return root.getMessageData().getSubject();
                }
            }
            return "#" + root.getMessageId();
        }

        return "#";
    }

    @Override
    public boolean processPacket(final AThreadModel<Post> model, IPacket p) {
        final int forumId = model.getRoot().getForumId();
        final int threadId = model.getRoot().getMessageId();

        new PacketDispatcher(
                new IPacketProcessor<SetForumReadPacket>() {
                    @Override
                    public void process(SetForumReadPacket p) {
                        if (p.getForumId() == forumId) {
                            markForumRead(model, p.isRead());
                        }
                    }
                },
                new IPacketProcessor<SetPostReadPacket>() {
                    @Override
                    public void process(SetPostReadPacket p) {
                        if (p.getForumId() == forumId) {
                            if (p.isRecursive()) {
                                // Post is a root of marked thread
                                markThreadRead(model, p.getPostId(), p.isRead());
                            } else {
                                // Mark as read only the post
                                markPostRead(model, p.getPostId(), p.isRead());
                            }
                        }
                    }
                },
                new IPacketProcessor<SynchronizationCompletePacket>() {
                    @Override
                    public void process(SynchronizationCompletePacket p) {
                        if (!p.isForumAffected(forumId)) {
                            // Current forum is not changed - have a rest
                            return;
                        }
                        if (!p.isTopicAffected(threadId)) {
                            return;
                        }

                        // Thread always filled in.
                        loadThread(model, model.getRoot(), null);
                    }
                }
        ).dispatch(p);
        return true;
    }

    @Override
    public Icon getTitleIcon(AThreadModel<Post> model) {
        ThreadIcon threadIcon = null;

        Post root = model.getRoot();

        if (root != null) {
            switch (root.isRead()) {
                case Read:
                    threadIcon = ThreadIcon.Read;
                    break;
                case ReadPartially:
                    threadIcon = ThreadIcon.ReadPartially;
                    break;
                case Unread:
                    threadIcon = ThreadIcon.Unread;
                    break;
            }

            if (threadIcon != ThreadIcon.Read) {
                // Check for non-read replies.

                int userId = Property.RSDN_USER_ID.get(0);
                if (userId > 0 && hasUnreadReplies(userId, root)) {
                    threadIcon = ThreadIcon.HasResponseUnread;
                }
            }
        }

        IconPack imagePack = Property.ROJAC_GUI_ICONPACK.get();
        return imagePack.getIcon(threadIcon);
    }

    @Override
    public JPopupMenu getTitlePopup(AThreadModel<Post> postAThreadModel, IAppControl appControl) {
        return null;
    }

    private boolean hasUnreadReplies(int userId, Post post) {
        if (post.getMessageData().getUserId() == userId && post.isRead() != ReadStatus.Unread) {
            return true;
        }

        for (Post p : post.getChildren()) {
            if (hasUnreadReplies(userId, p)) {
                return true;
            }
        }

        return false;
    }
}
