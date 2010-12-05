package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.view.forumlist.ForumTableModel;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.util.RojacWorker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
* @author xBlackCat
*/
class SubscribeChangeListener implements ActionListener {
    protected final IStorage storage = ServiceFactory.getInstance().getStorage();

    private final int forumId;
    private final ForumTableModel forumsModel;
    private final boolean subscribed;

    public SubscribeChangeListener(int forumId, ForumTableModel forumsModel, boolean subscribed) {
        this.forumId = forumId;
        this.forumsModel = forumsModel;
        this.subscribed = subscribed;
    }

    public void actionPerformed(ActionEvent e) {
        new RojacWorker<Void, Void>() {
            @Override
            protected Void perform() throws Exception {
                IForumAH fah = storage.getForumAH();
                fah.setSubscribeForum(forumId, !subscribed);
                return null;
            }

            @Override
            protected void done() {
                forumsModel.setSubscribed(forumId, !subscribed);
            }
        }.execute();
    }
}
