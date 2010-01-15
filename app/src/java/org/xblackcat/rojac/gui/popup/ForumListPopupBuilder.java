package org.xblackcat.rojac.gui.popup;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.view.ForumData;
import org.xblackcat.rojac.gui.view.ForumTableModel;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.executor.IExecutor;
import org.xblackcat.rojac.service.executor.TaskType;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.util.RojacWorker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author xBlackCat
 */

public class ForumListPopupBuilder implements IPopupBuilder {
    private static final Log log = LogFactory.getLog(ForumListPopupBuilder.class);

    protected final IStorage storage = ServiceFactory.getInstance().getStorage();
    protected final IExecutor executor = ServiceFactory.getInstance().getExecutor();

    /**
     * Builds popup menu for RSDN message link. Available actions in the menu are: <ul> <li>"Goto" - open the message in
     * the view window</li> <li>"Open in new tab" - open the message in new tab of the main frame</li> <li>"Open thread
     * in new tab" - open whole thread in browser</li> <li>"Open in browser" - open only the message in browser </li>
     * </ul>
     *
     * @param parameters
     *
     * @return
     */
    @Override
    public JPopupMenu buildMenu(Object... parameters) {
        if (ArrayUtils.isEmpty(parameters) || parameters.length != 3) {
            throw new IllegalArgumentException("Invalid parameters amount.");
        }

        final ForumData forumData = (ForumData) parameters[0];
        final ForumTableModel model = (ForumTableModel) parameters[1];
        final IRootPane mainFrame = (IRootPane) parameters[2];

        JPopupMenu menu = new JPopupMenu(forumData.getForum().getForumName());

        final boolean subscribed = forumData.isSubscribed();
        final int forumId = forumData.getForumId();

        menu.add(new AbstractAction(Messages.POPUP_VIEW_FORUMS_OPEN.get()) {
            public void actionPerformed(ActionEvent e) {
                mainFrame.openForumTab(forumData.getForum());
            }
        });
        menu.addSeparator();

        menu.add(new SetForumReadMenuItem(Messages.POPUP_VIEW_FORUMS_SET_READ_ALL, forumId, model, true));
        menu.add(new SetForumReadMenuItem(Messages.POPUP_VIEW_FORUMS_SET_UNREAD_ALL, forumId, model, false));

        menu.addSeparator();

        {
            JCheckBoxMenuItem mi = new JCheckBoxMenuItem(Messages.POPUP_VIEW_FORUMS_SUBSCRIBE.get(), subscribed);
            mi.addActionListener(new SubscribeChangeListener(forumId, model, subscribed));
            menu.add(mi);
        }


        return menu;
    }

    private class SubscribeChangeListener implements ActionListener {
        private final int forumId;
        private final ForumTableModel forumsModel;
        private final boolean subscribed;

        public SubscribeChangeListener(int forumId, ForumTableModel forumsModel, boolean subscribed) {
            this.forumId = forumId;
            this.forumsModel = forumsModel;
            this.subscribed = subscribed;
        }

        public void actionPerformed(ActionEvent e) {
            executor.execute(new RojacWorker<Void, Void>() {
                @Override
                protected Void perform() throws Exception {
                    IForumAH fah = storage.getForumAH();
                    fah.setSubscribeForum(forumId, !subscribed);
                    return null;
                }

                @Override
                protected void done() {
                    forumsModel.setSubscribed(forumId, !subscribed);
                    forumsModel.updateForums(forumId);
                }
            }, TaskType.MessageLoading);
        }
    }

    private class SetForumReadMenuItem extends JMenuItem {
        public SetForumReadMenuItem(Messages text, final int forumId, final ForumTableModel forumsModel, final boolean readFlag) {
            super(text.get());
            addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    executor.execute(new RojacWorker<Void, Void>() {
                        @Override
                        protected Void perform() throws Exception {
                            IForumAH fah = storage.getForumAH();
                            fah.setForumRead(forumId, readFlag);
                            return null;
                        }

                        @Override
                        protected void done() {
                            forumsModel.updateForums(forumId);
                        }
                    }, TaskType.MessageLoading);
                }
            });
        }
    }
}
