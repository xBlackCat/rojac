package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.view.thread.Post;
import org.xblackcat.rojac.gui.view.thread.SetMessageReadFlag;
import org.xblackcat.rojac.gui.view.thread.Thread;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.PacketType;
import org.xblackcat.rojac.service.datahandler.ProcessPacket;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.util.RojacWorker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

/**
 * @author xBlackCat
 */
class SetThreadReadMenuItem extends JMenuItem {
    public SetThreadReadMenuItem(Messages text, final Post post, final boolean read) {
        super(text.get());
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread threadRoot = post.getThreadRoot();

                if (threadRoot.equals(post)) {
                    // Mark whole thread.
                    new SetThreadReadFlag(read, threadRoot).execute();
                } else {
                    // Collect sub-items to be marked.
                    // All sub-items already loaded, so just mark them as read/unread.
                    Collection<Post> posts = post.getChildren();
                    posts.add(post);
                    new SetMessageReadFlag(read, posts.toArray(new Post[posts.size()])).execute();
                }
            }
        });
    }

    private class SetThreadReadFlag extends RojacWorker<Void, Void> {
        private final boolean read;
        private final Thread threadRoot;

        public SetThreadReadFlag(boolean read, Thread threadRoot) {
            this.read = read;
            this.threadRoot = threadRoot;
        }

        @Override
        protected Void perform() throws Exception {
            IStorage storage = ServiceFactory.getInstance().getStorage();
            storage.getMessageAH().updateThreadReadFlag(threadRoot.getMessageId(), read);
            return null;
        }

        @Override
        protected void done() {
            ProcessPacket processPacket = new ProcessPacket(
                    read ? PacketType.SetThreadRead: PacketType.SetThreadUnread,
                    threadRoot
            );
            ServiceFactory.getInstance().getDataDispatcher().processPacket(processPacket);
        }
    }
}
