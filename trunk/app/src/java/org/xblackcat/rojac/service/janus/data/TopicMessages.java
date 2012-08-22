package org.xblackcat.rojac.service.janus.data;

import ru.rsdn.janus.JanusMessageInfo;
import ru.rsdn.janus.JanusModerateInfo;
import ru.rsdn.janus.JanusRatingInfo;

import java.util.Collections;
import java.util.List;

/**
 * @author ASUS
 */

public class TopicMessages {
    public static final List<JanusMessageInfo> NO_MESSAGES = Collections.emptyList();
    public static final List<JanusModerateInfo> NO_MODERATES = Collections.emptyList();
    public static final List<JanusRatingInfo> NO_RATINGS = Collections.emptyList();

    protected final List<JanusMessageInfo> messages;
    protected final List<JanusModerateInfo> moderates;
    protected final List<JanusRatingInfo> ratings;

    public TopicMessages(
            List<JanusMessageInfo> messages,
            List<JanusModerateInfo> moderates,
            List<JanusRatingInfo> ratings
    ) {
        this.messages = messages == null ? NO_MESSAGES : messages;
        this.moderates = moderates == null ? NO_MODERATES : moderates;
        this.ratings = ratings == null ? NO_RATINGS : ratings;
    }

    public List<JanusMessageInfo> getMessages() {
        return messages;
    }

    public List<JanusModerateInfo> getModerates() {
        return moderates;
    }

    public List<JanusRatingInfo> getRatings() {
        return ratings;
    }

    public int getTotalRecords() {
        return messages.size() + moderates.size() + ratings.size();
    }
}
