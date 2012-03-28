package org.xblackcat.rojac.gui.view.message;

import chrriis.dj.nativeswing.swtimpl.NativeComponent;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserAdapter;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserNavigationEvent;
import net.java.balloontip.BalloonTip;
import org.xblackcat.rojac.gui.theme.PreviewIcon;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.options.Property;
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
        assert balloonTip != null;
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
                webBrowser.removeWebBrowserListener(this);
                webBrowser.navigate("about:blank");
            }

            @Override
            public void loadingProgressChanged(WebBrowserEvent e) {
                updateThumbnail(webBrowser);
            }
        });
        webBrowser.navigate(url.toExternalForm());
    }

    private void updateThumbnail(JWebBrowser webBrowser) {
        PreviewSize size = Property.LINK_PREVIEW_PAGE_SIZE.get();
        PreviewSize previewSize = Property.LINK_PREVIEW_PAGE_THUMBNAIL_SIZE.get();

        BufferedImage image = new BufferedImage(size.getWidth(), size.getHeight(), BufferedImage.TYPE_INT_ARGB);

        webBrowser.setSize(SWTUtils.DEFAULT_BROWSER_SIZE);
        NativeComponent nativeComponent = webBrowser.getNativeComponent();
        nativeComponent.setSize(SWTUtils.DEFAULT_BROWSER_SIZE);
        nativeComponent.paintComponent(image);

        previewImage.setText(null);
        previewImage.setIcon(new ImageIcon(image.getScaledInstance(previewSize.getWidth(), previewSize.getHeight(), Image.SCALE_SMOOTH)));
        previewImage.setPreferredSize(new Dimension(previewSize.getWidth(), previewSize.getHeight()));
        balloonTip.refreshLocation();
    }
}
