package org.xblackcat.rojac.gui.view.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.ReadStatistic;
import org.xblackcat.rojac.service.executor.TaskType;
import org.xblackcat.rojac.service.executor.TaskTypeEnum;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IStatisticAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.RojacWorker;
import org.xblackcat.sjpu.storage.StorageException;

import java.util.List;

/**
 * Worker to load collapsed thread - update only unread posts amount.
 *
 * @author xBlackCat
 */
@TaskType(TaskTypeEnum.Initialization)
class ThreadStatisticLoader extends RojacWorker<Void, ReadStatistic> {
    private static final Log log = LogFactory.getLog(ThreadStatisticLoader.class);

    private final Thread topic;
    private final SortedThreadsModel model;

    public ThreadStatisticLoader(Thread topic, SortedThreadsModel model, Runnable postProcessor) {
        super(postProcessor);
        this.topic = topic;
        this.model = model;
    }

    @Override
    protected Void perform() throws Exception {
        IStatisticAH mAH = Storage.get(IStatisticAH.class);
        try {
            ReadStatistic unreadPosts = mAH.getReplaysInThread(topic.getMessageId(), Property.RSDN_USER_ID.get());

            publish(unreadPosts);
        } catch (StorageException e) {
            log.error("Can not load statistic for topic #" + topic, e);
        }

        return null;
    }

    @Override
    protected void process(List<ReadStatistic> chunks) {
        for (ReadStatistic unreadPosts : chunks) {
            topic.setUnreadPosts(unreadPosts);
            model.pathToNodeChanged(topic);
        }
    }
}
