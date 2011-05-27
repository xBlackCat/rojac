package org.xblackcat.rojac.gui.dialog.extendmark;

import org.xblackcat.rojac.i18n.IDescribable;
import org.xblackcat.rojac.i18n.Messages;

/**
 * @author xBlackCat
 */

public enum NewState implements IDescribable {
    Read(Messages.Dialog_ExtMark_State_Read),
    Unread(Messages.Dialog_ExtMark_State_Unread);

    private final Messages msg;

    NewState(Messages msg) {
        this.msg = msg;
    }

    @Override
    public Messages getLabel() {
        return msg;
    }

}
