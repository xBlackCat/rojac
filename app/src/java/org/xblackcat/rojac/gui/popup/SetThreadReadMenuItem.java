package org.xblackcat.rojac.gui.popup;

import gnu.trove.TIntHashSet;
import org.xblackcat.rojac.gui.view.thread.ITreeItem;
import org.xblackcat.rojac.gui.view.thread.Thread;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.datahandler.SetPostReadPacket;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.util.RojacWorker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author xBlackCat
 */
class SetThreadReadMenuItem extends JMenuItem {
    public SetThreadReadMenuItem(Messages text, final ITreeItem<?> post, final boolean read) {
        super(text.get());
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (post instanceof Thread) {
                    // Mark whole thread.
                    new ThreadReadFlagSetter(read, (Thread) post).execute();
                } else {
                    // Mark sub-tree as read
                    new SubTreeReadFlagSetter(read, post).execute();
                }
            }
        });
    }

    private class ThreadReadFlagSetter extends RojacWorker<Void, Void> {
        private final boolean read;
        private final Thread threadRoot;

        public ThreadReadFlagSetter(boolean read, Thread threadRoot) {
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
            IPacket packet = new SetPostReadPacket(read, threadRoot.getForumId(), threadRoot.getMessageId(), true);
            ServiceFactory.getInstance().getDataDispatcher().processPacket(packet);
        }
    }

    private class SubTreeReadFlagSetter extends RojacWorker<Void, Void> {
        private final boolean read;
        private final int forumId;
        private final int rootId;
        private final TIntHashSet messageIds;

        public SubTreeReadFlagSetter(boolean read, ITreeItem<?> post) {
            this.read = read;
            rootId = post.getMessageId();
            forumId = post.getForumId();
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
            IPacket packet = new SetPostReadPacket(read, forumId, rootId, true);
            ServiceFactory.getInstance().getDataDispatcher().processPacket(packet);
        }
    }
}
