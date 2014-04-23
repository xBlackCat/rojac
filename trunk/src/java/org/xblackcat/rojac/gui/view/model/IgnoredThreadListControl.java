package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.OpenMessageMethod;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.gui.theme.ReadStatusIcon;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.datahandler.IgnoreUpdatedPacket;
import org.xblackcat.rojac.service.datahandler.IgnoreUserUpdatedPacket;
import org.xblackcat.rojac.service.datahandler.OptionsUpdatedPacket;
import org.xblackcat.rojac.service.executor.TaskType;
import org.xblackcat.rojac.service.executor.TaskTypeEnum;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.RojacWorker;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author xBlackCat
 */
class IgnoredThreadListControl extends AMessageListControl {
    /**
     * Creates a post list control. All posts a linked with the specified user. If parameter is <code>true</code> - the
     * control loads all replies on the user posts. If parameter is <code>false</code> - the control loads all posts of
     * the user.
     */
    public IgnoredThreadListControl() {
    }

    public void fillModelByItemId(final SortedThreadsModel model, final int itemId) {
        final IgnoredThreadList root = new IgnoredThreadList();
        model.setRoot(root);

        updateModel(model, null);
    }

    @Override
    public JPopupMenu getTitlePopup(SortedThreadsModel model, IAppControl appControl) {
        return null;
    }

    @Override
    public ViewMode getViewMode() {
        return ViewMode.Normal;
    }

    @Override
    public Icon getTitleIcon(SortedThreadsModel model) {
        return ReadStatusIcon.IgnoredThreads.getIcon(ReadStatus.Read);
    }

    @Override
    public void onDoubleClick(Post post, IAppControl appControl) {
        appControl.openMessage(post.getMessageId(), OpenMessageMethod.InThread);
    }

    protected void updateModel(final SortedThreadsModel model, Runnable postProcessor) {
        assert RojacUtils.checkThread(true);

        new PostListLoader(postProcessor, model).execute();
    }

    @Override
    public String getTitle(SortedThreadsModel model) {
        return Message.View_Navigation_Item_Ignored.get();
    }

    public JPopupMenu getItemMenu(Post post, IAppControl appControl) {
        return PopupMenuBuilder.getIgnoredListMenu(post, appControl);
    }

    @Override
    public void processPacket(final SortedThreadsModel model, IPacket p, final Runnable postProcessor) {
        if (p instanceof IgnoreUpdatedPacket) {
            updateModel(model, postProcessor);
        } else if (p instanceof OptionsUpdatedPacket) {
            OptionsUpdatedPacket updatedPacket = (OptionsUpdatedPacket) p;
            if (updatedPacket.isPropertyAffected(Property.SKIP_IGNORED_USER_REPLY) ||
                    updatedPacket.isPropertyAffected(Property.SKIP_IGNORED_USER_THREAD)) {
                model.subTreeNodesChanged(model.getRoot());
            }
        } else if (p instanceof IgnoreUserUpdatedPacket) {
            IgnoreUserUpdatedPacket up = (IgnoreUserUpdatedPacket) p;
            PostUtils.setIgnoreUserFlag(model, up.getUserId(), up.isIgnored());
        }
    }

    @TaskType(TaskTypeEnum.Initialization)
    private class PostListLoader extends RojacWorker<Void, Void> {
        private Collection<MessageData> messages = new ArrayList<>();
        private final SortedThreadsModel model;

        public PostListLoader(Runnable postProcessor, SortedThreadsModel model) {
            super(postProcessor);
            this.model = model;
        }

        @Override
        protected Void perform() throws Exception {
            messages.addAll(Storage.get(IMessageAH.class).getIgnoredTopicsList());
            return null;
        }

        @Override
        protected void done() {
            IgnoredThreadList root = (IgnoredThreadList) model.getRoot();

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
