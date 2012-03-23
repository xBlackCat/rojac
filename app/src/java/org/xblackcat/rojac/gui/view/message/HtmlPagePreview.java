package org.xblackcat.rojac.gui.view.message;

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
    HtmlPagePreview(final URL url, final Runnable onClose) {
        super(url.toExternalForm(), url.toString(), onClose);

        if (SWTUtils.isSwtEnabled) {
            JLabel centralPane1;
            centralPane1 = new JLabel(Message.PreviewLink_Load.get(), SwingConstants.CENTER);
            centralPane1.setToolTipText(Message.PreviewLink_Load_Tooltip.get());
            centralPane1.setIcon(PreviewIcon.Load);
            final MouseListener clickListener = new PreviewClickHandler(url, centralPane1, balloonTip);

            centralPane1.addMouseListener(clickListener);
            add(centralPane1, BorderLayout.CENTER);
        }
    }
}
