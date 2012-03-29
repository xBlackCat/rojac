package org.xblackcat.rojac.util;

import net.java.balloontip.styles.BalloonTipStyle;
import net.java.balloontip.styles.RoundedBalloonStyle;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.i18n.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * 28.03.12 16:45
 *
 * @author xBlackCat
 */
public class BalloonTipUtils {
    public static BalloonTipStyle createTipStyle(Color color) {
        return new RoundedBalloonStyle(5, 5, color, Color.black);
    }

    public static JButton balloonTipCloseButton() {
        return balloonTipCloseButton(null);
    }

    public static JButton balloonTipCloseButton(final Runnable onClose) {
        JButton closeButton = WindowsUtils.setupImageButton("close", new AButtonAction(Message.Button_Close) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (onClose != null) {
                    onClose.run();
                }
            }
        });
        closeButton.setBorder(null);
        closeButton.setContentAreaFilled(false);
        return closeButton;
    }
}
