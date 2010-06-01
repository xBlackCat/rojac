package org.xblackcat.rojac.service.options;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.util.RojacUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

/**
 * @author xBlackCat
 */

final class LocaleValueChecker implements IValueChecker<Locale> {
    private static final Log log = LogFactory.getLog(LocaleValueChecker.class);
    private final Collection<Locale> locales;

    LocaleValueChecker() {
        if (log.isTraceEnabled()) {
            log.trace("Initialize locale holder");
        }
        Locale[] locales;
        try {
            locales = RojacUtils.localesForBundle("/i18n/messages");
        } catch (Throwable e) {
            if (log.isWarnEnabled()) {
                log.warn("Can not obtain list of available locales.", e);
            }
            throw new RuntimeException("Can not load a locales list", e);
        }
        if (log.isTraceEnabled()) {
            log.trace("Found " + locales.length + " locales");

            for (Locale l : locales) {
                log.trace("Available locale: " + l);
            }
        }
        this.locales = new HashSet<Locale>(Arrays.asList(locales));
    }

    @Override
    public Locale[] getPossibleValues() {
        return locales.toArray(new Locale[locales.size()]);
    }

    @Override
    public String getValueDescription(Locale v) throws IllegalArgumentException {
        if (v == null) {
            return "error";
        } else if (v == Locale.ROOT) {
            return "Default";
        } else {
            return v.getDisplayName();
        }
    }

    @Override
    public boolean isValueCorrect(Locale v) {
        return locales.contains(v) || v == Locale.ROOT;
    }
}
