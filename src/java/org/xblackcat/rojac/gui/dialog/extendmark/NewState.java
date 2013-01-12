package org.xblackcat.rojac.gui.dialog.extendmark;

import org.xblackcat.rojac.i18n.IDescribable;
import org.xblackcat.rojac.i18n.Message;

/**
 * @author xBlackCat
 */

public enum NewState implements IDescribable {
    Read(Message.Dialog_ExtMark_State_Read),
    Unread(Message.Dialog_ExtMark_State_Unread);

    private final Message msg;

    NewState(Message msg) {
        this.msg = msg;
    }

    @Override
    public Message getLabel() {
        return msg;
    }

}
