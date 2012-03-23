package org.xblackcat.rojac.gui.view.message;

import chrriis.dj.nativeswing.swtimpl.NativeComponent;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserAdapter;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserNavigationEvent;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.CustomBalloonTip;
import net.java.balloontip.positioners.LeftAbovePositioner;
import net.java.balloontip.styles.BalloonTipStyle;
import net.java.balloontip.styles.RoundedBalloonStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacDebugException;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.gui.theme.PreviewIcon;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.util.ClipboardUtils;
import org.xblackcat.rojac.util.LinkUtils;
import org.xblackcat.rojac.util.RojacWorker;
import org.xblackcat.rojac.util.SWTUtils;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 13.03.12 16:05
 *
 * @author xBlackCat
 */
class HyperlinkHandler implements HyperlinkListener {
    private static final Log log = LogFactory.getLog(HyperlinkHandler.class);

    private final Map<Element, BalloonTip> openBalloons = new HashMap<>();

    private final IAppControl appControl;
    private final JTextPane invoker;

    public HyperlinkHandler(IAppControl appControl, JTextPane invoker) {
        this.appControl = appControl;
        this.invoker = invoker;
    }

    public void hyperlinkUpdate(HyperlinkEvent e) {
        Point l = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(l, invoker);
        int mouseY = l.y;

        Element element = e.getSourceElement();
        BalloonTip balloonTip = openBalloons.get(element);
        if (balloonTip != null) {
            balloonTip.refreshLocation();
            return;
        }

        URL url = e.getURL();
        String stringUrl = e.getDescription();
        String text = LinkUtils.getUrlText(element);

        if (url == null) {
            // Invalid url. Try to parse it from text.

            try {
                url = new URL(text);
                // Url in the 'text' field, so assume that text in the 'description' field
                text = stringUrl;
                stringUrl = url.toExternalForm();
            } catch (MalformedURLException ex) {
                // url can not be obtained neither from text nor from description.
            }
        }

        Integer messageId = LinkUtils.getMessageIdFromUrl(stringUrl);
        if (messageId == null) {
            messageId = LinkUtils.getMessageIdFromUrl(text);
        }

        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            if (url == null) {
                // TODO: show error or standard dialog
            } else if (messageId == null) {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();

                    try {
                        desktop.browse(url.toURI());
                    } catch (IOException e1) {
                        log.error("Can not open url " + url.toExternalForm() + " in default browser");
                    } catch (URISyntaxException e1) {
                        log.error("Can not obtain URI of URL: " + url.toExternalForm());
                    }
                } else {
                    // TODO: show error dialog ???
                }
            } else {
                JPopupMenu menu = PopupMenuBuilder.getLinkMenu(
                        e.getURL(),
                        e.getDescription(),
                        LinkUtils.getUrlText(element),
                        appControl
                );
                menu.show(invoker, l.x, l.y);
            }
        } else if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {

            if (url == null) {
                // TODO: show error or standard dialog
            } else if (messageId == null) {
                showHtmlPreviewBalloon(url, element, mouseY);
            } else {
                showMessageBalloon(messageId);
            }
        }
    }

    private void showMessageBalloon(int messageId) {

    }

    private void showHtmlPreviewBalloon(final URL url, final Element sourceElement, final int mouseY) {
        final HtmlPagePreview linkPreview = new HtmlPagePreview(url);

        final BalloonTip balloonTip = setupBalloon(sourceElement, mouseY, linkPreview);

        linkPreview.setBalloonTip(balloonTip);
    }

    /**
     * Generate a balloon tip. Returns null if
     *
     * @param sourceElement
     * @param y
     * @param info          @return
     */
    private BalloonTip setupBalloon(final Element sourceElement, int y, JComponent info) {
        Rectangle r = getElementRectangle(sourceElement, y);

        Color color = new Color(0xFFFFCC);
        info.setBackground(color);
        BalloonTipStyle tipStyle = new RoundedBalloonStyle(5, 5, info.getBackground(), Color.black);

        final BalloonTip balloonTip = new CustomBalloonTip(invoker, info, r, tipStyle, new LeftAbovePositioner(15, 15), null);
        openBalloons.put(sourceElement, balloonTip);

        balloonTip.addHierarchyListener(new HierarchyListener() {
            @Override
            public void hierarchyChanged(HierarchyEvent e) {
                if (HierarchyEvent.SHOWING_CHANGED == (HierarchyEvent.SHOWING_CHANGED & e.getChangeFlags())) {
                    openBalloons.remove(sourceElement);
                }
            }
        });

        balloonTip.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                balloonTip.closeBalloon();
                openBalloons.remove(sourceElement);
            }
        });
        balloonTip.requestFocus();
        return balloonTip;
    }

    private Rectangle getElementRectangle(Element sourceElement, int y) {
        Rectangle r;
        try {
            int startOffset = sourceElement.getStartOffset();
            int endOffset = sourceElement.getEndOffset();

            Rectangle p0 = invoker.modelToView(startOffset);
            Rectangle p1 = invoker.modelToView(endOffset);

            if (p0.y == p1.y) {
                r = p0.union(p1);
            } else if (p0.y <= y && p0.y + p0.height >= y) {
                Dimension size = invoker.getSize();
                r = new Rectangle(p0.x, p0.y, size.width - p0.y, p0.height);
            } else {
                r = new Rectangle(0, p1.y, p1.y + p1.width, p1.height);
            }
        } catch (BadLocationException e1) {
            throw new RojacDebugException("Got invalid offset of element", e1);
        }
        return r;
    }

    /**
     * 13.03.12 16:54
     *
     * @author xBlackCat
     */
    static class HtmlPagePreview extends JPanel {
        private final JLabel previewImage;
        private final JLabel label;
        private BalloonTip balloonTip;

        HtmlPagePreview(final URL url) {
            super(new BorderLayout());

            label = new JLabel("<html><body><a href='" + url + "'>" + url + "</a>", SwingConstants.CENTER);
            label.setIcon(PreviewIcon.CopyToClipBoard);
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            label.setToolTipText(Message.PreviewLink_CopyToClipboard_Tooltip.get());

            add(label, BorderLayout.NORTH);
            previewImage = new JLabel(Message.PreviewLink_Load.get(), SwingConstants.CENTER);
            previewImage.setToolTipText(Message.PreviewLink_Load_Tooltip.get());
            previewImage.setIcon(PreviewIcon.Load);
            if (SWTUtils.isSwtEnabled) {
                add(previewImage, BorderLayout.CENTER);
            }

            final MouseListener clickListener = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    previewImage.removeMouseListener(this);
                    previewImage.setToolTipText(null);
                    previewImage.setText(Message.PreviewLink_Loading.get());
                    previewImage.setIcon(PreviewIcon.Loading);

                    new RojacWorker<Void, Image>() {
                        @Override
                        protected Void perform() throws Exception {
                            final JWebBrowser webBrowser = SWTUtils.prepareBrowser();

                            webBrowser.addWebBrowserListener(new WebBrowserAdapter() {
                                @Override
                                public void locationChanged(WebBrowserNavigationEvent e) {
                                    updateThumbnail();
                                }

                                @Override
                                public void loadingProgressChanged(WebBrowserEvent e) {
                                    updateThumbnail();
                                }

                                private void updateThumbnail() {
                                    BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);

                                    NativeComponent nativeComponent = webBrowser.getNativeComponent();
                                    nativeComponent.setSize(new Dimension(800, 600));
                                    nativeComponent.paintComponent(image);

                                    publish(image.getScaledInstance(400, 300, Image.SCALE_SMOOTH));
                                }
                            });

                            boolean navigated = webBrowser.navigate(url.toExternalForm());

                            return null;
                        }

                        @Override
                        protected void process(List<Image> chunks) {
                            for (Image image : chunks) {
                                setPreview(image);
                                balloonTip.refreshLocation();
                            }
                        }
                    }.execute();

                }
            };

            previewImage.addMouseListener(clickListener);

            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    ClipboardUtils.copyToClipboard(url.toExternalForm());
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

        }

        public void setPreview(Image image) {
            previewImage.setText(null);
            previewImage.setIcon(new ImageIcon(image));
        }

        public void setBalloonTip(BalloonTip balloonTip) {
            this.balloonTip = balloonTip;
        }
    }
}