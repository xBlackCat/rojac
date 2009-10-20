package org.xblackcat.rojac.gui.frame.message;

import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.data.NewRating;
import org.xblackcat.rojac.data.Rating;

/**
 * @author xBlackCat
 */

final class MarkItem {
    private final Integer id;
    private final Mark mark;
    private final Integer userId;
    private final Long date;

    MarkItem(NewRating r) {
        this(r.getId(), r.getRate(), null, null);
    }

    MarkItem(Rating r) {
        this(null, r.getRate(), r.getUserId(), r.getRateDate());
    }

    private MarkItem(Integer id, Mark mark, Integer userId, Long date) {
        this.id = id;
        this.mark = mark;
        this.userId = userId;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public Mark getMark() {
        return mark;
    }

    public Integer getUserId() {
        return userId;
    }

    public Long getDate() {
        return date;
    }

    public boolean isNewRate() {
        return id != null;
    }
}
