package org.xblackcat.rojac.gui.view.message;

import net.java.balloontip.BalloonTip;
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
    private final JLabel thumbnail;
    private final YoutubeVideoInfo videoInfo;

    YoutubePagePreview(final URL url, final YoutubeVideoInfo videoInfo) {
        super(url.toExternalForm(), videoInfo.getVideoTitle());
        this.videoInfo = videoInfo;

        thumbnail = new JLabel(videoInfo.getThumbnail());

        add(thumbnail, BorderLayout.CENTER);

        JPanel statisticPane = new JPanel(new BorderLayout(5, 5));

        JPanel ratingPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 2));
        JLabel likes = new JLabel(String.valueOf(videoInfo.getNumLikes()), PreviewIcon.ThumbUp, SwingConstants.CENTER);
        JLabel dislikes = new JLabel(String.valueOf(videoInfo.getNumDislikes()), PreviewIcon.ThumbDown, SwingConstants.CENTER);
        likes.setForeground(new Color(0, 192, 0));
        dislikes.setForeground(new Color(255, 64, 64));
        ratingPane.add(likes);
        ratingPane.add(dislikes);

        statisticPane.add(ratingPane, BorderLayout.EAST);

        JLabel viewCount = new JLabel(Message.PreviewLink_Youtube_ViewCount.get(videoInfo.getViewCount()));
        statisticPane.add(viewCount, BorderLayout.WEST);

        JLabel duration = new JLabel(
                Message.PreviewLink_Youtube_Duration.get(
                        MessageUtils.formatDuration(videoInfo.getDuration())
                ),
                SwingConstants.CENTER
        );
        statisticPane.add(duration, BorderLayout.NORTH);

        add(statisticPane, BorderLayout.SOUTH);
    }

    @Override
    public void initializePreview(final BalloonTip balloonTip) {
        thumbnail.setCursor(RojacCursor.ZoomIn.get());
        thumbnail.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (thumbnail.getIcon() == videoInfo.getThumbnail()) {
                    thumbnail.setIcon(videoInfo.getHqThumbnail());
                    thumbnail.setCursor(RojacCursor.ZoomOut.get());
                } else {
                    thumbnail.setIcon(videoInfo.getThumbnail());
                    thumbnail.setCursor(RojacCursor.ZoomIn.get());
                }

                if (balloonTip != null) {
                    balloonTip.refreshLocation();
                }
            }
        });
    }
}
