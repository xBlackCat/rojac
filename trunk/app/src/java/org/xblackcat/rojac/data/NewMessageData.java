package org.xblackcat.rojac.data;

import org.xblackcat.rojac.service.options.Property;

/**
 * @author xBlackCat
 */

public class NewMessageData extends MessageData {
    public NewMessageData(NewMessage md) {
        super(
                // Negative id means the message is local-only
                -md.getLocalMessageId(),
                -1,
                md.getParentId(),
                md.getForumId(),
                Property.RSDN_USER_ID.get(-1),
                md.getSubject(),
                Property.RSDN_USER_NAME.get(),
                -1,
                -1,
                !md.isDraft(),
                null,
                false,
                0);
    }
}
