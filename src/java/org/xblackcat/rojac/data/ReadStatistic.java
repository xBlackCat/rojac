package org.xblackcat.rojac.data;

import org.xblackcat.rojac.i18n.Message;

/**
 * @author xBlackCat
 */
public class ReadStatistic {
    protected final int totalMessages;
    protected final int unreadMessages;
    protected final int unreadReplies;

    public ReadStatistic() {
        this(0, 0, 0);
    }

    public ReadStatistic(int totalMessages, int unreadMessages, int unreadReplies) {
        this.unreadReplies = unreadReplies;
        this.unreadMessages = unreadMessages < 0 ? 0 : unreadMessages > totalMessages ? totalMessages : unreadMessages;
        this.totalMessages = totalMessages;
    }

    public int getTotalMessages() {
        return totalMessages;
    }

    public int getUnreadMessages() {
        return unreadMessages;
    }

    public int getUnreadReplies() {
        return unreadReplies;
    }

    public String asString() {
        if (unreadMessages == 0) {
            return String.valueOf(totalMessages);
        } else if (unreadMessages == totalMessages) {
            if (unreadReplies == 0) {
                return String.valueOf(totalMessages);
            } else {
                return Message.View_Navigation_Item_ForumInfo_Full.get(
                        unreadReplies,
                        unreadMessages,
                        totalMessages
                );
            }
        } else {
            if (unreadReplies == 0) {
                return Message.View_Navigation_Item_ForumInfo.get(
                        unreadMessages,
                        totalMessages
                );
            } else {
                return Message.View_Navigation_Item_ForumInfo_Full.get(
                        unreadReplies,
                        unreadMessages,
                        totalMessages
                );
            }
        }
    }

    public String toString() {
        return "[" + totalMessages + "/" + unreadMessages + "/" + unreadReplies + "]";
    }
}
