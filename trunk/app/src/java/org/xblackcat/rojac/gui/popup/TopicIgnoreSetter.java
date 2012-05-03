package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.service.datahandler.IgnoreUpdatedPacket;
import org.xblackcat.rojac.service.storage.IMiscAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.List;

/**
* 03.05.12 16:46
*
* @author xBlackCat
*/
public class TopicIgnoreSetter extends RojacWorker<Void, Void> {
    private final boolean ignored;
    private final int topicId;
    private final int forumId;

    public TopicIgnoreSetter(boolean ignored, int topicId, int forumId) {
        this.ignored = ignored;
        this.topicId = topicId;
        this.forumId = forumId;
    }

    @Override
    protected Void perform() throws Exception {
        IMiscAH miscAH = Storage.get(IMiscAH.class);

        if (ignored) {
            miscAH.removeFromIgnoredTopicList(topicId);
        } else {
            miscAH.addToIgnoredTopicList(topicId);
        }

        publish();

        return null;
    }

    @Override
    protected void process(List<Void> chunks) {
        new IgnoreUpdatedPacket(forumId, topicId, !ignored).dispatch();
    }
}
