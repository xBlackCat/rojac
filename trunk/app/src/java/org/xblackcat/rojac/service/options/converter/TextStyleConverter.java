package org.xblackcat.rojac.service.options.converter;

import org.apache.commons.lang3.StringUtils;
import org.xblackcat.rojac.gui.theme.TextStyle;

import java.awt.*;
import java.util.Locale;

/**
 * @author xBlackCat
 */

public class TextStyleConverter implements IConverter<TextStyle> {
    @Override
    public TextStyle convert(String s) {
        if (StringUtils.isBlank(s)) {
            return TextStyle.DEFAULT;
        }

        Font f = null;
        Color fg = null;
        Color bg = null;

        String[] parts = s.split(";");
        if (parts.length > 0) {
            // Have font property
            f = Font.decode(parts[0]);

            if (parts.length > 1) {
                // Have foreground
                fg = Color.decode(parts[1]);

                if (parts.length > 2) {
                    bg = Color.decode(parts[2]);
                }
            }
        }

        return new TextStyle(f, fg, bg);
    }

    @Override
    public String toString(TextStyle ts) {
        StringBuilder obj = new StringBuilder();

        // Store font info
        Font f = ts.getFont();
        if (f != null) {
            obj.append(f.getFontName(Locale.ROOT));

            if (f.getStyle() != Font.PLAIN) {
                obj.append('-');
                if ((f.getStyle() & Font.BOLD) == Font.BOLD) {
                    obj.append("bold");
                }
                if ((f.getStyle() & Font.ITALIC) == Font.ITALIC) {
                    obj.append("italic");
                }
            }
            obj.append('-');
            obj.append(f.getSize());
        }
        obj.append(';');

        // Store foreground
        Color fg = ts.getForeground();
        if (fg != null) {
            obj.append(fg.getRGB());
        }
        obj.append(';');

        // Store background
        Color bg = ts.getBackground();
        if (bg != null) {
            obj.append(bg.getRGB());
        }

        return obj.toString();
    }
}
