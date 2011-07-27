package org.xblackcat.rojac.service.options.converter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.theme.IconPack;

/**
 * @author xBlackCat
 */

public class IconPackConverter implements IConverter<IconPack> {
    private static final Log log = LogFactory.getLog(IconPack.class);

    public IconPack convert(String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        }

        String[] loc = s.trim().split(":");
        if (loc.length == 3) {
            return new IconPack(loc[0], loc[2], loc[1]);
        }

        log.error("Invalid IconPack: " + s);

        return null;
    }

    public String toString(IconPack o) {
        if (o == null) {
            return null;
        }

        return String.format("%s:%s:%s", o.getName(),o.getExtension(), o.getPathPrefix());
    }


}
