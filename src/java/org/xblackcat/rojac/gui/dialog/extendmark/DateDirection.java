package org.xblackcat.rojac.gui.dialog.extendmark;

import org.xblackcat.rojac.i18n.IDescribable;
import org.xblackcat.rojac.i18n.Message;

/**
 * @author xBlackCat
 */

public enum DateDirection implements IDescribable {
    Before(Message.Dialog_ExtMark_DateDirection_Before),
    After(Message.Dialog_ExtMark_DateDirection_After);

    private final Message msg;

    DateDirection(Message msg) {
        this.msg = msg;
    }

    @Override
    public Message getLabel() {
        return msg;
    }

}
