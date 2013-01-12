package org.xblackcat.rojac.gui;

import org.xblackcat.rojac.gui.view.ViewType;
import org.xblackcat.rojac.i18n.IDescribable;
import org.xblackcat.rojac.i18n.Message;

/**
 * @author xBlackCat
 */

public enum OpenMessageMethod implements IDescribable {
    /**
     * Open a new tab with message view and show the message there.
     */
    NewTab(Message.OpenMessageMethod_MessageInTab, ViewType.SingleMessage),
    InThread(Message.OpenMessageMethod_InThread, ViewType.SingleThread),
    InForum(Message.OpenMessageMethod_InForum, ViewType.Forum),
    ;
    private final Message label;
    private final ViewType associatedViewType;

    OpenMessageMethod(Message label, ViewType associatedViewType) {
        this.label = label;
        this.associatedViewType = associatedViewType;
    }

    public ViewType getAssociatedViewType() {
        return associatedViewType;
    }

    @Override
    public Message getLabel() {
        return label;
    }
}
