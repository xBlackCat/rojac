package org.xblackcat.sunaj.service.janus.data;

/**
 * Date: 14.04.2007
 *
 * @author ASUS
 */

public final class NewRating {
    private final int messageId;
    private final int rate;

    public NewRating(int messageId, int rate) {
        this.messageId = messageId;
        this.rate = rate;
    }

    public int getMessageId() {
        return messageId;
    }

    public int getRate() {
        return rate;
    }

    public String toString() {
        StringBuilder str = new StringBuilder("NewRating[");
        str.append("messageId=").append(messageId).append(", ");
        str.append("rate=").append(rate).append(']');
        return str.toString();
    }
}
