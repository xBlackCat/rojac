package org.xblackcat.rojac.gui.view.message;

import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YtRating;
import org.xblackcat.rojac.util.UIUtils;

import javax.swing.*;
import java.util.List;

/**
 * 26.03.12 9:40
 *
 * @author xBlackCat
 */
class YoutubeVideoInfo {
    private final String videoId;
    private final String videoTitle;
    private final Long duration;
    private final Integer numDislikes;
    private final Integer numLikes;
    private final long viewCount;
    private final Icon thumbnail;
    private final Icon hqThumbnail;

    YoutubeVideoInfo(VideoEntry ve) {
        YouTubeMediaGroup mediaGroup = ve.getMediaGroup();

        videoId = mediaGroup.getVideoId();
        videoTitle = ve.getTitle().getPlainText();
        duration = mediaGroup.getDuration();
        YtRating rating = ve.getYtRating();
        if (rating != null) {
            numDislikes = rating.getNumDislikes();
            numLikes = rating.getNumLikes();
        } else {
            numDislikes = null;
            numLikes = null;
        }
        viewCount = ve.getStatistics().getViewCount();

        // Load thumbnail
        List<MediaThumbnail> thumbnails = mediaGroup.getThumbnails();

        String thumbnailUrl = null;
        String hqThumbnailUrl = null;
        for (MediaThumbnail mt : thumbnails) {
            if (mt.getUrl().contains("/default.")) {
                thumbnailUrl = mt.getUrl();
            } else if (mt.getUrl().contains("/hqdefault.")) {
                hqThumbnailUrl = mt.getUrl();
            }
        }

        // Load images
        thumbnail = UIUtils.loadRemoteIcon(thumbnailUrl);
        hqThumbnail = UIUtils.loadRemoteIcon(hqThumbnailUrl);
    }

    public String getVideoId() {
        return videoId;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public Long getDuration() {
        return duration;
    }

    public boolean hasRating() {
        return numLikes != null;
    }

    public int getNumDislikes() {
        return numDislikes;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public long getViewCount() {
        return viewCount;
    }

    public Icon getThumbnail() {
        return thumbnail;
    }

    public Icon getHqThumbnail() {
        return hqThumbnail;
    }
}
