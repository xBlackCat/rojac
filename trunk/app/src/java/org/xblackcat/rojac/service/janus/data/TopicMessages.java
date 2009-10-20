package org.xblackcat.rojac.service.janus.data;

import org.xblackcat.rojac.data.Message;
import org.xblackcat.rojac.data.Moderate;
import org.xblackcat.rojac.data.Rating;
import ru.rsdn.Janus.JanusMessageInfo;
import ru.rsdn.Janus.JanusModerateInfo;
import ru.rsdn.Janus.JanusRatingInfo;

/**
 * @author ASUS
 */

public final class TopicMessages {
    private final Message[] messages;
    private final Moderate[] moderates;
    private final Rating[] ratings;

    public TopicMessages(JanusMessageInfo[] mes, JanusModerateInfo[] mod, JanusRatingInfo[] r) {
        messages = new Message[mes.length];
        for (int i = 0; i < mes.length; i++) {
            messages[i] = new Message(mes[i]);
        }

        moderates = new Moderate[mod.length];
        for (int i = 0; i < mod.length; i++) {
            moderates[i] = new Moderate(mod[i]);
        }

        ratings = new Rating[r.length];
        for (int i = 0; i < r.length; i++) {
            ratings[i] = new Rating(r[i]);
        }
    }

    public Message[] getMessages() {
        return messages;
    }

    public Moderate[] getModerates() {
        return moderates;
    }

    public Rating[] getRatings() {
        return ratings;
    }
}
