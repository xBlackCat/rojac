package org.xblackcat.rojac.gui.dialog.extendmark;

import org.xblackcat.rojac.i18n.IDescribable;
import org.xblackcat.rojac.i18n.Messages;

/**
 * @author xBlackCat
 */
public enum Scope implements IDescribable {
    All(Messages.Dialog_ExtMark_Scope_All),
    Forum(Messages.Dialog_ExtMark_Scope_Forum),
    Thread(Messages.Dialog_ExtMark_Scope_Thread);

    private final Messages msg;

    Scope(Messages msg) {
        this.msg = msg;
    }

    @Override
    public Messages getLabel() {
        return msg;
    }
}
