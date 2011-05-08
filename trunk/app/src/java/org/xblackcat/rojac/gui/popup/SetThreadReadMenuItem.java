package org.xblackcat.rojac.gui.popup;

import gnu.trove.TIntHashSet;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.gui.view.model.ITreeItem;
import org.xblackcat.rojac.gui.view.model.Post;
import org.xblackcat.rojac.gui.view.model.Thread;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.datahandler.SetPostReadPacket;
import org.xblackcat.rojac.service.datahandler.SetReadExPacket;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.util.RojacWorker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author xBlackCat
 */
class SetThreadReadMenuItem extends JMenuItem {
    public SetThreadReadMenuItem(Messages text, final Post post, final boolean read) {
        super(text.get());
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (post instanceof Thread) {
                    // Mark whole thread.
                    new ThreadReadFlagSetter(read, post.getMessageData()).execute();
                } else {
                    // Mark sub-tree as read
                    new SubTreeReadFlagSetter(read, post).execute();
                }
            }
        });
    }

    public SetThreadReadMenuItem(Messages text, final MessageData data, final boolean read) {
        super(text.get());
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mark whole thread.
                new ThreadReadFlagSetter(read, data).execute();
            }
        });
    }

    private class ThreadReadFlagSetter extends RojacWorker<Void, Void> {
        private final boolean read;
        private final MessageData threadRoot;

        public ThreadReadFlagSetter(boolean read, MessageData messageData) {
            this.read = read;
            this.threadRoot = messageData;
        }

        @Override
        protected Void perform() throws Exception {
            IStorage storage = ServiceFactory.getInstance().getStorage();
            storage.getMessageAH().updateThreadReadFlag(threadRoot.getMessageId(), read);
            return null;
        }

        @Override
        protected void done() {
            IPacket packet = new SetPostReadPacket(read, threadRoot.getForumId(), threadRoot.getMessageId(), true);
            ServiceFactory.getInstance().getDataDispatcher().processPacket(packet);
        }
    }

    private class SubTreeReadFlagSetter extends RojacWorker<Void, Void> {
        private final boolean read;
        private final TIntHashSet messageIds;

        public SubTreeReadFlagSetter(boolean read, ITreeItem<?> post) {
            this.read = read;
            messageIds = new TIntHashSet();

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
            IPacket packet = new SetReadExPacket(read, messageIds);
            ServiceFactory.getInstance().getDataDispatcher().processPacket(packet);
        }
    }
}
