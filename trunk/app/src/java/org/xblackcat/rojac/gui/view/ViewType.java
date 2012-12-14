package org.xblackcat.rojac.gui.view;

import org.xblackcat.rojac.RojacDebugException;
import org.xblackcat.rojac.data.MessageData;

/**
 * @author xBlackCat
 */

public enum ViewType {
    Forum {
        @Override
        public ViewId makeId(MessageData md) {
            return makeId(md.getForumId());
        }
    },
    SingleThread {
        @Override
        public ViewId makeId(MessageData md) {
            return makeId(md.getThreadRootId());
        }
    },
    SingleMessage {
        @Override
        public ViewId makeId(MessageData md) {
            return makeId(md.getMessageId());
        }
    },
    Favorite {
        @Override
        public ViewId makeId(MessageData md) {
            throw new RojacDebugException("Not implemented");
        }
    },
    PostList {
        @Override
        public ViewId makeId(MessageData md) {
            return makeId(md.getUserId());
        }
    },
    ReplyList {
        @Override
        public ViewId makeId(MessageData md) {
            return makeId(md.getUserId());
        }
    },
    OutBox {
        @Override
        public ViewId makeId(MessageData md) {
            return makeId(0); // Only one outbox can be shown
        }

        @Override
        public ViewId makeId(int itemId) {
            return new ViewId(0, this);
        }
    },
    StartPage {
        @Override
        public ViewId makeId(MessageData md) {
            return makeId(0); // Only one outbox can be shown
        }

        @Override
        public ViewId makeId(int itemId) {
            return new ViewId(0, this);
        }
    },
    IgnoredThreadList {
        @Override
        public ViewId makeId(MessageData md) {
            return makeId(0); // Only one ignored threads list can be shown
        }

        @Override
        public ViewId makeId(int itemId) {
            return new ViewId(0, this);
        }
    }
    ;

    private ViewType() {
    }

    public ViewId makeId(int itemId) {
        return new ViewId(itemId, this);
    }

    public abstract ViewId makeId(MessageData md);
}
