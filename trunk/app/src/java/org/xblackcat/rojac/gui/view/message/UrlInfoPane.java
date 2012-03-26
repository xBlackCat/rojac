package org.xblackcat.rojac.gui.view.message;

import net.java.balloontip.BalloonTip;
import org.apache.commons.lang3.StringEscapeUtils;
import org.xblackcat.rojac.gui.theme.PreviewIcon;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.util.ClipboardUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;

/**
 * 23.03.12 16:36
 *
 * @author xBlackCat
 */
public class UrlInfoPane extends JPanel {
    protected BalloonTip balloonTip;

    public UrlInfoPane(final String url, String text) {
        this(url, text, null);
    }

    public UrlInfoPane(final String url, String text, final Runnable onClose) {
        super(new BorderLayout(10, 10));

        final JLabel label = new JLabel("<html><body><a href='" + url + "'>" + StringEscapeUtils.escapeHtml4(text) + "</a>", SwingConstants.CENTER);
        label.setIcon(PreviewIcon.CopyToClipBoard);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.setToolTipText(Message.PreviewLink_CopyToClipboard_Tooltip.get());
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ClipboardUtils.copyToClipboard(url);
                onClose.run();
                balloonTip.setContents(new JLabel(Message.PreviewLink_LinkCopied.get()));
                balloonTip.setVisible(true);

                Timer timer = new Timer((int) TimeUnit.SECONDS.toMillis(3), new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        balloonTip.closeBalloon();
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        });

        add(label, BorderLayout.NORTH);
    }

    public void setBalloonTip(BalloonTip balloonTip) {
        this.balloonTip = balloonTip;
    }
}
