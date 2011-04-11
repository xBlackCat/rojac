package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.dialog.extendmark.ExtendedMarkDialog;
import org.xblackcat.rojac.gui.dialog.extendmark.Scope;
import org.xblackcat.rojac.gui.view.model.Post;
import org.xblackcat.rojac.gui.view.thread.SetMessagesReadFlagEx;
import org.xblackcat.rojac.i18n.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author xBlackCat
 */
class ExtendedMarkRead extends JMenuItem {
    private final IAppControl rootPane;

    public ExtendedMarkRead(Messages title, final Post post, IAppControl mainFrame) {
        super(title.get());
        rootPane = mainFrame;

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExtendedMarkDialog emd = new ExtendedMarkDialog((Window) rootPane);

                if (!emd.selectDate(post.getMessageData().getMessageDate(), Scope.Thread)) {
                    return;
                }

                new SetMessagesReadFlagEx(
                        emd.getReadStatus(),
                        emd.getDateDirection(),
                        emd.getSelectedDate(),
                        post.getForumId(),
                        post.getTopicId(),
                        Scope.Thread
                ).execute(); 
            }
        });
    }
}
