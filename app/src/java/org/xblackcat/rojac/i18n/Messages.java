package org.xblackcat.rojac.i18n;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.utils.ResourceUtils;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author xBlackCat
 */

public enum Messages {
    MAIN_WINDOW_TITLE,
    // Button texts
    BUTTON_OK,
    BUTTON_CANCEL,
    BUTTON_APPLY,
    BUTTON_SAVE,
    BUTTON_PREVIEW,
    BUTTON_IGNORE,
    BUTTON_YES,
    BUTTON_NO,
    BUTTON_CHANGEPASSWORD,

    BUTTON_REPLY_TOOLTIP,

    PANEL_THREAD_HEADER_ID,
    PANEL_THREAD_HEADER_SUBJECT,
    PANEL_THREAD_HEADER_USER,
    PANEL_THREAD_HEADER_REPLIES,
    PANEL_THREAD_HEADER_RATING,
    PANEL_THREAD_HEADER_DATE,

    // Main window view
    MAINFRAME_BUTTON_UPDATE,
    MAINFRAME_BUTTON_LOADMESSAGE,
    MAINFRAME_BUTTON_SETTINGS,
    MAINFRAME_BUTTON_ABOUT,

    // Forum list view
    VIEW_FORUMS_TITLE,
    VIEW_FORUMS_TAB_TEXT,
    VIEW_FORUMS_BUTTON_UPDATE,
    VIEW_FORUMS_BUTTON_SUBSCRIBED,
    VIEW_FORUMS_BUTTON_FILLED,
    VIEW_FORUMS_BUTTON_HASUNREAD,

    // Favorites view
    VIEW_FAVORITES_TITLE,
    VIEW_FAVORITES_TAB_TEXT,

    // Threads view
    VIEW_THREAD_BUTTON_NEW_THREAD,
    VIEW_THREAD_BUTTON_PREVIOUS_UNREAD,
    VIEW_THREAD_BUTTON_NEXT_UNREAD,

    // Tray texts
    // Note that the first parameter is always a version string.
    TRAY_STATE_INITIALIZED,
    TRAY_STATE_NORMAL,
    TRAY_STATE_SYNCHRONIZATION,
    TRAY_STATE_HAVE_UNREAD_MESSAGES,

    //
    // Dialog texts
    //

    // Set mark related dialog texts
    /**
     * Parameters are: 1. Mark description
     */
    DIALOG_SET_MARK_MESSAGE,
    DIALOG_SET_MARK_TITLE,

    // Login dialog related messages
    DIALOG_LOGIN_TITLE,
    DIALOG_LOGIN_TEXT,
    DIALOG_LOGIN_USERNAME,
    DIALOG_LOGIN_PASSWORD,
    DIALOG_LOGIN_SAVE_PASSWORD,
    DIALOG_LOGIN_EMPTY_USERNAME,
    DIALOG_LOGIN_EMPTY_PASSWORD,
    DIALOG_LOGIN_INVALID_USERNAME,
    DIALOG_LOGIN_INVALID_USERNAME_TITLE,

    // Load extra messages dialog texts
    DIALOG_LOADMESSAGE_TITLE,
    DIALOG_LOADMESSAGE_LABEL,
    DIALOG_LOADMESSAGE_LOADATONCE,

    // About dialog texts
    DIALOG_ABOUT_TITLE,

    // Options dialog texts
    DIALOG_OPTIONS_TITLE,
    DIALOG_OPTIONS_DESCRIPTION,

    // Edit message dialog related texts
    ERROR_DIALOG_MESSAGE_NOT_FOUND_MESSAGE,
    ERROR_DIALOG_MESSAGE_NOT_FOUND_TITLE,
    MESSAGE_RESPONSE_HEADER,

    /**
     * Parameters are: 1. Mark description
     */
    ERROR_DIALOG_SET_MARK_MESSAGE,
    ERROR_DIALOG_SET_MARK_TITLE,

    MESSAGE_PANE_USER_LABEL,
    MESSAGE_PANE_DATE_LABEL,
    MESSAGE_PANE_TOOLBAR_TITLE_RATING,

    SYNCHRONIZE_COMMAND_NAME_NEW_POSTS,
    SYNCHRONIZE_COMMAND_NAME_EXTRA_POSTS,
    SYNCHRONIZE_COMMAND_NAME_BROKEN_TOPICS,
    SYNCHRONIZE_COMMAND_NAME_FORUM_LIST,
    SYNCHRONIZE_COMMAND_NAME_USERS,
    SYNCHRONIZE_COMMAND_NAME_SUBMIT,
    SYNCHRONIZE_COMMAND_NAME_TEST,

    SYNCHRONIZE_COMMAND_GOT_POSTS,
    SYNCHRONIZE_COMMAND_GOT_FORUMS,
    SYNCHRONIZE_COMMAND_GOT_USERS,
    SYNCHRONIZE_COMMAND_GOT_USER_ID,

    SYNCHRONIZE_COMMAND_UPDATE_DATABASE,
    SYNCHRONIZE_COMMAND_PORTION,
    SYNCHRONIZE_COMMAND_START,
    SYNCHRONIZE_COMMAND_DONE,
    SYNCHRONIZE_COMMAND_USE_USER,    
    SYNCHRONIZE_COMMAND_READ,
    SYNCHRONIZE_COMMAND_WRITE,
    SYNCHRONIZE_COMMAND_EXCEPTION,
    SYNCHRONIZE_COMMAND_USE_COMPRESSION,
    SYNCHRONIZE_COMMAND_DONT_USE_COMPRESSION,

    // Mark descriptions
    DESCRIPTION_MARK_SELECT,
    DESCRIPTION_MARK_PLUSONE,
    DESCRIPTION_MARK_AGREE,
    DESCRIPTION_MARK_DISAGREE,
    DESCRIPTION_MARK_X1,
    DESCRIPTION_MARK_X2,
    DESCRIPTION_MARK_X3,
    DESCRIPTION_MARK_SMILE,
    DESCRIPTION_MARK_REMOVE,

    //Smile descriptions
    DESCRIPTION_SMILE_SMILE,
    DESCRIPTION_SMILE_SAD,
    DESCRIPTION_SMILE_WINK,
    DESCRIPTION_SMILE_BIGGRIN,
    DESCRIPTION_SMILE_LOL,
    DESCRIPTION_SMILE_SMIRK,
    DESCRIPTION_SMILE_CONFUSED,
    DESCRIPTION_SMILE_NO,
    DESCRIPTION_SMILE_SUPER,
    DESCRIPTION_SMILE_SHUFFLE,
    DESCRIPTION_SMILE_WOW,
    DESCRIPTION_SMILE_CRASH,
    DESCRIPTION_SMILE_USER,
    DESCRIPTION_SMILE_MANIAC,
    DESCRIPTION_SMILE_DONOTKNOW,

    // Popup menu texts
    POPUP_LINK_OPEN_IN_BROWSER,
    POPUP_LINK_COPY_TO_CLIPBOARD,
    POPUP_LINK_OPEN_MESSAGE,
    POPUP_LINK_OPEN_MESSAGE_IN_NEW_TAB,
    POPUP_LINK_OPEN_THREAD_IN_NEW_TAB,
    // Menu of forum view
    POPUP_VIEW_FORUMS_SUBSCRIBE,
    POPUP_VIEW_FORUMS_OPEN,
    POPUP_VIEW_FORUMS_SET_READ_ALL,
    POPUP_VIEW_FORUMS_SET_UNREAD_ALL,
    // Messages threads view related messages
    POPUP_VIEW_THREADS_TREE_MARK_TITLE,
    POPUP_VIEW_THREADS_TREE_MARK_READ,
    POPUP_VIEW_THREADS_TREE_MARK_UNREAD,
    POPUP_VIEW_THREADS_TREE_MARK_THREAD_READ,
    POPUP_VIEW_THREADS_TREE_MARK_THREAD_UNREAD,

    POPUP_VIEW_THREADS_TREE_COPYURL,
    POPUP_VIEW_THREADS_TREE_COPYURL_MESSAGE,
    POPUP_VIEW_THREADS_TREE_COPYURL_FLAT,
    POPUP_VIEW_THREADS_TREE_COPYURL_THREAD,

    POPUP_VIEW_THREADS_TREE_OPEN_MESSAGE,
    POPUP_VIEW_THREADS_TREE_OPEN_MESSAGE_NEW_TAB,
    POPUP_VIEW_THREADS_TREE_OPEN_MESSAGE_CURRENT_VIEW,
    ;

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
        String key = ResourceUtils.constantToProperty(name());

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

    public String toString() {
        return "Constant: " + name() + " (" + ResourceUtils.constantToProperty(name()) + ")";
    }

    public static Locale getLocale() {
        readLock.lock();
        try {
            return currentLocale;
        } finally {
            readLock.unlock();
        }
    }
}
