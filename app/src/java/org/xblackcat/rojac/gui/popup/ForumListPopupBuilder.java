package org.xblackcat.rojac.gui.popup;

import org.apache.commons.lang.ArrayUtils;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.view.forumlist.ForumData;
import org.xblackcat.rojac.gui.view.forumlist.ForumTableModel;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.PacketType;
import org.xblackcat.rojac.service.datahandler.ProcessPacket;
import org.xblackcat.rojac.service.executor.IExecutor;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.util.RojacWorker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** @author xBlackCat */

class ForumListPopupBuilder implements IPopupBuilder {
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
     * @return built pop-up menu
     */
    @Override
    public JPopupMenu buildMenu(Object... parameters) {
        if (ArrayUtils.isEmpty(parameters) || parameters.length != 3) {
            throw new IllegalArgumentException("Invalid parameters amount.");
        }

        ForumData forumData = (ForumData) parameters[0];
        ForumTableModel model = (ForumTableModel) parameters[1];
        IRootPane mainFrame = (IRootPane) parameters[2];

        return buildInternal(forumData, model, mainFrame);
    }

    /**
     * Real menu builder.
     *
     * @param forumData selected forum data.
     * @param model forums view model to manipulate with its data.  
     * @param mainFrame main frame controller.
     *
     * @return built pop-up menu
     */
    private JPopupMenu buildInternal(final ForumData forumData, ForumTableModel model, final IRootPane mainFrame) {
        JPopupMenu menu = new JPopupMenu(forumData.getForum().getForumName());

        final boolean subscribed = forumData.isSubscribed();
        final int forumId = forumData.getForumId();

        menu.add(new AbstractAction(Messages.POPUP_VIEW_FORUMS_OPEN.get()) {
            public void actionPerformed(ActionEvent e) {
                mainFrame.openForumTab(forumData.getForum());
            }
        });
        menu.addSeparator();

        menu.add(new SetForumReadMenuItem(Messages.POPUP_VIEW_FORUMS_SET_READ_ALL, forumId, true));
        menu.add(new SetForumReadMenuItem(Messages.POPUP_VIEW_FORUMS_SET_UNREAD_ALL, forumId, false));

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
                }
            });
        }
    }

    private class SetForumReadMenuItem extends JMenuItem {
        public SetForumReadMenuItem(Messages text, final int forumId, final boolean readFlag) {
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
                            PacketType type = readFlag ? PacketType.SetForumRead : PacketType.SetForumUnread;
                            ProcessPacket p = new ProcessPacket(type, forumId);
                            ServiceFactory.getInstance().getDataDispatcher().processPacket(p);
                        }
                    });
                }
            });
        }
    }
}