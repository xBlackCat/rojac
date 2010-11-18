package org.xblackcat.rojac.gui.view.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.List;

/**
* @author xBlackCat
*/
class ThreadLoader extends RojacWorker<Void, MessageData> {
    private static final Log log = LogFactory.getLog(ThreadLoader.class);

    protected final IStorage storage = ServiceFactory.getInstance().getStorage();

    private final Thread item;
    private final AThreadModel<Post> threadModel;
    private final IItemProcessor<Post> postProcessor;

    public ThreadLoader(Thread item, AThreadModel<Post> threadModel, IItemProcessor<Post> postProcessor) {
        this.item = item;
        this.threadModel = threadModel;
        this.postProcessor = postProcessor;
    }

    @Override
    protected Void perform() throws Exception {
        int itemId = item.getMessageId();
        int forumId = item.getForumId();

        MessageData[] messages;
        try {
            messages = storage.getMessageAH().getMessagesDataByTopicId(itemId, forumId);
        } catch (StorageException e) {
            log.error("Can not load message children for id = " + itemId + " (forum #" + forumId + ")", e);
            throw e;
        }

        publish(messages);

        return null;
    }

    @Override
    protected void process(List<MessageData> chunks) {
        item.fillThread(chunks);
        item.setLoadingState(LoadingState.Loaded);

        threadModel.nodeStructureChanged(item);

        if (postProcessor != null) {
            postProcessor.processItem(item);
        }
    }
}
