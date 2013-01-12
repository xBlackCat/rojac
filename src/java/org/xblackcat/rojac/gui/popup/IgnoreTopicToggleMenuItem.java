package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.i18n.Message;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author xBlackCat
 */
class IgnoreTopicToggleMenuItem extends JMenuItem {
    public IgnoreTopicToggleMenuItem(MessageData topic) {
        final int topicId = topic.getMessageId();
        final int forumId = topic.getForumId();
        final boolean ignored = topic.isIgnored();

        setText(ignored ? Message.Popup_Ignore_Reset.get() : Message.Popup_Ignore_Set.get());
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TopicIgnoreSetter(ignored, topicId, forumId).execute();
            }
        });
    }

}
