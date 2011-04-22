package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.view.MessageChecker;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.*;
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

                loadThread(model, rootItem, null);
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
        if (root.getMessageData() != null && root.getMessageData().getSubject() != null) {
            return root.getMessageData().getSubject();
        } else {
            return "#" + root.getMessageId();
        }
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
    public Icon getTitleIcon(AThreadModel<Post> postAThreadModel) {
        return null;
    }

    @Override
    public JPopupMenu getTitlePopup(AThreadModel<Post> postAThreadModel, IAppControl appControl) {
        return null;
    }
}