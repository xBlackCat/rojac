package org.xblackcat.rojac.i18n;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.options.Property;

import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author xBlackCat Date: 23.06.11
 */
public class LocaleControl {
    private static final Log log = LogFactory.getLog(LocaleControl.class);

    private static final LocaleControl INSTANCE = new LocaleControl();
    private static final Charset SOURCE_CHARSET = Charset.forName("ISO-8859-1");
    private static final Charset REAL_CHARSET = Charset.forName("UTF-8");

    public static LocaleControl getInstance() {
        return INSTANCE;
    }

    private final Map<String, ResourceBundle> bundlesCache = new HashMap<>();

    private final Lock readLock;
    private final Lock writeLock;

    private boolean strict;
    private Locale locale;

    private LocaleControl() {
        ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
        readLock = rwl.readLock();
        writeLock = rwl.writeLock();

        setLocale(null);
    }

    /**
     * Set the specified locale for messages
     *
     * @param locale locale to set.
     * @throws IllegalArgumentException is thrown if invalid locale is specified.
     */
    public void setLocale(Locale locale) throws IllegalArgumentException {
        setLocale(locale, false);
    }

    /**
     * Set the specified locale for messages
     *
     * @param locale locale to set.
     * @param strict
     * @throws IllegalArgumentException is thrown if invalid locale is specified.
     */
    public void setLocale(Locale locale, boolean strict) throws IllegalArgumentException {
        writeLock.lock();
        try {
            this.locale = locale;
            this.strict = strict;
            bundlesCache.clear();
        } finally {
            writeLock.unlock();
        }
    }

    public Locale getLocale() {
        return locale;
    }

    ResourceBundle getBundle(String base) {
        readLock.lock();

        ResourceBundle bundle;
        try {
            bundle = bundlesCache.get(base);
        } finally {
            readLock.unlock();
        }
        if (bundle != null) {
            return bundle;
        }

        // Bundle not yet initialized

        if (locale != null) {
            bundle = ResourceBundle.getBundle("i18n/" + base, locale);
            if (!bundle.getLocale().equals(locale)) {
                if (strict) {
                    throw new IllegalArgumentException("Can not load resources for " + locale + " locale.");
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("Can not initialize locale " + locale + ". The " + bundle.getLocale() + " will be used.");
                    }
                }
            }
        } else {
            bundle = ResourceBundle.getBundle("i18n/" + base);
        }

        writeLock.lock();
        try {
            bundlesCache.put(base, bundle);
        } finally {
            writeLock.unlock();
        }

        return bundle;
    }

    String getString(String bundle, String key, Object... arguments) {
        String mes;

        ResourceBundle messages = getBundle(bundle);
        try {
            mes = messages.getString(key);
        } catch (MissingResourceException e) {
            if (Property.ROJAC_DEBUG_MODE.get()) {
                mes = key + ": " + ArrayUtils.toString(arguments);
            } else {
                throw e;
            }
        }

        if (mes != null) {
            mes = new String(mes.getBytes(SOURCE_CHARSET), REAL_CHARSET);

            return String.format(locale, mes, arguments);
        } else {
            return key;
        }
    }

}
