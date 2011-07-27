package org.xblackcat.rojac.service.options.converter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Locale;

/**
 * @author xBlackCat
 */

public class LocaleConverter implements IConverter<Locale> {
    private static final Log log = LogFactory.getLog(LocaleConverter.class);

    public Locale convert(String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        }

        String[] loc = s.trim().split("-");
        if (loc.length == 2 && loc[0].length() == 2 && loc[1].length() == 2) {
            return new Locale(loc[0], loc[1]);
        }

        return Locale.ROOT;
    }

    public String toString(Locale o) {
        if (o == null) {
            return null;
        }

        return String.format("%s-%s", o.getLanguage(), o.getCountry());
    }


}
