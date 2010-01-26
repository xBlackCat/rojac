package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.service.PacketType;
import org.xblackcat.rojac.service.ProcessPacket;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.janus.commands.AffectedMessage;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacWorker;

/** @author xBlackCat */
class SetMessageReadFlag extends RojacWorker<Void, Void> {
    private Post post;
    private IRootPane mainFrame;

    public SetMessageReadFlag(Post post, IRootPane mainFrame) {
        this.post = post;
        this.mainFrame = mainFrame;
    }

    @Override
    protected Void perform() throws Exception {
        IStorage storage = ServiceFactory.getInstance().getStorage();
        try {
            storage.getMessageAH().updateMessageReadFlag(post.getMessageId(), true);
        } catch (StorageException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void done() {
        if (post != null) {
            post.setRead(true);

            ProcessPacket processPacket = new ProcessPacket(
                    PacketType.SetReadPost,
                    new AffectedMessage(post.getForumId(), post.getMessageId())
            );
            mainFrame.processPacket(processPacket);
        }
    }
}
