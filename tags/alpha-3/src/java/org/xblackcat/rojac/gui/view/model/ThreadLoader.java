package org.xblackcat.rojac.gui.view.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.Collection;
import java.util.List;

/**
* @author xBlackCat
*/
class ThreadLoader extends RojacWorker<Void, MessageData> {
    private static final Log log = LogFactory.getLog(ThreadLoader.class);

    protected final IStorage storage = ServiceFactory.getInstance().getStorage();

    private final Thread item;
    private AThreadModel<Post> model;

    public ThreadLoader(AThreadModel<Post> model, Thread item, Runnable postProcessor) {
        super(postProcessor);
        this.item = item;
        this.model = model;
    }

    @Override
    protected Void perform() throws Exception {
        int itemId = item.getMessageId();
        int forumId = item.getForumId();

        try {
            Collection<MessageData> datas = storage.getMessageAH().getMessagesDataByTopicId(itemId, forumId);
            for (MessageData md : datas) {
                publish(md);
            }
        } catch (StorageException e) {
            log.error("Can not load message children for id = " + itemId + " (forum #" + forumId + ")", e);
            throw e;
        }

        return null;
    }

    @Override
    protected void process(List<MessageData> chunks) {
        item.fillThread(chunks);
    }

    @Override
    protected void done() {
        item.setLoadingState(LoadingState.Loaded);
        model.nodeStructureChanged(item);

        super.done();
    }
}
