package org.xblackcat.sunaj.i18n;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.sunaj.util.ResourceUtils;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Date: 23 груд 2007
 *
 * @author xBlackCat
 */

public enum Message {
    MAIN_WINDOW_TITLE;

    private static final Log log = LogFactory.getLog(Message.class);

    // Constants
    private static final String LOCALIZATION_BUNDLE_NAME = "localization/messages";

    private static ResourceBundle messages;

    private static final Lock readLock;
    private static final Lock writeLock;

    static {
        ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
        readLock = rwl.readLock();
        writeLock = rwl.writeLock();

        setLocale(null);
    }

    /**
     * Selects the specified locales for messages
     *
     * @param loc
     */
    public static void setLocale(Locale loc) throws IllegalArgumentException {
        ResourceBundle m;
        if (loc != null) {
            m = ResourceBundle.getBundle(LOCALIZATION_BUNDLE_NAME, loc);
            if (!m.getLocale().equals(loc)) {
                throw new IllegalArgumentException("Can not load resources for " + loc + " locale.");
            }
        } else {
            m = ResourceBundle.getBundle(LOCALIZATION_BUNDLE_NAME);
        }

        writeLock.lock();
        try {
            messages = m;
        } finally {
            writeLock.unlock();
        }
    }

    public String getMessage(Object... params) throws MissingResourceException {
        String key = ResourceUtils.constantToProperty(name());

        String mes;

        readLock.lock();
        try {
            mes = messages.getString(key);
        } finally {
            readLock.unlock();
        }

        return String.format(mes, params).trim();
    }

    public String toString() {
        return getMessage();
    }
}
