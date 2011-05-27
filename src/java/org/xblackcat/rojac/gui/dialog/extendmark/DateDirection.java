package org.xblackcat.rojac.gui.dialog.extendmark;

import org.xblackcat.rojac.i18n.IDescribable;
import org.xblackcat.rojac.i18n.Messages;

/**
 * @author xBlackCat
 */

public enum DateDirection implements IDescribable {
    Before(Messages.Dialog_ExtMark_DateDirection_Before),
    After(Messages.Dialog_ExtMark_DateDirection_After);

    private final Messages msg;

    DateDirection(Messages msg) {
        this.msg = msg;
    }

    @Override
    public Messages getLabel() {
        return msg;
    }

}
