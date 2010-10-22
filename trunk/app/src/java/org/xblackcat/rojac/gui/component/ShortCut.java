package org.xblackcat.rojac.gui.component;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * List of all the available keyboard shortcuts.
 *
 * @author xBlackCat
 */

public enum ShortCut {
    // Thread view related
    NextUnreadMessage(KeyEvent.VK_DOWN, KeyEvent.CTRL_DOWN_MASK),
    PrevUnreadMessage(KeyEvent.VK_UP, KeyEvent.CTRL_DOWN_MASK),
    NewThread(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK),
    // Main frame related
    Synchronization(JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_F9, 0),
    LoadExtraMessages(JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_F9, KeyEvent.SHIFT_DOWN_MASK),
    Settings(JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK + KeyEvent.ALT_DOWN_MASK),
    About(JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_F1, 0),
    // Forum list view related
    ShowOnlyNotEmpty(KeyEvent.VK_1, KeyEvent.CTRL_DOWN_MASK),
    ShowOnlySubscribed(KeyEvent.VK_2, KeyEvent.CTRL_DOWN_MASK),
    ShowOnlyUnread(KeyEvent.VK_3, KeyEvent.CTRL_DOWN_MASK),
    // Message view related
    ReplyOnMessage(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK),
    ShowMessageMarks(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK),
    SetMarkOnMessage(KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK),
    ;
    private static final String ACTION_PREFIX = "Action:";

    public static ShortCut getShortCut(String action) {
        if (!action.startsWith(ACTION_PREFIX)) {
            return null;
        }

        try {
            return valueOf(action.substring(ACTION_PREFIX.length()));
        } catch (IllegalArgumentException e) {
            // No such action shortcut.
            return null;
        }
    }

    private final int condition;
    private final KeyStroke defKeystroke;
    private KeyStroke keyStroke;

    ShortCut(int condition, int keyCode, int modifier) {
        this.condition = condition;
        this.defKeystroke = KeyStroke.getKeyStroke(keyCode, modifier);
    }

    ShortCut(int keyCode, int modifier) {
        this(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, keyCode, modifier);
    }

    public KeyStroke getKeyStroke() {
        return keyStroke == null ? defKeystroke : keyStroke;
    }

    public void setShortCut(int keyCode, int modifier) {
        keyStroke = KeyStroke.getKeyStroke(keyCode, modifier);
    }

    public int getCondition() {
        return condition;
    }

    public String getActionName() {
        return ACTION_PREFIX + name();
    }

    @Override
    public String toString() {
        return getActionName();
    }
}
