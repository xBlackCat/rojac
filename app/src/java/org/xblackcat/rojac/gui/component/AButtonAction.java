package org.xblackcat.rojac.gui.component;

import org.xblackcat.rojac.gui.keyboard.ShortCut;
import org.xblackcat.rojac.i18n.Messages;

import javax.swing.*;

/**
 * @author xBlackCat
 */

public abstract class AButtonAction extends AbstractAction {
    private final Messages message;
    private final ShortCut shortCut;

    public AButtonAction(Messages message) {
        this(message, null);
    }

    public AButtonAction(Messages message, ShortCut shortCut) {
        this.message = message;
        this.shortCut = shortCut;
    }

    public Messages getMessage() {
        return message;
    }

    public ShortCut getShortCut() {
        return shortCut;
    }
}
