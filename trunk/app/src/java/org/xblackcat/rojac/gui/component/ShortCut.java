package org.xblackcat.rojac.gui.component;

import org.xblackcat.rojac.i18n.Message;
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
    NextMessage(Message.View_Thread_Button_Next, KeyEvent.VK_DOWN, KeyEvent.SHIFT_DOWN_MASK),
    PrevMessage(Message.View_Thread_Button_Previous, KeyEvent.VK_UP, KeyEvent.SHIFT_DOWN_MASK),
    NextUnreadMessage(Message.View_Thread_Button_NextUnread, KeyEvent.VK_DOWN, KeyEvent.CTRL_DOWN_MASK),
    PrevUnreadMessage(Message.View_Thread_Button_PreviousUnread, KeyEvent.VK_UP, KeyEvent.CTRL_DOWN_MASK),
    ToThreadRoot(Message.View_Thread_Button_ToThreadRoot, KeyEvent.VK_LEFT, KeyEvent.CTRL_DOWN_MASK),
    NewThread(Message.View_Thread_Button_NewThread, KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK),
    MarkSubTreeRead(Message.Popup_View_ThreadsTree_Mark_ThreadRead, KeyEvent.VK_RIGHT, KeyEvent.CTRL_DOWN_MASK + KeyEvent.SHIFT_DOWN_MASK),
    MarkWholeThreadRead(Message.Popup_View_ThreadsTree_Mark_WholeThreadRead, KeyEvent.VK_LEFT, KeyEvent.CTRL_DOWN_MASK + KeyEvent.SHIFT_DOWN_MASK),
    IgnoreTopic(Message.View_Thread_Button_IgnoreTopic, KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK),
    FollowTopic(Message.View_Thread_Button_FollowTopic, KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK),
    IgnoreUser(Message.View_Thread_Button_IgnoreUser, KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK + KeyEvent.SHIFT_DOWN_MASK),
    FollowUser(Message.View_Thread_Button_FollowUser, KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK + KeyEvent.SHIFT_DOWN_MASK),
    IgnoreUnread(Message.View_Thread_Button_IgnoreUnread, KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK + KeyEvent.ALT_DOWN_MASK),
    ShowHiddenThreads(Message.View_Thread_Button_ShowHiddenThreads, KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK + KeyEvent.SHIFT_DOWN_MASK),
    // Main frame related
    Synchronization(Message.MainFrame_Button_Update, JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_F9, 0),
    LoadExtraMessages(Message.MainFrame_Button_LoadMessage, JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_F9, KeyEvent.SHIFT_DOWN_MASK),
    GoToMessage(Message.MainFrame_Button_GoToMessage, JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_G, KeyEvent.CTRL_DOWN_MASK),
    Settings(Message.MainFrame_Button_Settings, JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK + KeyEvent.ALT_DOWN_MASK),
    About(Message.MainFrame_Button_About, JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_F1, 0),
    GoBack(Message.MainFrame_Button_GoBack, JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_LEFT, KeyEvent.ALT_DOWN_MASK),
    GoForward(Message.MainFrame_Button_GoForward, JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_RIGHT, KeyEvent.ALT_DOWN_MASK),
    ForumManage(Message.MainFrame_Button_ForumManage, JComponent.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_F, KeyEvent.ALT_DOWN_MASK),
    // Forum list view related
    ShowOnlyNotEmpty(Message.View_Forums_Button_Filled, KeyEvent.VK_1, KeyEvent.CTRL_DOWN_MASK),
    ShowOnlySubscribed(Message.View_Forums_Button_Subscribed, KeyEvent.VK_2, KeyEvent.CTRL_DOWN_MASK),
    ShowOnlyUnread(Message.View_Forums_Button_HasUnread, KeyEvent.VK_3, KeyEvent.CTRL_DOWN_MASK),
    // Message view related
    ReplyOnMessage(Message.Button_Reply_ToolTip, KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK),
    ShowMessageMarks(Message.Panel_Message_Toolbar_Rating, KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK),
    SetMarkOnMessage(Message.Description_Mark_Select, KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK),
//    ----
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

    private final Message description;
    private final int condition;
    private final KeyStroke defaultKeyStroke;
    private KeyStroke keyStroke;

    ShortCut(Message description, int condition, int keyCode, int modifier) {
        this.condition = condition;
        this.defaultKeyStroke = KeyStroke.getKeyStroke(keyCode, modifier);
        this.description = description;
    }

    ShortCut(Message description, int keyCode, int modifier) {
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

    public Message getDescription() {
        return description;
    }

    public boolean isCustom() {
        return keyStroke != null && !defaultKeyStroke.equals(keyStroke);
    }
}
