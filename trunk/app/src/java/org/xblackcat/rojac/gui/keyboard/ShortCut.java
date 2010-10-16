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
    NewThread(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK),
    Synchronization(JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_F9, 0),
    LoadExtraMessages(JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_F9, KeyEvent.SHIFT_DOWN_MASK),
    Settings(JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK + KeyEvent.ALT_DOWN_MASK),
    About(JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_F1, 0),
    ;

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
