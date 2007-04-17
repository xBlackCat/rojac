package org.xblackcat.sunaj.service.janus.data;

import org.xblackcat.sunaj.service.data.Message;
import org.xblackcat.sunaj.service.data.ModerateInfo;
import org.xblackcat.sunaj.service.data.Rating;
import ru.rsdn.Janus.JanusMessageInfo;
import ru.rsdn.Janus.JanusModerateInfo;
import ru.rsdn.Janus.JanusRatingInfo;

/**
 * Date: 14.04.2007
 *
 * @author ASUS
 */

public final class TopicMessages {
    private final Message[] messages;
    private final ModerateInfo[] moderates;
    private final Rating[] ratings;

    public TopicMessages(JanusMessageInfo[] mes, JanusModerateInfo[] mod, JanusRatingInfo[] r) {
        messages = new Message[mes.length];
        for (int i = 0; i < mes.length; i++) {
            messages[i] = new Message(mes[i]);
        }

        moderates = new ModerateInfo[mod.length];
        for (int i = 0; i < mod.length; i++) {
            moderates[i] = new ModerateInfo(mod[i]);
        }

        ratings = new Rating[r.length];
        for (int i = 0; i < r.length; i++) {
            ratings[i] = new Rating(r[i]);
        }
    }

    public Message[] getMessages() {
        return messages;
    }

    public ModerateInfo[] getModerates() {
        return moderates;
    }

    public Rating[] getRatings() {
        return ratings;
    }
}
