package org.xblackcat.rojac.gui.view.forumlist;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumStatistic;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.gui.view.AView;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.PacketType;
import org.xblackcat.rojac.service.datahandler.ProcessPacket;
import org.xblackcat.rojac.service.janus.commands.AffectedMessage;
import org.xblackcat.rojac.service.janus.commands.IResultHandler;
import org.xblackcat.rojac.service.janus.commands.Request;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacWorker;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Main class for forum view.
 *
 * @author xBlackCat
 */
public class ForumsListView extends AView {
    private static final Log log = LogFactory.getLog(ForumsListView.class);

    // Data and models
    private ForumTableModel forumsModel = new ForumTableModel();

    public ForumsListView(IRootPane rootPane) {
        super(rootPane);
        final JTable forums = new JTable(forumsModel);
        forums.setTableHeader(null);
        add(new JScrollPane(forums));

        forums.setDefaultRenderer(ForumData.class, new MultiLineForumRenderer());
//        forums.setDefaultRenderer(ForumData.class, new ForumCellRenderer());
        forums.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                checkMenu(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                checkMenu(e);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                checkMenu(e);
            }

            private void checkMenu(MouseEvent e) {
                final Point p = e.getPoint();

                int ind = forums.rowAtPoint(p);

                int modelInd = forums.convertRowIndexToModel(ind);

                ForumData forum = forumsModel.getValueAt(modelInd, 0);

                if (e.isPopupTrigger()) {
                    JPopupMenu menu = PopupMenuBuilder.getForumViewMenu(forum, forumsModel, mainFrame);

                    menu.show(forums, p.x, p.y);
                } else if (e.getClickCount() > 1 && e.getButton() == MouseEvent.BUTTON1) {
                    mainFrame.openForumTab(forum.getForum());
                }
            }
        });

        final ForumsRowFilter forumsRowFilter = new ForumsRowFilter();
        final TableRowSorter<ForumTableModel> forumsRowSorter = new TableRowSorter<ForumTableModel>(forumsModel);
        forumsRowSorter.setComparator(0, new ForumDataComparator());
        forumsRowSorter.setStringConverter(new ForumsTableStringConverter());
        forumsRowSorter.setSortable(0, true);
        forumsRowSorter.setSortsOnUpdates(false);
        forumsRowSorter.setSortKeys(Arrays.asList(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
        forumsRowSorter.setRowFilter(forumsRowFilter);
        forumsRowSorter.sort();
        forums.setRowSorter(forumsRowSorter);

        forumsModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    forumsRowSorter.sort();
                }
            }
        });

        JToggleButton filledOnlyButton = WindowsUtils.setupToggleButton("filled_only", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean newState = !forumsRowFilter.isNotEmpty();
                Property.VIEW_FORUM_LIST_FILLED_ONLY.set(newState);
                forumsRowFilter.setNotEmpty(newState);
                forumsRowSorter.sort();
            }
        }, Messages.VIEW_FORUMS_BUTTON_FILLED);
        JToggleButton subscribedOnlyButton = WindowsUtils.setupToggleButton("subscribed_only", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean newState = !forumsRowFilter.isSubscribed();
                forumsRowFilter.setSubscribed(newState);
                Property.VIEW_FORUM_LIST_SUBSCRIBED_ONLY.set(newState);
                forumsRowSorter.sort();
            }
        }, Messages.VIEW_FORUMS_BUTTON_SUBSCRIBED);
        JToggleButton unreadOnlyButton = WindowsUtils.setupToggleButton("unread_only", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean newState = !forumsRowFilter.isUnread();
                forumsRowFilter.setUnread(newState);
                Property.VIEW_FORUM_LIST_UNREAD_ONLY.set(newState);
                forumsRowSorter.sort();
            }
        }, Messages.VIEW_FORUMS_BUTTON_HASUNREAD);

        JButton updateListButton = WindowsUtils.setupImageButton("update", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Request.GET_FORUMS_LIST.process(
                        SwingUtilities.windowForComponent(ForumsListView.this),
                        new IResultHandler() {
                            @Override
                            public void process(AffectedMessage... messages) {
                                ServiceFactory.getInstance().getDataDispatcher().processPacket(new ProcessPacket(PacketType.ForumsLoaded));
                            }
                        }
                );
            }
        }, Messages.VIEW_FORUMS_BUTTON_UPDATE);

        JToolBar toolBar = WindowsUtils.createToolBar(
                updateListButton,
                null,
                filledOnlyButton,
                subscribedOnlyButton,
                unreadOnlyButton);

        filledOnlyButton.setSelected(Property.VIEW_FORUM_LIST_FILLED_ONLY.get());
        subscribedOnlyButton.setSelected(Property.VIEW_FORUM_LIST_SUBSCRIBED_ONLY.get());
        unreadOnlyButton.setSelected(Property.VIEW_FORUM_LIST_UNREAD_ONLY.get());

        forumsRowFilter.setNotEmpty(Property.VIEW_FORUM_LIST_FILLED_ONLY.get());
        forumsRowFilter.setSubscribed(Property.VIEW_FORUM_LIST_SUBSCRIBED_ONLY.get());
        forumsRowFilter.setUnread(Property.VIEW_FORUM_LIST_UNREAD_ONLY.get());
        
        add(toolBar, BorderLayout.NORTH);
    }

    public void applySettings() {
        super.applySettings();

        executor.execute(new ForumLoader());
    }

    public void processPacket(ProcessPacket changedData) {
        final int[] forumIds = changedData.getForumIds();
        switch (changedData.getType()) {
            case SetForumRead:
                forumsModel.setRead(true, forumIds);
                break;
            case SetForumUnread:
                forumsModel.setRead(false, forumIds);
                break;
            case AddMessages:
            case UpdateMessages:
            case SetThreadRead:
            case SetThreadUnread:
                loadForumStatistic(forumIds);
                break;
            case SetPostRead:
                for (int forumId : forumIds) {
                    adjustUnreadPosts(-1, forumId);
                }
                break;
            case SetPostUnread:
                for (int forumId : forumIds) {
                    adjustUnreadPosts(1, forumId);
                }
                break;
            case ForumsLoaded:
                executor.execute(new ForumLoader());
                break;
        }
    }

    private void adjustUnreadPosts(int amount, int forumId) {
        ForumData data = forumsModel.getForumData(forumId);
        ForumStatistic oldStatistic = data.getStat();

        ForumStatistic newStatistic = new ForumStatistic(
                forumId,
                oldStatistic.getTotalMessages(),
                oldStatistic.getUnreadMessages() + amount,
                oldStatistic.getLastMessageDate()
        );

        forumsModel.updateStatistic(newStatistic);
    }

    private void loadForumStatistic(int[] forumIds) {
        executor.execute(new ForumUpdater(forumIds));
    }

    private class ForumUpdater extends RojacWorker<Void, ForumStatistic> {
        private final int[] forumIds;

        public ForumUpdater(int... forumIds) {
            this.forumIds = forumIds;
        }

        @Override
        protected Void perform() throws Exception {
            IForumAH fah = storage.getForumAH();

            Map<Integer, Integer> totalMessages = fah.getMessagesInForum(forumIds);
            Map<Integer, Integer> unreadMessages = fah.getUnreadMessagesInForum(forumIds);
            Map<Integer, Long> lastPostDate = fah.getLastMessageDateInForum(forumIds);

            for (int forumId : forumIds) {
                publish(new ForumStatistic(
                        forumId,
                        totalMessages.get(forumId),
                        unreadMessages.get(forumId),
                        lastPostDate.get(forumId)
                ));
            }

            return null;
        }

        @Override
        protected void process(List<ForumStatistic> chunks) {
            for (ForumStatistic stat : chunks) {
                forumsModel.updateStatistic(stat);
            }
        }
    }

    private class ForumLoader extends RojacWorker<Void, Forum> {
        @Override
        protected Void perform() throws Exception {
            try {
                publish(storage.getForumAH().getAllForums());
            } catch (StorageException e) {
                log.error("Can not load forum list", e);
                throw e;
            }

            return null;
        }

        @Override
        protected void process(List<Forum> chunks) {
            Forum[] forums = chunks.toArray(new Forum[chunks.size()]);
            forumsModel.fillForums(forums);
            int[] ids = new int[forums.length];

            for (int i = 0, forumsLength = forums.length; i < forumsLength; i++) {
                ids[i] = forums[i].getForumId();
            }

            loadForumStatistic(ids);
        }
    }
}
