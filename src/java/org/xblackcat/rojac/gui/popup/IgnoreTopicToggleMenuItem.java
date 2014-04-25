package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.i18n.Message;

import javax.swing.*;

/**
 * @author xBlackCat
 */
class IgnoreTopicToggleMenuItem extends JMenuItem {
    public IgnoreTopicToggleMenuItem(MessageData topic) {
        final int topicId = topic.getMessageId();
        final int forumId = topic.getForumId();
        final boolean ignored = topic.isIgnored();

        setText(ignored ? Message.Popup_Ignore_Reset.get() : Message.Popup_Ignore_Set.get());
        addActionListener(e -> new TopicIgnoreSetter(ignored, topicId, forumId).execute());
    }

}
