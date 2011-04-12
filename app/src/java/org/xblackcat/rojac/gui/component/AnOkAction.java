package org.xblackcat.rojac.gui.component;

import org.xblackcat.rojac.i18n.Messages;

/**
 * @author xBlackCat
 */
public abstract class AnOkAction extends AButtonAction {
    protected AnOkAction(ShortCut shortCut) {
        super(Messages.Button_Ok, shortCut);
    }

    protected AnOkAction() {
        super(Messages.Button_Ok);
    }
}
