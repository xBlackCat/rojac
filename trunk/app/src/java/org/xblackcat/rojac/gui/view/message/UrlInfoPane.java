package org.xblackcat.rojac.gui.view.message;

import net.java.balloontip.BalloonTip;
import org.apache.commons.lang3.StringEscapeUtils;
import org.xblackcat.rojac.gui.theme.PreviewIcon;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.options.Property;
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
abstract class UrlInfoPane extends JPanel {
    protected final JLabel copyLinkLabel;
    protected final String urlString;
    protected final Runnable onClose;

    public UrlInfoPane(String urlString, String text) {
        this(urlString, text, null);
    }

    public UrlInfoPane(String urlString, String text, Runnable onClose) {
        super(new BorderLayout(10, 10));
        this.urlString = urlString;
        this.onClose = onClose;

        copyLinkLabel = new JLabel("<html><body><a href='" + urlString + "'>" + StringEscapeUtils.escapeHtml4(text) + "</a>", SwingConstants.CENTER);
        copyLinkLabel.setIcon(PreviewIcon.CopyToClipBoard);
        copyLinkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        copyLinkLabel.setToolTipText(Message.PreviewLink_CopyToClipboard_Tooltip.get());

        add(copyLinkLabel, BorderLayout.NORTH);
    }

    public final void initialize(final BalloonTip balloonTip) {
        copyLinkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ClipboardUtils.copyToClipboard(urlString);
                if (onClose != null) {
                    onClose.run();
                }
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

        if (Property.LINK_PREVIEW_ENABLED.get()) {
            initializePreview(balloonTip);
        }
    }

    protected abstract void initializePreview(BalloonTip balloonTip);

    public Runnable getOnClose() {
        return onClose;
    }
}
