package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.data.ForumStatistic;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacUtils;

/**
 * @author xBlackCat
 */
abstract class AForumTask<V> extends ALoadTask<V> {
    protected final IForumAH fah = Storage.get(IForumAH.class);

    protected final ForumStatistic getForumStatistic(int forumId) throws StorageException {
        assert RojacUtils.checkThread(false);

        Number totalMessages = fah.getMessagesInForum(forumId);
        Number unreadMessages = fah.getUnreadMessagesInForum(forumId);
        Number lastPostDate = fah.getLastMessageDateInForum(forumId);

        Property<Integer> rsdnUserId = Property.RSDN_USER_ID;
        int unreadReplies;

        if (rsdnUserId.isSet()) {
            unreadReplies = fah.getUnreadRepliesInForum(forumId, rsdnUserId.get()).intValue();
        } else {
            unreadReplies = 0;
        }

        return new ForumStatistic(
                forumId,
                totalMessages.intValue(),
                unreadMessages.intValue(),
                lastPostDate == null ? null : lastPostDate.longValue(),
                unreadReplies
        );
    }
}
