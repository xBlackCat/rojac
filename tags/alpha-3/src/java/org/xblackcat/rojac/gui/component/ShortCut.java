package org.xblackcat.rojac.gui.component;

import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.util.ShortCutUtils;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * List of all the available keyboard shortcuts.
 *
 * @author xBlackCat
 */

public enum ShortCut {
    // Thread view related
    NextMessage(Messages.View_Thread_Button_Next, KeyEvent.VK_DOWN, KeyEvent.SHIFT_DOWN_MASK),
    PrevMessage(Messages.View_Thread_Button_Previous, KeyEvent.VK_UP, KeyEvent.SHIFT_DOWN_MASK),
    NextUnreadMessage(Messages.View_Thread_Button_NextUnread, KeyEvent.VK_DOWN, KeyEvent.CTRL_DOWN_MASK),
    PrevUnreadMessage(Messages.View_Thread_Button_PreviousUnread, KeyEvent.VK_UP, KeyEvent.CTRL_DOWN_MASK),
    ToThreadRoot(Messages.View_Thread_Button_ToThreadRoot, KeyEvent.VK_LEFT, KeyEvent.CTRL_DOWN_MASK),
    NewThread(Messages.View_Thread_Button_NewThread, KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK),
    MarkSubTreeRead(Messages.Popup_View_ThreadsTree_Mark_ThreadRead, KeyEvent.VK_RIGHT, KeyEvent.CTRL_DOWN_MASK + KeyEvent.SHIFT_DOWN_MASK),
    MarkWholeThreadRead(Messages.Popup_View_ThreadsTree_Mark_WholeThreadRead,KeyEvent.VK_LEFT, KeyEvent.CTRL_DOWN_MASK + KeyEvent.SHIFT_DOWN_MASK),
    // Main frame related
    Synchronization(Messages.MainFrame_Button_Update, JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_F9, 0),
    LoadExtraMessages(Messages.MainFrame_Button_LoadMessage, JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_F9, KeyEvent.SHIFT_DOWN_MASK),
    GoToMessage(Messages.MainFrame_Button_GoToMessage, JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_G, KeyEvent.CTRL_DOWN_MASK),
    Settings(Messages.MainFrame_Button_Settings, JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK + KeyEvent.ALT_DOWN_MASK),
    About(Messages.MainFrame_Button_About, JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_F1, 0),
    GoBack(Messages.MainFrame_Button_GoBack, JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_LEFT, KeyEvent.ALT_DOWN_MASK),
    GoForward(Messages.MainFrame_Button_GoForward, JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_RIGHT, KeyEvent.ALT_DOWN_MASK),
    ForumManage(Messages.MainFrame_Button_ForumManage, JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_F, KeyEvent.ALT_DOWN_MASK),
    // Forum list view related
    ShowOnlyNotEmpty(Messages.View_Forums_Button_Filled, KeyEvent.VK_1, KeyEvent.CTRL_DOWN_MASK),
    ShowOnlySubscribed(Messages.View_Forums_Button_Subscribed, KeyEvent.VK_2, KeyEvent.CTRL_DOWN_MASK),
    ShowOnlyUnread(Messages.View_Forums_Button_HasUnread, KeyEvent.VK_3, KeyEvent.CTRL_DOWN_MASK),
    // Message view related
    ReplyOnMessage(Messages.Button_Reply_ToolTip, KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK),
    ShowMessageMarks(Messages.Panel_Message_Toolbar_Rating, KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK),
    SetMarkOnMessage(Messages.Description_Mark_Select, KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK),
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

    private final Messages description;
    private final int condition;
    private final KeyStroke defaultKeyStroke;
    private KeyStroke keyStroke;

    ShortCut(Messages description, int condition, int keyCode, int modifier) {
        this.condition = condition;
        this.defaultKeyStroke = KeyStroke.getKeyStroke(keyCode, modifier);
        this.description = description;
    }

    ShortCut(Messages description, int keyCode, int modifier) {
        this(description, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, keyCode, modifier);
    }

    public KeyStroke getKeyStroke() {
        return keyStroke == null ? defaultKeyStroke : keyStroke;
    }

    public KeyStroke getDefaultKeyStroke() {
        return defaultKeyStroke;
    }

    public void setKeyStroke(KeyStroke keyStroke) {
        this.keyStroke = keyStroke;
    }

    public int getCondition() {
        return condition;
    }

    public String getActionName() {
        return ACTION_PREFIX + name();
    }

    @Override
    public String toString() {
        final StringBuilder toString = new StringBuilder();
        toString.append(getActionName());
        toString.append(" (");
        toString.append(ShortCutUtils.keyStrokeHint(defaultKeyStroke));
        if (keyStroke != null) {
            toString.append("/");
            toString.append(ShortCutUtils.keyStrokeHint(keyStroke));
        }
        toString.append(")");
        return toString.toString();
    }

    public Messages getDescription() {
        return description;
    }

    public boolean isCustom() {
        return keyStroke != null && !defaultKeyStroke.equals(keyStroke);
    }
}
