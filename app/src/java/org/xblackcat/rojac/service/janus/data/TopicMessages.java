package org.xblackcat.rojac.service.janus.data;

import ru.rsdn.Janus.JanusMessageInfo;
import ru.rsdn.Janus.JanusModerateInfo;
import ru.rsdn.Janus.JanusRatingInfo;

/**
 * @author ASUS
 */

public class TopicMessages {
    public static final JanusMessageInfo[] NO_MESSAGES = new JanusMessageInfo[0];
    public static final JanusModerateInfo[] NO_MODERATES = new JanusModerateInfo[0];
    public static final JanusRatingInfo[] NO_RATINGS = new JanusRatingInfo[0];

    protected final JanusMessageInfo[] messages;
    protected final JanusModerateInfo[] moderates;
    protected final JanusRatingInfo[] ratings;

    public TopicMessages(JanusMessageInfo[] messages, JanusModerateInfo[] moderates, JanusRatingInfo[] ratings) {
        this.messages = messages == null ? NO_MESSAGES : messages;
        this.moderates = moderates == null ? NO_MODERATES : moderates;
        this.ratings = ratings == null ? NO_RATINGS : ratings;
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
