package org.xblackcat.rojac.gui.view.message;

import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.data.NewRating;
import org.xblackcat.rojac.data.Rating;
import org.xblackcat.rojac.data.User;

/**
 * @author xBlackCat
 */

final class MarkItem {
    private final Integer id;
    private final Mark mark;
    private final Integer userId;
    private final User user;
    private final Long date;

    MarkItem(NewRating r) {
        this(r.getId(), r.getRate(), null, null, null);
    }

    MarkItem(Rating r) {
        this(r, null);
    }

    MarkItem(Rating r, User user) {
        this(null, r.getRate(), r.getUserId(), r.getRateDate(), user);
    }

    private MarkItem(Integer id, Mark mark, Integer userId, Long date, User user) {
        this.id = id;
        this.mark = mark;
        this.userId = userId;
        this.date = date;
        this.user = user;
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

    public User getUser() {
        return user;
    }
}
