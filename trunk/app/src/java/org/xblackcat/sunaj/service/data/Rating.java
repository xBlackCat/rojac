package org.xblackcat.sunaj.service.data;

import ru.rsdn.Janus.JanusRatingInfo;

import java.util.Date;

/**
 * Date: 14.04.2007
 *
 * @author ASUS
 */

public final class Rating {
    private final int messageId;
    private final int topicId;
    private final int userId;
    private final int userRating;
    private final Mark rate;
    private final long rateDate;

    public Rating(int messageId, int topicId, int userId, int userRating, int rate, long rateDate) {
        this.messageId = messageId;
        this.topicId = topicId;
        this.userId = userId;
        this.userRating = userRating;
        this.rate = Mark.getMark(rate);
        this.rateDate = rateDate;
    }

    public Rating(JanusRatingInfo i) {
        this(i.getMessageId(), i.getTopicId(), i.getUserId(), i.getUserRating(), i.getRate(), i.getRateDate().getTimeInMillis());
    }

    public int getMessageId() {
        return messageId;
    }

    public int getTopicId() {
        return topicId;
    }

    public int getUserId() {
        return userId;
    }

    public int getUserRating() {
        return userRating;
    }

    public Mark getRate() {
        return rate;
    }

    public long getRateDate() {
        return rateDate;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rating that = (Rating) o;

        if (messageId != that.messageId) return false;
        if (rate != that.rate) return false;
        if (rateDate != that.rateDate) return false;
        if (topicId != that.topicId) return false;
        if (userId != that.userId) return false;
        if (userRating != that.userRating) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = messageId;
        result = 31 * result + topicId;
        result = 31 * result + userId;
        result = 31 * result + userRating;
        result = 31 * result + rate.getValue();
        result = 31 * result + (int) (rateDate ^ (rateDate >>> 32));
        return result;
    }

    public String toString() {
        StringBuilder str = new StringBuilder("Raiting[");
        str.append("messageId=").append(messageId).append(", ");
        str.append("topicId=").append(topicId).append(", ");
        str.append("userId=").append(userId).append(", ");
        str.append("userRating=").append(userRating).append(", ");
        str.append("rate=").append(rate).append(", ");
        str.append("rateDate=").append(new Date(rateDate)).append(']');
        return str.toString();
    }
}
