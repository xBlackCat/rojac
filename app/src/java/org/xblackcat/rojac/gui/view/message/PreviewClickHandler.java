package org.xblackcat.rojac.gui.view.message;

import chrriis.dj.nativeswing.swtimpl.NativeComponent;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserAdapter;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserNavigationEvent;
import net.java.balloontip.BalloonTip;
import org.xblackcat.rojac.gui.theme.PreviewIcon;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.SWTUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * 23.03.12 11:36
 *
 * @author xBlackCat
 */
class PreviewClickHandler extends MouseAdapter {
    private final URL url;
    private final JLabel previewImage;
    private BalloonTip balloonTip;

    public PreviewClickHandler(URL url, JLabel previewImage1, BalloonTip balloonTip) {
        this.url = url;
        previewImage = previewImage1;
        this.balloonTip = balloonTip;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        previewImage.removeMouseListener(this);
        previewImage.setToolTipText(null);
        previewImage.setText(Message.PreviewLink_Loading.get());
        previewImage.setIcon(PreviewIcon.Loading);

        assert RojacUtils.checkThread(true);
        // Check and load correspond SWT library

        final JWebBrowser webBrowser = SWTUtils.getBrowser();

        webBrowser.addWebBrowserListener(new WebBrowserAdapter() {
            @Override
            public void locationChanged(WebBrowserNavigationEvent e) {
                updateThumbnail(webBrowser);
            }

            @Override
            public void loadingProgressChanged(WebBrowserEvent e) {
                updateThumbnail(webBrowser);
            }
        });

        webBrowser.navigate("about:blank");
        webBrowser.navigate(url.toExternalForm());
    }

    private void updateThumbnail(JWebBrowser webBrowser) {
        BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);

        NativeComponent nativeComponent = webBrowser.getNativeComponent();
        nativeComponent.setSize(SWTUtils.DEFAULT_BROWSER_SIZE);
        nativeComponent.paintComponent(image);

        previewImage.setText(null);
        previewImage.setIcon(new ImageIcon(image.getScaledInstance(400, 300, Image.SCALE_SMOOTH)));
        if (balloonTip != null) {
            balloonTip.refreshLocation();
        }
    }
}
