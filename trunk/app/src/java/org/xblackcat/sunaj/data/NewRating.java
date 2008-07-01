package org.xblackcat.sunaj.data;

import ru.rsdn.Janus.PostRatingInfo;

/**
 * Date: 14.04.2007
 *
 * @author ASUS
 */

public final class NewRating implements IRSDNable<PostRatingInfo> {
    private final int id;
    private final int messageId;
    private final Mark rate;

    public NewRating(int id, int messageId, int rate) {
        this.id = id;
        this.messageId = messageId;
        this.rate = Mark.getMark(rate);
    }

    public int getId() {
        return id;
    }

    public int getMessageId() {
        return messageId;
    }

    public Mark getRate() {
        return rate;
    }

    public PostRatingInfo getRSDNObject() {
        return new PostRatingInfo(id, messageId, rate.getValue());
    }

    public String toString() {
        StringBuilder str = new StringBuilder("NewRating[");
        str.append("id=").append(id).append(", ");
        str.append("messageId=").append(messageId).append(", ");
        str.append("rate=").append(rate).append(']');
        return str.toString();
    }
}
