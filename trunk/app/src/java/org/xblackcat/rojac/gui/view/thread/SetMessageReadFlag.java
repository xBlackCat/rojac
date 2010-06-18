package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.PacketType;
import org.xblackcat.rojac.service.datahandler.ProcessPacket;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.ArrayList;
import java.util.Collection;


/**
 * @author xBlackCat
 */
public class SetMessageReadFlag extends RojacWorker<Void, Void> {
    private Post[] posts;
    protected boolean read;

    public SetMessageReadFlag(boolean read, Post... posts) {
        this.posts = posts;
        this.read = read;
    }

    @Override
    protected Void perform() throws Exception {
        IStorage storage = ServiceFactory.getInstance().getStorage();
        for (Post p : posts) {
            storage.getMessageAH().updateMessageReadFlag(p.getMessageId(), read);
        }
        return null;
    }

    @Override
    protected void done() {
        Collection<Post> affectedPosts = new ArrayList<Post>(posts.length);
        for (Post p : posts) {
            // Fire event only if post read state is differ than new post state.
            // Just in case.
            if ((p.isRead() == ReadStatus.Unread) == read) {
                p.setRead(read);
                affectedPosts.add(p);
            }
        }

        ProcessPacket processPacket = new ProcessPacket(
                read ? PacketType.SetPostRead : PacketType.SetPostUnread,
                affectedPosts.toArray(new Post[affectedPosts.size()])
        );
        ServiceFactory.getInstance().getDataDispatcher().processPacket(processPacket);
    }
}
