package org.xblackcat.rojac.data;

/**
 * 27.03.12 16:21
 *
 * @author xBlackCat
 */
public class ThreadData {
    private final MessageData messageData;
    private final DiscussionStatistic readStatistic;

    public ThreadData(MessageData messageData, DiscussionStatistic readStatistic) {
        this.messageData = messageData;
        this.readStatistic = readStatistic;
    }

    public MessageData getMessageData() {
        return messageData;
    }

    public DiscussionStatistic getReadStatistic() {
        return readStatistic;
    }
}
