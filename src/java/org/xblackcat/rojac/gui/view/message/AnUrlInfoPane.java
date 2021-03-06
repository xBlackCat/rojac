package org.xblackcat.rojac.gui.view.message;

import net.java.balloontip.BalloonTip;
import org.apache.commons.lang3.StringEscapeUtils;
import org.xblackcat.rojac.gui.theme.PreviewIcon;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.util.ClipboardUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;

/**
 * 23.03.12 16:36
 *
 * @author xBlackCat
 */
abstract class AnUrlInfoPane extends JPanel {
    protected final JLabel copyLinkLabel;
    protected final String urlString;
    protected final Runnable onClose;
    protected final JLabel infoLabel = new JLabel(null, null, SwingConstants.CENTER);

    public AnUrlInfoPane(String urlString, String text) {
        this(urlString, text, null);
    }

    public AnUrlInfoPane(String urlString, String text, Runnable onClose) {
        super(new BorderLayout(10, 10));
        this.urlString = urlString;
        this.onClose = onClose;

        copyLinkLabel = new JLabel("", SwingConstants.CENTER);
        copyLinkLabel.setIcon(PreviewIcon.CopyToClipBoard);
        copyLinkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        copyLinkLabel.setToolTipText(Message.PreviewLink_CopyToClipboard_Tooltip.get());

        updateDescription(text);

        add(copyLinkLabel, BorderLayout.NORTH);
    }

    protected final void updateDescription(String text) {
        copyLinkLabel.setText("<html><body><a href='" + urlString + "'>" + StringEscapeUtils.escapeHtml4(text) + "</a>");
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

                Timer timer = new Timer((int) TimeUnit.SECONDS.toMillis(3), e1 -> balloonTip.closeBalloon());
                timer.setRepeats(false);
                timer.start();
            }
        });


        add(infoLabel, BorderLayout.CENTER);
        if (Property.LINK_PREVIEW_ENABLED.get()) {
            infoLabel.setIcon(PreviewIcon.WaitingLarge);
            infoLabel.setText(Message.PreviewLink_Loading.get());
            loadUrlInfo(balloonTip);
        } else {
            infoLabel.setIcon(PreviewIcon.DisabledLarge);
            infoLabel.setText(Message.PreviewLink_Disabled.get());
            infoLabel.setToolTipText(Message.PreviewLink_Disabled_Tooltip.get());
        }

        balloonTip.refreshLocation();
    }

    protected abstract void loadUrlInfo(BalloonTip balloonTip);

    public Runnable getOnClose() {
        return onClose;
    }
}
