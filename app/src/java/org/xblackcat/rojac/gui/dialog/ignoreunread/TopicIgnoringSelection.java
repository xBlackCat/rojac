package org.xblackcat.rojac.gui.dialog.ignoreunread;

import org.xblackcat.rojac.i18n.IDescribable;
import org.xblackcat.rojac.i18n.Message;

/**
 * 13.12.11 16:25
 *
 * @author xBlackCat
 */
public enum TopicIgnoringSelection implements IDescribable {
    TotallyUnread(Message.TopicIgnoringSelection_TotallyUnread),
    HaveUnread(Message.TopicIgnoringSelection_HaveUnread)
    ;
    private final Message label;

    private TopicIgnoringSelection(Message label) {
        this.label = label;
    }

    public Message getLabel() {
        return label;
    }
}
