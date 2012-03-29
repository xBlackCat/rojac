package org.xblackcat.rojac.gui.view.message;

import chrriis.dj.nativeswing.swtimpl.NativeComponent;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserAdapter;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserNavigationEvent;
import net.java.balloontip.BalloonTip;
import org.apache.commons.lang3.StringUtils;
import org.xblackcat.rojac.gui.theme.PreviewIcon;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.util.SWTUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * 13.03.12 16:54
 *
 * @author xBlackCat
 */
class HtmlPagePreview extends AnUrlInfoPane {
    private final URL url;

    HtmlPagePreview(final URL url, final Runnable onClose) {
        super(url.toExternalForm(), url.toString(), onClose);
        this.url = url;
    }

    @Override
    public void loadUrlInfo(BalloonTip balloonTip) {
        if (SWTUtils.isSwtEnabled) {
            infoLabel.setText(Message.PreviewLink_Load.get());
            infoLabel.setToolTipText(Message.PreviewLink_Load_Tooltip.get());
            infoLabel.setIcon(PreviewIcon.Load);

            infoLabel.addMouseListener(new PreviewClickHandler(balloonTip));
        } else {
            infoLabel.setIcon(PreviewIcon.DisabledLarge);
            infoLabel.setText("Browser component is not installed");
            infoLabel.setToolTipText("Browser component is not installed");
        }
        balloonTip.refreshLocation();
    }

    /**
     * 23.03.12 11:36
     *
     * @author xBlackCat
     */
    private class PreviewClickHandler extends MouseAdapter {
        private BalloonTip balloonTip;

        public PreviewClickHandler(BalloonTip balloonTip) {
            assert balloonTip != null;
            this.balloonTip = balloonTip;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            infoLabel.removeMouseListener(this);

            infoLabel.setToolTipText(null);
            infoLabel.setText(Message.PreviewLink_Loading.get());
            infoLabel.setIcon(PreviewIcon.Loading);

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

            infoLabel.setText(null);

            String pageTitle = webBrowser.getPageTitle();
            if (StringUtils.isNotBlank(pageTitle) && !"about:blank".equals(pageTitle)) {
                infoLabel.setToolTipText(pageTitle);
                updateDescription(pageTitle);
            }

            infoLabel.setIcon(new ImageIcon(image.getScaledInstance(previewSize.getWidth(), previewSize.getHeight(), Image.SCALE_SMOOTH)));
            infoLabel.setPreferredSize(new Dimension(previewSize.getWidth(), previewSize.getHeight()));
            balloonTip.refreshLocation();
        }
    }
}
