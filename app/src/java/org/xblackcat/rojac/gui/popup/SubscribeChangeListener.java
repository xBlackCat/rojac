package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.IDataDispatcher;
import org.xblackcat.rojac.service.datahandler.SubscriptionChanged;
import org.xblackcat.rojac.service.storage.IForumAH;
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
            IForumAH fah = ServiceFactory.getInstance().getStorage().getForumAH();
            fah.setSubscribeForum(forumId, !subscribed);
            return null;
        }

        @Override
        protected void done() {
            final Collection<SubscriptionChanged.Subscription> subscriptionList =
                    Collections.singleton(
                            new SubscriptionChanged.Subscription(forumId, !subscribed)
                    );
            final IDataDispatcher dispatcher = ServiceFactory.getInstance().getDataDispatcher();
            dispatcher.processPacket(new SubscriptionChanged(subscriptionList));
        }
    }
}
