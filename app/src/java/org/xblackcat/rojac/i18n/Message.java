package org.xblackcat.rojac.i18n;

import org.xblackcat.rojac.util.MessageUtils;

import java.util.MissingResourceException;

/**
 * @author xBlackCat
 */

public enum Message {
    Main_Window_Title,
    // Button texts
    Button_Ok,
    Button_Cancel,
    Button_Apply,
    Button_Save,
    Button_Check,
    Button_Preview,
    Button_Default,
    Button_Yes,
    Button_No,
    Button_ChangePassword,
    Button_Import,

    Button_Reply_ToolTip,

    UserName_Anonymous,

    Panel_Thread_Header_Id,
    Panel_Thread_Header_Subject,
    Panel_Thread_Header_User,
    Panel_Thread_Header_Replies,
    Panel_Thread_Header_Rating,
    Panel_Thread_Header_Date,

    // Main window view
    MainFrame_Button_Update,
    MainFrame_Button_LoadMessage,
    MainFrame_Button_GoToMessage,
    MainFrame_Button_Settings,
    MainFrame_Button_About,
    MainFrame_Button_GoBack,
    MainFrame_Button_GoForward,
    MainFrame_Button_ForumManage,

    // Forum list view
    View_Forums_Title,
    View_Forums_Tab_Text,
    View_Forums_Button_Update,
    View_Forums_Button_Subscribed,
    View_Forums_Button_Filled,
    View_Forums_Button_HasUnread,

    // Recent topics view
    View_RecentTopics_Title,

    // Favorites view
    View_Favorites_Title,
    View_Favorites_Tab_Text,
    View_Favorites_Statistic_Data,
    View_Favorites_Statistic_Label,

    // Navigation view
    View_Navigation_Title,
    View_Navigation_Item_Personal,
    View_Navigation_Item_SubscribedForums,
    View_Navigation_Item_NotSubscribedForums,
    View_Navigation_Item_Favorites,
    View_Navigation_Item_ForumInfo,
    View_Navigation_Item_ForumExtraInfo,
    View_Navigation_Item_Outbox,
    View_Navigation_Item_MyResponses,
    View_Navigation_Item_Ignored,
    View_Navigation_Item_Draft,

    // Threads view
    View_Thread_Button_NewThread,
    View_Thread_Button_Previous,
    View_Thread_Button_Next,
    View_Thread_Button_PreviousUnread,
    View_Thread_Button_NextUnread,
    View_Thread_Button_ToThreadRoot,

    // Tray texts
    // Note that the first parameter is always a version string.
    Tray_State_Loading,
    Tray_State_Normal,
    Tray_State_Synchronization,
    Tray_State_HaveUnreadMessages,
    Tray_State_HaveUnreadReplies,

    // Tray popup menu texts
    Tray_Popup_Item_ShowMainframe,
    Tray_Popup_Item_HideMainframe,
    Tray_Popup_Item_Synchronize,
    Tray_Popup_Item_CancelSync,
    Tray_Popup_Item_Options,
    Tray_Popup_Item_About,
    Tray_Popup_Item_Exit,

    // Tray notifications
    Tray_Balloon_SynchronizationComplete_Title,
    // 1. Forums affected, 2. topics affected, 3. Messages affected.
    Tray_Balloon_SynchronizationComplete_Text,

    //
    // Dialog texts
    //

    // Set mark related dialog texts
    /**
     * Parameters are: 1. Mark description
     */
    Dialog_SetMark_Message,
    Dialog_SetMark_Title,

    Dialog_Import_Title,
    Dialog_Import_Label,
    Dialog_Import_Label_Source,
    Dialog_Import_Label_Destination,
    Dialog_Import_Warning_Title,
    Dialog_Import_Warning_Text,

    Dialog_ImportProgress_Title,
    Dialog_ImportProgress_Label,

    /**
     * Parameters:
     * 1. engine name (string)
     * 2. url (string)
     */
    Log_ImportProgress_CheckSource,
    /**
     * Parameters:
     * 1. engine name (string)
     * 2. url (string)
     */
    Log_ImportProgress_CheckDestination,
    /**
     * Parameters:
     * 1. item name (string)
     * 2. rows amount (integer)
     */
    Log_ImportProgress_CopyItem,
    /**
     * Parameters:
     * 1. item name (string)
     * 2. rows amount (integer)
     */
    Log_ImportProgress_CopyDone,
    Log_ImportProgress_Done,

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

    // Database settings dialog
    Dialog_DbSettings_Title,
    Dialog_DbSettings_Label,
    Dialog_DbSettings_Label_Engine,
    Dialog_DbSettings_Label_Url,
    Dialog_DbSettings_Label_UserName,
    Dialog_DbSettings_Label_Passord,
    Dialog_DbSettings_Label_ShutdownUrl,
    Dialog_DbSettings_Label_DriverName,

    Dialog_DbCheck_Title,
    Dialog_DbCheck_Success,
    Dialog_DbCheck_Fail,

    // Subscription manager dialog related
    Dialog_Subscription_Title,
    Dialog_Subscription_Header_Subscription,
    Dialog_Subscription_Header_ShortForumName,
    Dialog_Subscription_Header_FullForumName,

    WarnDialog_NoForums_Title,
    WarnDialog_NoForums_Question,

    WarnDialog_NothingToSync_Title,
    WarnDialog_NothingToSync_Question,

    // Load extra messages dialog texts
    Dialog_OpenMessage_Title,
    Dialog_OpenMessage_Label,

    // Load extra messages dialog texts
    Dialog_LoadMessage_Title,
    Dialog_LoadMessage_Label,
    Dialog_LoadMessage_MessageNotExists,
    Dialog_LoadMessage_LoadAtOnce,

    // About dialog texts
    Dialog_About_Title,

    // Options dialog texts
    Dialog_Options_Title,
    Dialog_Options_Title_General,
    Dialog_Options_Title_Keymap,
    Dialog_Options_Description_General,
    Dialog_Options_Description_Keymap,

    // Edit message dialog related texts
    ErrorDialog_MessageNotFound_Message,
    ErrorDialog_MessageNotFound_Title,
    ErrorDialog_MessageEmptySubject_Message,
    ErrorDialog_MessageEmptySubject_Title,

    Message_Response_Header,

    // Extended mark messages dialgo related
    Dialog_ExtMark_Title,
    Dialog_ExtMark_TopLine,
    Dialog_ExtMark_As,
    Dialog_ExtMark_State_Read,
    Dialog_ExtMark_State_Unread,
    Dialog_ExtMark_DateDirection_Before,
    Dialog_ExtMark_DateDirection_After,
    Dialog_ExtMark_Scope_All,
    Dialog_ExtMark_Scope_Forum,
    Dialog_ExtMark_Scope_Thread,

    // Updater dialog messages
    /**
     * Has one int parameter last version number.
     */
    Dialog_Updater_UpdateExists,
    Dialog_Updater_UpdateExists_Title,
    Dialog_Updater_NoUpdate,
    Dialog_Updater_NoUpdate_Title,

    // Progress control related
    ProgressControl_AffectedBytes,

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
    Favorite_Thread_Name,
    Favorite_UserPosts_Name,
    Favorite_SubTree_Name,
    Favorite_UserReplies_Name,
    Favorite_Category_Name,

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
    // Common popup action names
    Popup_View_Open,
    Popup_View_Remove,
    Popup_View_SetReadAll,
    Popup_View_SetUnreadAll,

    Popup_Ignore_Set,
    Popup_Ignore_Reset,

    // Menu of forum view
    Popup_View_Forums_Subscribe,
    // Messages threads view related messages
    Popup_View_ThreadsTree_Mark_Title,
    Popup_View_ThreadsTree_Mark_Read,
    Popup_View_ThreadsTree_Mark_Unread,
    Popup_View_ThreadsTree_Mark_ThreadRead,
    Popup_View_ThreadsTree_Mark_ThreadUnread,
    Popup_View_ThreadsTree_Mark_Extended,
    Popup_View_ThreadsTree_Mark_WholeThreadRead,

    Popup_View_OutboxTree_Edit,
    Popup_View_OutboxTree_Remove,
    Popup_View_OutboxTree_RemoveAll,

    Popup_Open_SubMenu,
    Popup_Open_MessageInThread,
    Popup_Open_MessageInForum,
    Popup_Open_MessageInTab,
    Popup_Open_UserPostList,
    Popup_Open_UserReplyList,

    Popup_Favorites_Add,
    Popup_Favorites_Add_Thread,
    Popup_Favorites_Add_UserPosts,
    Popup_Favorites_Add_ToUserReplies,

    Popup_View_ThreadsTree_CopyUrl,
    Popup_View_ThreadsTree_CopyUrl_Message,
    Popup_View_ThreadsTree_CopyUrl_Flat,
    Popup_View_ThreadsTree_CopyUrl_Thread,

    OpenMessageMethod_MessageInTab,
    OpenMessageMethod_InThread,
    OpenMessageMethod_InForum,;

    // Constants
    static final String LOCALIZATION_BUNDLE_NAME = "messages";

    private final String key = MessageUtils.constantCamelToPropertyName(name());

    /**
     * Returns a localized text of the constant. Optionally accepts parameters to substitute into text.
     *
     * @param arguments optionally parameters for formatting message.
     * @return formatted localized message.
     * @throws MissingResourceException if no localized message is exists for the constant.
     */
    public String get(Object... arguments) throws MissingResourceException {
        return LocaleControl.getInstance().getString(LOCALIZATION_BUNDLE_NAME, key(), arguments);
    }

    public String key() {
        return key;
    }

    public String toString() {
        return "Constant: " + name() + " (" + key() + ")";
    }
}
