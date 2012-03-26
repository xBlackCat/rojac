package org.xblackcat.rojac.gui.view.message;

import net.java.balloontip.BalloonTip;
import org.xblackcat.rojac.gui.theme.PreviewIcon;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.util.SWTUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.net.URL;

/**
 * 13.03.12 16:54
 *
 * @author xBlackCat
 */
class HtmlPagePreview extends UrlInfoPane {
    private final JLabel centralPane;
    private final URL url;

    HtmlPagePreview(final URL url, final Runnable onClose) {
        super(url.toExternalForm(), url.toString(), onClose);
        this.url = url;

        centralPane = new JLabel(Message.PreviewLink_Load.get(), SwingConstants.CENTER);
        if (SWTUtils.isSwtEnabled) {
            centralPane.setToolTipText(Message.PreviewLink_Load_Tooltip.get());
            centralPane.setIcon(PreviewIcon.Load);
            add(centralPane, BorderLayout.CENTER);
        }
    }

    @Override
    public void initialize(BalloonTip balloonTip) {
        super.initialize(balloonTip);

        final MouseListener clickListener = new PreviewClickHandler(url, centralPane, balloonTip);

        centralPane.addMouseListener(clickListener);
    }
}
