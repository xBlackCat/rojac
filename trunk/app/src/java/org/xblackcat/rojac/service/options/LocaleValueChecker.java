package org.xblackcat.rojac.service.options;

import org.xblackcat.utils.ResourceUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

/**
 * @author xBlackCat
 */

final class LocaleValueChecker implements IValueChecker<Locale> {
    private final Collection<Locale> locales;

    LocaleValueChecker() {
        Locale[] locales;
        try {
            locales = ResourceUtils.localesForBundle("/i18n/messages");
        } catch (IOException e) {
            throw new RuntimeException("Can not initialize locales list.", e);
        }
        this.locales = new HashSet<Locale>(Arrays.asList(locales));
    }

    @Override
    public Locale[] getPossibleValues() {
        return locales.toArray(new Locale[locales.size()]);
    }

    @Override
    public String getValueDescription(Locale v) throws IllegalArgumentException {
        return v != null ? v.getDisplayName() : "error";
    }

    @Override
    public boolean isValueCorrect(Locale v) {
        return locales.contains(v);
    }
}
