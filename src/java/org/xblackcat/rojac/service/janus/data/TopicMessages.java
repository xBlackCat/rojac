package org.xblackcat.rojac.service.janus.data;

import ru.rsdn.Janus.JanusMessageInfo;
import ru.rsdn.Janus.JanusModerateInfo;
import ru.rsdn.Janus.JanusRatingInfo;

/**
 * @author ASUS
 */

public class TopicMessages {
    protected final JanusMessageInfo[] messages;
    protected final JanusModerateInfo[] moderates;
    protected final JanusRatingInfo[] ratings;

    public TopicMessages(JanusMessageInfo[] messages, JanusModerateInfo[] moderates, JanusRatingInfo[] ratings) {
        this.messages = messages;
        this.moderates = moderates;
        this.ratings = ratings;
    }

    public JanusMessageInfo[] getMessages() {
        return messages;
    }

    public JanusModerateInfo[] getModerates() {
        return moderates;
    }

    public JanusRatingInfo[] getRatings() {
        return ratings;
    }

    public int getTotalRecords() {
        return messages.length + moderates.length + ratings.length;
    }
}
