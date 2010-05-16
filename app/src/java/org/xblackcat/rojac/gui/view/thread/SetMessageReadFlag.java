package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.PacketType;
import org.xblackcat.rojac.service.datahandler.ProcessPacket;
import org.xblackcat.rojac.service.janus.commands.AffectedMessage;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacWorker;

/** @author xBlackCat */
public class SetMessageReadFlag extends RojacWorker<Void, Void> {
    private Post post;
    protected boolean read;
    private final int forumId;

    public SetMessageReadFlag(Post post, boolean read) {
        this.post = post;
        this.read = read;
        this.forumId = post.getForumId() == 0 ? post.getThreadRoot().getForumId() : post.getForumId();

        if (forumId == 0) {
            throw new IllegalArgumentException("Message #" + post.getMessageId() + " has not linked to any forums.");
        }
    }

    @Override
    protected Void perform() throws Exception {
        IStorage storage = ServiceFactory.getInstance().getStorage();
        try {
            storage.getMessageAH().updateMessageReadFlag(post.getMessageId(), read);
        } catch (StorageException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void done() {
        if (post != null) {
            // Fire event only if post read state is differ than new post state.
            // Just in case.
            if ((post.isRead() == ReadStatus.Unread) == read) { 
                post.setRead(read);

                ProcessPacket processPacket = new ProcessPacket(
                        read ? PacketType.SetPostRead : PacketType.SetPostUnread,
                        new AffectedMessage(forumId, post.getMessageId())
                );
                ServiceFactory.getInstance().getDataDispatcher().processPacket(processPacket);
            }
        }
    }
}
