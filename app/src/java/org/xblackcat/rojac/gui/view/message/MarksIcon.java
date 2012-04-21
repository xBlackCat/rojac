package org.xblackcat.rojac.gui.view.message;

import org.xblackcat.rojac.data.Mark;

import javax.swing.*;
import java.awt.*;

/**
 * Post reputation icon. Group all marks set to the post.
 *
 * @author xBlackCat
 */
class MarksIcon implements Icon {
    private final int GAP = 2;

    private final Icon[] icons;
    private final int width;
    private final int height;


    public MarksIcon(Mark... marks) {
        int width = 0;
        int height = 0;

        icons = new Icon[marks.length];

        if (marks.length > 0) {
            width = -GAP;
            for (int i = 0, marksLength = marks.length; i < marksLength; i++) {
                ImageIcon icon = marks[i].getIcon();
                icons[i] = icon;

                width += GAP;
                width += icon.getIconWidth();

                height = Math.max(height, icon.getIconHeight());
            }
        }

        this.height = height;
        this.width = width;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        int offset_x = 0;

        for (Icon i : icons) {
            i.paintIcon(c, g, x + offset_x, y);

            offset_x += i.getIconWidth() + GAP;
        }
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }
}
