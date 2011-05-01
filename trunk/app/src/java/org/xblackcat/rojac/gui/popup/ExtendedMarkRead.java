package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.gui.component.InvokeExtMarkDialog;
import org.xblackcat.rojac.gui.dialog.extendmark.Scope;
import org.xblackcat.rojac.i18n.Messages;

import javax.swing.*;
import java.awt.*;

/**
 * @author xBlackCat
 */
class ExtendedMarkRead extends JMenuItem {
    public ExtendedMarkRead(Messages title, MessageData messageData, Window owner) {
        this(
                title,
                Scope.Thread,
                messageData.getMessageDate(),
                messageData.getForumId(),
                messageData.getThreadRootId(), owner
        );
    }

    public ExtendedMarkRead(Messages title, Forum forum, Window owner) {
        this(title, Scope.Forum, null, forum.getForumId(), 0, owner);
    }

    private ExtendedMarkRead(
            Messages title,
            Scope scope,
            Long messageDate,
            int forumId,
            int topicId,
            Window owner
    ) {
        super(title.get());

        addActionListener(new InvokeExtMarkDialog(messageDate, scope, forumId, topicId, owner));
    }

}
