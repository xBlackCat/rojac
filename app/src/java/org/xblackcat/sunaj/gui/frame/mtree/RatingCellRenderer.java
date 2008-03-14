package org.xblackcat.sunaj.gui.frame.mtree;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.sunaj.service.data.Rating;
import org.xblackcat.sunaj.service.storage.IRatingAH;
import org.xblackcat.sunaj.service.storage.StorageException;

/**
 * Date: 15 груд 2007
 *
 * @author xBlackCat
 */
final class RatingCellRenderer extends MessageCellRenderer {
    private static final Log log = LogFactory.getLog(RatingCellRenderer.class);
    private final IRatingAH ratingDAO;

    public RatingCellRenderer(IRatingAH ratingDAO) {
        this.ratingDAO = ratingDAO;
    }

    protected boolean setupComponent(MessageItem item, boolean selected) {
        if (true) {
            return false;
        }

        try {
            Rating[] ratings = ratingDAO.getRatingsByMessageId(item.getMessageId());

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