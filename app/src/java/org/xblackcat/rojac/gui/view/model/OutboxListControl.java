package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.data.NewMessage;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.gui.theme.ReadStatusIcon;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.datahandler.*;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.INewMessageAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.RojacWorker;

import javax.swing.*;
import java.util.Collection;

/**
 * @author xBlackCat
 */
class OutboxListControl extends MessageListControl {
    /**
     * Creates a post list control. All posts a linked with the specified user. If parameter is <code>true</code> - the
     * control loads all replies on the user posts. If parameter is <code>false</code> - the control loads all posts of
     * the user.
     */
    public OutboxListControl() {
    }

    public void fillModelByItemId(final AThreadModel<Post> model, final int itemId) {
        final OutboxPostList root = new OutboxPostList();
        model.setRoot(root);

        updateModel(model, null);
    }

    @Override
    public JPopupMenu getTitlePopup(AThreadModel<Post> model, IAppControl appControl) {
        return null;
    }

    @Override
    public Icon getTitleIcon(AThreadModel<Post> model) {
        if (model.getRoot() != null) {
            return ReadStatusIcon.OutboxItem.getIcon(model.getRoot().isRead());
        }

        return null;
    }

    @Override
    public void onDoubleClick(Post post, IAppControl appControl) {
        appControl.editMessage(null, -post.getMessageId());
    }

    protected void updateModel(final AThreadModel<Post> model, Runnable postProcessor) {
        assert RojacUtils.checkThread(true);

        new PostListLoader(postProcessor, model).execute();
    }

    @Override
    public String getTitle(AThreadModel<Post> model) {
        return Message.View_Navigation_Item_Outbox.get();
    }

    public JPopupMenu getItemMenu(Post post, IAppControl appControl) {
        return PopupMenuBuilder.getNewMessageListMenu(post, appControl);
    }

    @Override
    public void processPacket(final AThreadModel<Post> model, IPacket p, final Runnable postProcessor) {
        new PacketDispatcher(
                new IPacketProcessor<OptionsUpdatedPacket>() {
                    @Override
                    public void process(OptionsUpdatedPacket p) {
                        if (p.isPropertyAffected(Property.SKIP_IGNORED_USER_REPLY) ||
                                p.isPropertyAffected(Property.SKIP_IGNORED_USER_THREAD)) {
                            model.subTreeNodesChanged(model.getRoot());
                        }
                    }
                },
                new IPacketProcessor<NewMessagesUpdatedPacket>() {
                    @Override
                    public void process(NewMessagesUpdatedPacket p) {
                        updateModel(model, postProcessor);
                    }
                },
                new IPacketProcessor<SynchronizationCompletePacket>() {
                    @Override
                    public void process(SynchronizationCompletePacket p) {
                        updateModel(model, postProcessor);
                    }
                }
        ).dispatch(p);
    }

    private class PostListLoader extends RojacWorker<Void, Void> {
        private Collection<NewMessage> messages;
        private final AThreadModel<Post> model;

        public PostListLoader(Runnable postProcessor, AThreadModel<Post> model) {
            super(postProcessor);
            this.model = model;
        }

        @Override
        protected Void perform() throws Exception {
            messages = Storage.get(INewMessageAH.class).getAllNewMessages();
            return null;
        }

        @Override
        protected void done() {
            OutboxPostList root = (OutboxPostList) model.getRoot();

            root.fillList(messages);
            root.setLoadingState(LoadingState.Loaded);
            model.markInitialized();
            model.nodeChanged(root);
            model.nodeStructureChanged(root);
            model.fireResortModel();
            super.done();
        }
    }
}
