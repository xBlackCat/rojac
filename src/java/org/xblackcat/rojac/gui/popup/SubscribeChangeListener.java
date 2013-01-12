package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.service.datahandler.SubscriptionChangedPacket;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.RojacWorker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Collections;

/**
 * @author xBlackCat
 */
class SubscribeChangeListener implements ActionListener {
    private final int forumId;
    private final boolean subscribed;

    public SubscribeChangeListener(int forumId, boolean subscribed) {
        this.forumId = forumId;
        this.subscribed = subscribed;
    }

    public void actionPerformed(ActionEvent e) {
        new ForumSubscriber().execute();
    }

    private class ForumSubscriber extends RojacWorker<Void, Void> {
        @Override
        protected Void perform() throws Exception {
            IForumAH fah = Storage.get(IForumAH.class);
            fah.setSubscribeForum(forumId, !subscribed);
            return null;
        }

        @Override
        protected void done() {
            final Collection<SubscriptionChangedPacket.Subscription> subscriptionList =
                    Collections.singleton(
                            new SubscriptionChangedPacket.Subscription(forumId, !subscribed)
                    );

            new SubscriptionChangedPacket(subscriptionList).dispatch();
        }
    }
}
