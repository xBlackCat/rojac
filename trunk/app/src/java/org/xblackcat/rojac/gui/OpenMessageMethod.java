package org.xblackcat.rojac.gui;

import org.xblackcat.rojac.i18n.IDescribable;
import org.xblackcat.rojac.i18n.Message;

/**
 * @author xBlackCat
 */

public enum OpenMessageMethod implements IDescribable {
    /**
     * Open a new tab with message view and show the message there.
     */
    NewTab(Message.OpenMessageMethod_NewTab),
    InThread(Message.OpenMessageMethod_InThread),
    InForum(Message.OpenMessageMethod_InForum),
    ;
    private final Message label;

    OpenMessageMethod(Message label) {
        this.label = label;
    }

    @Override
    public Message getLabel() {
        return label;
    }
}
