package org.xblackcat.rojac.gui.dialog.subscribtion;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ItemStatisticData;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.IResult;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.List;

/**
 * @author xBlackCat
 */
class ForumLoader extends RojacWorker<Void, Forum> {
    private static final Log log = LogFactory.getLog(ForumLoader.class);

    private final SubscribeForumModel model;

    public ForumLoader(SubscribeForumModel model) {
        this.model = model;
        model.clear();
    }

    @Override
    protected Void perform() throws Exception {
        try {
            try (IResult<ItemStatisticData<Forum>> allForums = Storage.get(IForumAH.class).getAllForums(Property.RSDN_USER_ID.get(-1))) {
                for (ItemStatisticData<Forum> f : allForums) {
                    publish(f.getItem());
                }
            }
        } catch (StorageException e) {
            log.error("Can not load forum list", e);
            throw e;
        }

        return null;
    }

    @Override
    protected void process(List<Forum> forums) {
        model.addForums(forums);
    }

    @Override
    protected void done() {
        model.fireTableDataChanged();
    }
}
