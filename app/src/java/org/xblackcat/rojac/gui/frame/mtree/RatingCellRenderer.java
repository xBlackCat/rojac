package org.xblackcat.rojac.gui.frame.mtree;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Rating;
import org.xblackcat.rojac.service.storage.IRatingAH;
import org.xblackcat.rojac.service.storage.StorageException;

/**
 * Date: 15 груд 2007
 *
 * @author xBlackCat
 */
final class RatingCellRenderer extends MessageCellRenderer {
    private static final Log log = LogFactory.getLog(RatingCellRenderer.class);
    private final IRatingAH ratingAH;

    public RatingCellRenderer(IRatingAH ratingAH) {
        this.ratingAH = ratingAH;
    }

    protected boolean setupComponent(MessageItem item, boolean selected) {
        if (true) {
            return false;
        }

        try {
            Rating[] ratings = ratingAH.getRatingsByMessageId(item.getMessageId());

            setText(ratingsToString(ratings));
        } catch (StorageException e) {
            log.error("Can not get ratings for message [id = " + item.getMessageId() + "]", e);
        }

        return false;
    }

    private String ratingsToString(Rating[] ratings) {
        StringBuilder res = new StringBuilder();
        for (Rating r : ratings) {
            res.append(r.getUserRating()).append("x").append(r.getRate()).append(' ');
        }
        return res.toString();
    }
}