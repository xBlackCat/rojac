package org.xblackcat.rojac.i18n;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.options.Property;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Pattern;

/**
 * @author xBlackCat
 */

public enum Messages {
    Main_Window_Title,
    // Button texts
    Button_Ok,
    Button_Cancel,
    Button_Apply,
    Button_Save,
    Button_Preview,
    Button_Ignore,
    Button_Yes,
    Button_No,
    Button_ChangePassword,

    Button_Reply_ToolTip,

    Panel_Thread_Header_Id,
    Panel_Thread_Header_Subject,
    Panel_Thread_Header_User,
    Panel_Thread_Header_Replies,
    Panel_Thread_Header_Rating,
    Panel_Thread_Header_Date,

    // Main window view
    MainFrame_Button_Update,
    MainFrame_Button_LoadMessage,
    MainFrame_Button_Settings,
    MainFrame_Button_About,

    // Forum list view
    View_Forums_Title,
    View_Forums_Tab_Text,
    View_Forums_Button_Update,
    View_Forums_Button_Subscribed,
    View_Forums_Button_Filled,
    View_Forums_Button_HasUnread,

    // Favorites view
    View_Favorites_Title,
    View_Favorites_Tab_Text,

    // Threads view
    View_Thread_Button_NewThread,
    View_Thread_Button_PreviousUnread,
    View_Thread_Button_NextUnread,
    View_Thread_Button_ToThreadRoot,

    // Tray texts
    // Note that the first parameter is always a version string.
    Tray_State_Initialized,
    Tray_State_Normal,
    Tray_State_Synchronization,
    Tray_State_HaveUnreadMessages,

    // Tray popup menu texts
    Tray_Popup_Item_ShowMainframe,
    Tray_Popup_Item_HideMainframe,
    Tray_Popup_Item_Synchronize,
    Tray_Popup_Item_CancelSync,
    Tray_Popup_Item_Options,
    Tray_Popup_Item_About,
    Tray_Popup_Item_Exit,

    //
    // Dialog texts
    //

    // Set mark related dialog texts
    /**
     * Parameters are: 1. Mark description
     */
    Dialog_SetMark_Message,
    Dialog_SetMark_Title,

    // Confirm exit dialog
    Dialog_ConfirmExit_Message,
    Dialog_ConfirmExit_Title,

    // Login dialog related messages
    Dialog_Login_Title,
    Dialog_Login_Text,
    Dialog_Login_UserName,
    Dialog_Login_Password,
    Dialog_Login_SavePassword,
    Dialog_Login_EmptyUserName,
    Dialog_Login_EmptyPassword,
    Dialog_Login_InvalidUserName,
    Dialog_Login_InvalidUserName_Title,

    // Load extra messages dialog texts
    Dialog_LoadMessage_Title,
    Dialog_LoadMessage_Label,
    Dialog_LoadMessage_MessageNotExists,
    Dialog_LoadMessage_LoadAtOnce,

    // About dialog texts
    Dialog_About_Title,

    // Options dialog texts
    Dialog_Options_Title,
    Dialog_Options_Description,

    // Edit message dialog related texts
    ErrorDialog_MessageNotFound_Message,
    ErrorDialog_MessageNotFound_Title,
    Message_Response_Header,

    // Updater dialog mesages
    /**
     * Has one int parameter last version number.
     */
    Dialog_Updater_UpdateExists,
    Dialog_Updater_UpdateExists_Title,
    Dialog_Updater_NoUpdate,
    Dialog_Updater_NoUpdate_Title,

    /**
     * Parameters are: 1. Mark description
     */
    ErrorDialog_SetMark_Message,
    ErrorDialog_SetMark_Title,

    Panel_Message_Label_User,
    Panel_Message_Label_Date,
    Panel_Message_Toolbar_Rating,

    Synchronize_Command_Name_NewPosts,
    Synchronize_Command_Name_ExtraPosts,
    Synchronize_Command_Name_BrokenTopics,
    Synchronize_Command_Name_ForumList,
    Synchronize_Command_Name_Users,
    Synchronize_Command_Name_Submit,
    Synchronize_Command_Name_Test,

    Synchronize_Message_GotPosts,
    Synchronize_Message_GotForums,
    Synchronize_Message_GotUsers,
    Synchronize_Message_GotUserId,

    Synchronize_Message_UpdateDatabase,
    Synchronize_Message_StoreMessages,
    Synchronize_Message_StoreModerates,
    Synchronize_Message_StoreRatings,
    Synchronize_Message_UpdateCaches,
    Synchronize_Message_StoreUserInfo,

    Synchronize_Message_Portion,
    Synchronize_Message_Start,
    Synchronize_Message_Done,
    Synchronize_Message_UseUser,
    Synchronize_Message_Read,
    Synchronize_Message_WasRead,
    Synchronize_Message_ReadUnknown,
    Synchronize_Message_Write,
    Synchronize_Message_Exception,
    Synchronize_Message_CompressionUsed,
    Synchronize_Message_CompressionNotUsed,
    Synchronize_Message_ProxyUsed,

    // Favorite-related messages
    Favorite_Name_UnreadTreadPosts,
    Favorite_Name_UserPosts,
    Favorite_Name_PostResponses,
    Favorite_Name_UserResponses,
    Favorite_Name_Category,

    // Mark descriptions
    Description_Mark_Select,
    Description_Mark_PlusOne,
    Description_Mark_Agree,
    Description_Mark_Disagree,
    Description_Mark_X1,
    Description_Mark_X2,
    Description_Mark_X3,
    Description_Mark_Smile,
    Description_Mark_Remove,

    //Smile descriptions
    Description_Smile_Smile,
    Description_Smile_Sad,
    Description_Smile_Wink,
    Description_Smile_BigGrin,
    Description_Smile_Lol,
    Description_Smile_Smirk,
    Description_Smile_Confused,
    Description_Smile_No,
    Description_Smile_Super,
    Description_Smile_Shuffle,
    Description_Smile_Wow,
    Description_Smile_Crash,
    Description_Smile_User,
    Description_Smile_Maniac,
    Description_Smile_DoNotKnow,

    Description_UpdatePeriod_None,
    Description_UpdatePeriod_EveryRun,
    Description_UpdatePeriod_EveryDay,
    Description_UpdatePeriod_EveryWeek,
    Description_UpdatePeriod_EveryMonth,
    // Popup menu texts
    Popup_Link_Open_InBrowser,
    Popup_Link_Copy_ToClipboard,
    Popup_Link_Open_InBrowser_Message,
    Popup_Link_Open_InBrowser_Thread,
    // Menu of forum view
    Popup_View_Forums_Subscribe,
    Popup_View_Forums_Open,
    Popup_View_Forums_SetReadAll,
    Popup_View_Forums_SetUnreadAll,
    // Messages threads view related messages
    Popup_View_ThreadsTree_Mark_Title,
    Popup_View_ThreadsTree_Mark_Read,
    Popup_View_ThreadsTree_Mark_Unread,
    Popup_View_ThreadsTree_Mark_ThreadRead,
    Popup_View_ThreadsTree_Mark_ThreadUnread,
    Popup_View_ThreadsTree_Mark_Extended,

    Popup_View_ThreadsTree_CopyUrl,
    Popup_View_ThreadsTree_CopyUrl_Message,
    Popup_View_ThreadsTree_CopyUrl_Flat,
    Popup_View_ThreadsTree_CopyUrl_Thread,

    Popup_View_ThreadsTree_OpenMessage,
    Popup_View_ThreadsTree_OpenMessage_NewTab,
    Popup_View_ThreadsTree_OpenMessage_CurrentView,;

    private static final Log log = LogFactory.getLog(Messages.class);
    // Constants
    static final String LOCALIZATION_BUNDLE_NAME = "i18n/messages";

    private static ResourceBundle messages;

    private static final Lock readLock;
    private static final Lock writeLock;
    private static Locale currentLocale;

    static {
        ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
        readLock = rwl.readLock();
        writeLock = rwl.writeLock();

        setLocale(null);
    }

    /**
     * Set the specified locale for messages
     *
     * @param locale locale to set.
     *
     * @throws IllegalArgumentException is thrown if invalid locale is specified.
     */
    public static void setLocale(Locale locale) throws IllegalArgumentException {
        setLocale(locale, false);
    }

    /**
     * Set the specified locale for messages
     *
     * @param locale locale to set.
     * @param strict
     *
     * @throws IllegalArgumentException is thrown if invalid locale is specified.
     */
    public static void setLocale(Locale locale, boolean strict) throws IllegalArgumentException {
        ResourceBundle m;
        if (locale != null) {
            m = ResourceBundle.getBundle(LOCALIZATION_BUNDLE_NAME, locale);
            if (!m.getLocale().equals(locale)) {
                if (strict) {
                    throw new IllegalArgumentException("Can not load resources for " + locale + " locale.");
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("Can not initialize locale " + locale + ". The " + m.getLocale() + " will be used.");
                    }
                }
            }
        } else {
            m = ResourceBundle.getBundle(LOCALIZATION_BUNDLE_NAME);
        }

        writeLock.lock();
        try {
            messages = m;
            currentLocale = m.getLocale();
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Returns a localized text of the constant. Optionally accepts parameters to substitute into text.
     *
     * @param arguments optionally parameters for formatting message.
     *
     * @return formatted localized message.
     *
     * @throws MissingResourceException if no localized message is exists for the constant.
     */
    public String get(Object... arguments) throws MissingResourceException {
        String key = key();

        String mes;

        Locale l;
        readLock.lock();
        l = currentLocale;
        try {
            mes = messages.getString(key);
        } catch (MissingResourceException e) {
            if (Property.ROJAC_DEBUG_MODE.get()) {
                mes = key + ": " + ArrayUtils.toString(arguments);
            } else {
                throw e;
            }
        } finally {
            readLock.unlock();
        }
        if (mes != null) {
            try {
                mes = new String(mes.getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Invalid encoding for string of key " + key, e);
            }
            return String.format(l, mes, arguments);
        } else {
            return key;
        }
    }

    public String key() {
        return constantCamelToPropertyName(name());
    }

    public String toString() {
        return "Constant: " + name() + " (" + key() + ")";
    }

    public static Locale getLocale() {
        readLock.lock();
        try {
            return currentLocale;
        } finally {
            readLock.unlock();
        }
    }

    private static final Pattern DOTS_PATTERN = Pattern.compile("\\.{2,}");

    private static String constantCamelToPropertyName(String s) {
        if (s == null) {
            return null;
        }
        StringBuilder result = new StringBuilder(s.length() << 1);
        boolean wasDot = true;
        for (char c : s.toCharArray()) {
            if (Character.isUpperCase(c)) {
                if (!wasDot) {
                    result.append('_');
                }
                c = Character.toLowerCase(c);
            }
            if (c == '_') {
                result.append('.');
                wasDot = true;
            } else {
                result.append(c);
                wasDot = false;
            }
        }

        return DOTS_PATTERN.matcher(result).replaceAll(".");
    }
}
