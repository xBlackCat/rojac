package org.xblackcat.rojac.gui.keyboard;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * List of all the available shortcuts
 *
 * @author xBlackCat
 */

public enum ShortCut {
    NextUnreadMessage(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, KeyEvent.VK_DOWN, KeyEvent.CTRL_DOWN_MASK),
    PrevUnreadMessage(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, KeyEvent.VK_UP, KeyEvent.CTRL_DOWN_MASK),
    NewThread(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK),;

    private final int condition;
    private final KeyStroke defKeystroke;
    private KeyStroke keystroke;

    ShortCut(int condition, int keyCode, int modifier) {
        this.condition = condition;
        this.defKeystroke = KeyStroke.getKeyStroke(keyCode, modifier);
    }

    public KeyStroke getKeyStroke() {
        return keystroke == null ? defKeystroke : keystroke;
    }

    public int getCondition() {
        return condition;
    }

}
