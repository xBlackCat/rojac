package org.xblackcat.rojac.gui.view.message;

import org.xblackcat.rojac.gui.component.RojacCursor;
import org.xblackcat.rojac.gui.theme.PreviewIcon;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.util.MessageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

/**
 * 13.03.12 16:54
 *
 * @author xBlackCat
 */
class YoutubePagePreview extends UrlInfoPane {
    YoutubePagePreview(final URL url, final YoutubeVideoInfo vi) {
        super(url.toExternalForm(), vi.getVideoTitle());

        final JLabel thumbnail = new JLabel(vi.getThumbnail());
        thumbnail.setCursor(RojacCursor.ZoomIn.get());
        thumbnail.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (thumbnail.getIcon() == vi.getThumbnail()) {
                    thumbnail.setIcon(vi.getHqThumbnail());
                    thumbnail.setCursor(RojacCursor.ZoomOut.get());
                } else {
                    thumbnail.setIcon(vi.getThumbnail());
                    thumbnail.setCursor(RojacCursor.ZoomIn.get());
                }

                if (balloonTip != null) {
                    balloonTip.refreshLocation();
                }
            }
        });

        add(thumbnail, BorderLayout.CENTER);

        JPanel statisticPane = new JPanel(new BorderLayout(5, 5));

        JPanel ratingPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 2));
        JLabel likes = new JLabel(String.valueOf(vi.getNumLikes()), PreviewIcon.ThumbUp, SwingConstants.CENTER);
        JLabel dislikes = new JLabel(String.valueOf(vi.getNumDislikes()), PreviewIcon.ThumbDown, SwingConstants.CENTER);
        likes.setForeground(new Color(0, 192, 0));
        dislikes.setForeground(new Color(255, 64, 64));
        ratingPane.add(likes);
        ratingPane.add(dislikes);

        statisticPane.add(ratingPane, BorderLayout.EAST);

        JLabel viewCount = new JLabel(Message.PreviewLink_Youtube_ViewCount.get(vi.getViewCount()));
        statisticPane.add(viewCount, BorderLayout.WEST);

        JLabel duration = new JLabel(
                Message.PreviewLink_Youtube_Duration.get(
                        MessageUtils.formatDuration(vi.getDuration())
                ),
                SwingConstants.CENTER
        );
        statisticPane.add(duration, BorderLayout.NORTH);

        add(statisticPane, BorderLayout.SOUTH);
    }
}
