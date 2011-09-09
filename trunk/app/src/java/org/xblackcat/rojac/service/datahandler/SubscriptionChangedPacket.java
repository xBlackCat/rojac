package org.xblackcat.rojac.service.datahandler;

import java.util.Collection;

/**
 * @author xBlackCat
 */

public class SubscriptionChangedPacket extends APacket{
    private final Collection<Subscription> newSubscriptions;

    public SubscriptionChangedPacket(Collection<Subscription> newSubscriptions) {
        this.newSubscriptions = newSubscriptions;
    }

    public Collection<Subscription> getNewSubscriptions() {
        return newSubscriptions;
    }

    public boolean isNotEmpty() {
        return !newSubscriptions.isEmpty();
    }

    public static class Subscription {
        private final int forumId;
        private final boolean subscribed;

        public Subscription(int forumId, boolean subscribed) {
            this.forumId = forumId;
            this.subscribed = subscribed;
        }

        public int getForumId() {
            return forumId;
        }

        public boolean isSubscribed() {
            return subscribed;
        }
    }
}
