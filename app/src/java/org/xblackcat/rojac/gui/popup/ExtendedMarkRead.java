package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.component.InvokeExtMarkDialog;
import org.xblackcat.rojac.gui.dialog.extendmark.Scope;
import org.xblackcat.rojac.gui.view.model.Post;
import org.xblackcat.rojac.i18n.Messages;

import javax.swing.*;

/**
 * @author xBlackCat
 */
class ExtendedMarkRead extends JMenuItem {
    public ExtendedMarkRead(Messages title, Post post, IAppControl appControl) {
        this(title, appControl, Scope.Thread, post.getMessageData().getMessageDate(), post.getForumId(), post.getThreadRoot().getMessageId());
    }

    public ExtendedMarkRead(Messages title, Forum forum, IAppControl appControl) {
        this(title, appControl, Scope.Forum, null, forum.getForumId(), 0);
    }

    private ExtendedMarkRead(
            Messages title,
            final IAppControl appControl,
            final Scope scope,
            final Long messageDate,
            final int forumId,
            final int topicId
    ) {
        super(title.get());

        addActionListener(new InvokeExtMarkDialog(messageDate, scope, forumId, topicId, appControl.getMainFrame()));
    }

}
