package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.gui.component.InvokeExtMarkDialogAction;
import org.xblackcat.rojac.gui.dialog.extendmark.Scope;
import org.xblackcat.rojac.i18n.Message;

import javax.swing.*;
import java.awt.*;

/**
 * @author xBlackCat
 */
class ExtendedMarkRead extends JMenuItem {
    public ExtendedMarkRead(Message title, MessageData messageData, Window owner) {
        this(
                title,
                Scope.Thread,
                messageData.getMessageDate(),
                messageData.getForumId(),
                messageData.getThreadRootId(), owner
        );
    }

    public ExtendedMarkRead(Message title, Forum forum, Window owner) {
        this(title, Scope.Forum, null, forum.getForumId(), 0, owner);
    }

    public ExtendedMarkRead(Message title, Window owner) {
        this(title, Scope.All, null, 0, 0, owner);
    }

    private ExtendedMarkRead(
            Message title,
            Scope scope,
            Long messageDate,
            int forumId,
            int topicId,
            Window owner
    ) {
        super(title.get());

        addActionListener(new InvokeExtMarkDialogAction(messageDate, scope, forumId, topicId, owner));
    }

}
