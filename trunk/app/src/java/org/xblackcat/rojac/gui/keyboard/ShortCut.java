package org.xblackcat.rojac.gui.keyboard;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * List of all the available keyboard shortcuts.
 *
 * @author xBlackCat
 */

public enum ShortCut {
    // Thread view related
    NextUnreadMessage(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, KeyEvent.VK_DOWN, KeyEvent.CTRL_DOWN_MASK),
    PrevUnreadMessage(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, KeyEvent.VK_UP, KeyEvent.CTRL_DOWN_MASK),
    NewThread(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK),
    // Main frame related
    Synchronization(JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_F9, 0),
    LoadExtraMessages(JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_F9, KeyEvent.SHIFT_DOWN_MASK),
    Settings(JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK + KeyEvent.ALT_DOWN_MASK),
    About(JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_F1, 0),
    // Forum list view related
    ShowOnlyNotEmpty(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, KeyEvent.VK_1, KeyEvent.CTRL_DOWN_MASK),
    ShowOnlySubscribed(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, KeyEvent.VK_2, KeyEvent.CTRL_DOWN_MASK),
    ShowOnlyUnread(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, KeyEvent.VK_3, KeyEvent.CTRL_DOWN_MASK),
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
