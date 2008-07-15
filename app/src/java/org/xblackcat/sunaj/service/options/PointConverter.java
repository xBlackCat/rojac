package org.xblackcat.sunaj.service.options;

import org.apache.commons.lang.StringUtils;

import java.awt.*;

/**
 * Date: 15 лип 2008
 *
 * @author xBlackCat
 */

public class PointConverter implements IConverter<Point> {
    public Point convert(String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        }

        try {
            String[] axises = s.split("\\s*,\\s*");
            if (axises.length == 2) {
                return new Point(Integer.parseInt(axises[0]), Integer.parseInt(axises[1]));
            }
        } catch (NumberFormatException e) {
            // Can not parse. Return null
        }
        return null;
    }

    public String toString(Point o) {
        if (o == null) {
            return null;
        }

        return String.format("%d,%d", o.x, o.y);
    }
}