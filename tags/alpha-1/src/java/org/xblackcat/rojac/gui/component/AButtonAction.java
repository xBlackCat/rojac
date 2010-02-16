package org.xblackcat.rojac.gui.component;

import org.xblackcat.rojac.i18n.Messages;

import java.awt.event.ActionListener;

/**
 * @author xBlackCat
 */

public abstract class AButtonAction implements ActionListener {
    private final Messages message;

    public AButtonAction(Messages message) {
        this.message = message;
    }

    public Messages getMessage() {
        return message;
    }
}
