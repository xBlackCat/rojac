package org.xblackcat.rojac.gui.view.thread;

import gnu.trove.set.hash.TIntHashSet;
import org.xblackcat.rojac.gui.view.model.ITreeItem;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.datahandler.SetReadExPacket;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.util.RojacWorker;

/**
 * @author xBlackCat
 */
public class SubTreeReadFlagSetter extends RojacWorker<Void, Void> {
    private final boolean read;
    private final TIntHashSet messageIds;
    private final int threadId;
    private final int forumId;

    public SubTreeReadFlagSetter(boolean read, ITreeItem<?> post) {
        this.read = read;
        messageIds = new TIntHashSet();
        threadId = post.getTopicId();
        forumId = post.getForumId();

        fillMessageIds(post);
    }

    private void fillMessageIds(ITreeItem<?> post) {
        messageIds.add(post.getMessageId());

        int i = 0;
        int childrenLength = post.getSize();
        while (i < childrenLength) {
            ITreeItem<?> p = post.getChild(i);
            fillMessageIds(p);
            i++;
        }
    }

    @Override
    protected Void perform() throws Exception {
        IStorage storage = ServiceFactory.getInstance().getStorage();
        for (int postId : messageIds.toArray()) {
            storage.getMessageAH().updateMessageReadFlag(postId, read);
        }
        return null;
    }

    @Override
    protected void done() {
        IPacket packet = new SetReadExPacket(forumId, threadId, messageIds, read);
        ServiceFactory.getInstance().getDataDispatcher().processPacket(packet);
    }
}
