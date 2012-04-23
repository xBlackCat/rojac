package org.xblackcat.rojac.gui.hint;

import org.xblackcat.rojac.util.BalloonTipUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * 21.04.12 13:16
 *
 * @author xBlackCat
 */
class MessageHint extends JPanel {
    public MessageHint(Icon icon, JComponent comp, Runnable onClose) {
        super(new BorderLayout(5, 0));

        setBorder(new EmptyBorder(2, 5, 2, 5));

        Color color = BalloonTipUtils.TIP_BACKGROUND;
        setBackground(color);
        setAlignmentX(0);

        final JLabel iconLabel = new JLabel(icon);
        add(iconLabel, BorderLayout.WEST);

        add(comp, BorderLayout.CENTER);

        add(BalloonTipUtils.balloonTipCloseButton(onClose), BorderLayout.EAST);
    }
}
