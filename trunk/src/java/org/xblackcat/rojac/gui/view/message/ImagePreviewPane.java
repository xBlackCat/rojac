package org.xblackcat.rojac.gui.view.message;

import net.java.balloontip.BalloonTip;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.util.RojacWorker;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Iterator;

/**
* 29.03.12 10:15
*
* @author xBlackCat
*/
class ImagePreviewPane extends AnUrlInfoPane {
    private final URL url;

    public ImagePreviewPane(URL url) {
        super(url.toExternalForm(), url.toExternalForm());
        this.url = url;
    }

    @Override
    protected void loadUrlInfo(final BalloonTip balloonTip) {
        new RojacWorker<Void, Icon>() {
            @Override
            protected Void perform() throws Exception {
                PreviewSize size = Property.LINK_PREVIEW_IMAGE_SIZE.get();

                Image image = ImageIO.read(url);

                if (image != null) {
                    Image thumb = image;
                    int width = image.getWidth(null);
                    int height = image.getHeight(null);

                    if (height > size.getHeight() || width > size.getWidth()) {
                        if (height * size.getWidth() > width * size.getHeight()) {
                            thumb = image.getScaledInstance(-1, size.getHeight(), Image.SCALE_SMOOTH);
                        } else {
                            thumb = image.getScaledInstance(size.getWidth(), -1, Image.SCALE_SMOOTH);
                        }
                    }

                    publish(new ImageIcon(thumb));
                }

                return null;
            }

            @Override
            protected void process(java.util.List<Icon> chunks) {
                Iterator<Icon> iterator = chunks.iterator();
                if (iterator.hasNext()) {
                    final Icon icon = iterator.next();
                    infoLabel.setIcon(icon);
                    infoLabel.setText(null);
                    infoLabel.setToolTipText(url.toExternalForm());
                    balloonTip.refreshLocation();
                }
            }
        }.execute();
    }
}
