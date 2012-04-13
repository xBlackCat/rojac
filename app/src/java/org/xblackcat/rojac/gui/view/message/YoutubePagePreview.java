package org.xblackcat.rojac.gui.view.message;

import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.util.ServiceException;
import net.java.balloontip.BalloonTip;
import org.xblackcat.rojac.gui.component.RojacCursor;
import org.xblackcat.rojac.gui.theme.PreviewIcon;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.util.MessageUtils;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.RojacWorker;
import org.xblackcat.rojac.util.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

/**
 * 13.03.12 16:54
 *
 * @author xBlackCat
 */
class YoutubePagePreview extends AnUrlInfoPane {
    private final String videoId;

    YoutubePagePreview(final URL url, String videoId) {
        super(url.toExternalForm(), url.toExternalForm());

        this.videoId = videoId;
    }

    @Override
    public void loadUrlInfo(final BalloonTip balloonTip) {
        new RojacWorker<Void, YoutubeVideoInfo>() {
            @Override
            protected Void perform() throws Exception {
                YouTubeService service = new YouTubeService(RojacUtils.VERSION_STRING);

                try {
                    String videoEntryUrl = "http://gdata.youtube.com/feeds/api/videos/" + videoId;
                    VideoEntry ve = service.getEntry(new URL(videoEntryUrl), VideoEntry.class);

                    publish(new YoutubeVideoInfo(ve));
                } catch (ServiceException e) {
                    // Can not load video
                    publish((YoutubeVideoInfo) null);
                }

                return null;
            }

            @Override
            protected void process(List<YoutubeVideoInfo> chunks) {
                Iterator<YoutubeVideoInfo> iterator = chunks.iterator();
                if (iterator.hasNext()) {
                    showInfo(iterator.next());
                }
            }

            private void showInfo(final YoutubeVideoInfo videoInfo) {
                if (videoInfo == null) {
                    infoLabel.setText(Message.PreviewLink_Youtube_VideoNotFound.get());
                    infoLabel.setIcon(PreviewIcon.DisabledLarge);
                    return;
                }

                if (Property.LINK_PREVIEW_YOUTUBE_SIZE.get() == YoutubePreviewSize.Normal) {
                    infoLabel.setIcon(videoInfo.getThumbnail());
                } else {
                    infoLabel.setIcon(videoInfo.getHqThumbnail());
                }

                infoLabel.setText(null);
                infoLabel.setToolTipText(videoInfo.getVideoTitle());
                updateDescription(videoInfo.getVideoTitle());

                infoLabel.setCursor(RojacCursor.ZoomIn.get());
                infoLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (infoLabel.getIcon() == videoInfo.getThumbnail()) {
                            infoLabel.setIcon(videoInfo.getHqThumbnail());
                            infoLabel.setCursor(RojacCursor.ZoomOut.get());
                        } else {
                            infoLabel.setIcon(videoInfo.getThumbnail());
                            infoLabel.setCursor(RojacCursor.ZoomIn.get());
                        }

                        if (balloonTip != null) {
                            balloonTip.refreshLocation();
                        }
                    }
                });

                JPanel statisticPane = new JPanel(new BorderLayout(5, 5));

                if (videoInfo.hasRating()) {
                    JPanel ratingPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 2));
                    JLabel likes = new JLabel(String.valueOf(videoInfo.getNumLikes()), PreviewIcon.ThumbUp, SwingConstants.CENTER);
                    JLabel dislikes = new JLabel(String.valueOf(videoInfo.getNumDislikes()), PreviewIcon.ThumbDown, SwingConstants.CENTER);
                    likes.setForeground(new Color(0, 192, 0));
                    dislikes.setForeground(new Color(255, 64, 64));
                    ratingPane.add(likes);
                    ratingPane.add(dislikes);

                    statisticPane.add(ratingPane, BorderLayout.EAST);
                }

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

                UIUtils.updateBackground(YoutubePagePreview.this, getBackground());
                balloonTip.refreshLocation();
            }
        }.execute();
    }
}
