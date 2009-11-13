package org.xblackcat.rojac.i18n;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    PANEL_THREAD_HEADER_RATING,
    PANEL_THREAD_HEADER_DATE,

    // Main window view
    MAINFRAME_BUTTON_UPDATE,
    MAINFRAME_BUTTON_LOADMESSAGE,
    MAINFRAME_BUTTON_SETTINGS,

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

    // Load extra messages dialog texts
    DIALOG_LOADMESSAGE_TITLE,
    DIALOG_LOADMESSAGE_LABEL,
    DIALOG_LOADMESSAGE_LOADATONCE,

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
    POPUP_VIEW_THREADS_TREE_COPYURL,
    POPUP_VIEW_THREADS_TREE_COPYURL_MESSAGE,
    POPUP_VIEW_THREADS_TREE_COPYURL_FLAT,
    POPUP_VIEW_THREADS_TREE_COPYURL_THREAD;

    private static final Log log = LogFactory.getLog(Messages.class);
    // Constants
    private static final String LOCALIZATION_BUNDLE_NAME = "i18n/messages";

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
        ResourceBundle m;
        if (locale != null) {
            m = ResourceBundle.getBundle(LOCALIZATION_BUNDLE_NAME, locale);
            if (!m.getLocale().equals(locale)) {
//                throw new IllegalArgumentException("Can not load resources for " + locale + " locale.");
                if (log.isDebugEnabled()) {
                    log.debug("Can not initialize locale " + locale + ". The " + m.getLocale() + " will be used.");
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
     * @param params optionally parameters for formatting message.
     *
     * @return formatted localized message.
     *
     * @throws MissingResourceException if no localized message is exists for the constant.
     */
    public String get(Object... params) throws MissingResourceException {
        String key = ResourceUtils.constantToProperty(name());

        String mes;

        Locale l;
        readLock.lock();
        try {
            mes = messages.getString(key);
            l = messages.getLocale();
/*
        } catch (MissingResourceException e) {
            // For testing purposes
            mes = key;
            l = locale;
*/
        } finally {
            readLock.unlock();
        }
        if (mes != null) {
            try {
                mes = new String(mes.getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Invalid encoding for string of key " + key, e);
            }
            return String.format(l, mes, params);
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
