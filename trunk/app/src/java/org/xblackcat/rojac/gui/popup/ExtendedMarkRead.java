package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.dialog.extendmark.ExtendedMarkDialog;
import org.xblackcat.rojac.gui.dialog.extendmark.Scope;
import org.xblackcat.rojac.gui.view.model.Post;
import org.xblackcat.rojac.gui.view.thread.SetMessagesReadFlagEx;
import org.xblackcat.rojac.i18n.Messages;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author xBlackCat
 */
class ExtendedMarkRead extends JMenuItem {
    private final IAppControl appControl;
    private final Scope scope = Scope.Thread;

    public ExtendedMarkRead(Messages title, Post post, IAppControl appControl) {
        this(title, appControl, Scope.Thread, post.getMessageData().getMessageDate(), post.getForumId(), post.getTopicId());
    }

    public ExtendedMarkRead(Messages title, Forum forum, IAppControl appControl) {
        this(title, appControl, Scope.Forum, null, forum.getForumId(), 0);
    }

    private ExtendedMarkRead(
            Messages title,
            final IAppControl appControl,
            final Scope thread,
            final Long messageDate,
            final int forumId,
            final int topicId
    ) {
        super(title.get());
        this.appControl = appControl;

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExtendedMarkDialog emd = new ExtendedMarkDialog(appControl.getMainFrame());

                if (!emd.selectDate(messageDate, scope)) {
                    return;
                }

                new SetMessagesReadFlagEx(
                        emd.getReadStatus(),
                        emd.getDateDirection(),
                        emd.getSelectedDate(),
                        forumId,
                        topicId,
                        emd.getScope()
                ).execute();
            }
        });
    }
}
