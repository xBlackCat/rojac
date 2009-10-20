package org.xblackcat.rojac.service.options.converter;

import org.apache.commons.lang.StringUtils;

import java.awt.*;

/**
 * @author xBlackCat
 */

public class DimensionConverter implements IConverter<Dimension> {
    public Dimension convert(String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        }

        try {
            String[] axises = s.split("\\s*,\\s*");
            if (axises.length == 2) {
                return new Dimension(Integer.parseInt(axises[0]), Integer.parseInt(axises[1]));
            }
        } catch (NumberFormatException e) {
            // Can not parse. Return null
        }
        return null;
    }

    public String toString(Dimension o) {
        if (o == null) {
            return null;
        }

        return String.format("%d,%d", o.width, o.height);
    }
}
