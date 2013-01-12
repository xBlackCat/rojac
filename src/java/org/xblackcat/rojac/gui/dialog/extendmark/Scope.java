package org.xblackcat.rojac.gui.dialog.extendmark;

import org.xblackcat.rojac.i18n.IDescribable;
import org.xblackcat.rojac.i18n.Message;

/**
 * @author xBlackCat
 */
public enum Scope implements IDescribable {
    All(Message.Dialog_ExtMark_Scope_All),
    Forum(Message.Dialog_ExtMark_Scope_Forum),
    Thread(Message.Dialog_ExtMark_Scope_Thread);

    private final Message msg;

    Scope(Message msg) {
        this.msg = msg;
    }

    @Override
    public Message getLabel() {
        return msg;
    }
}
