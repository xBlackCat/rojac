package org.xblackcat.rojac.gui.view.startpage;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.table.TableColumnExt;
import org.xblackcat.rojac.gui.*;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.gui.theme.ViewIcon;
import org.xblackcat.rojac.gui.tray.IStatisticListener;
import org.xblackcat.rojac.gui.view.AnItemView;
import org.xblackcat.rojac.gui.view.ViewType;
import org.xblackcat.rojac.gui.view.model.FavoriteType;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.datahandler.*;
import org.xblackcat.rojac.service.options.Property;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 13.12.12 14:50
 *
 * @author xBlackCat
 */
public class StartPageView extends AnItemView {
    public static final GridLayout LAYOUT_H = new GridLayout(1, 0, 10, 10);
    public static final GridLayout LAYOUT_V = new GridLayout(0, 1, 10, 10);
    private final NavigationModel navigationModel = new NavigationModel();
    private final RecentThreadsModel recentModel = new RecentThreadsModel();

    private final StatisticUpdateListener statisticUpdateListener = new StatisticUpdateListener(navigationModel);

    private final PacketDispatcher packetDispatcher = new PacketDispatcher(
            new APacketProcessor<OptionsUpdatedPacket>() {
                @Override
                public void process(OptionsUpdatedPacket p) {
                if (p.isPropertyAffected(Property.VIEW_RECENT_TOPIC_LIST_SIZE)) {
                    reloadLastPosts();
                }
                }
            },
            new APacketProcessor<IgnoreUserUpdatedPacket>() {
                @Override
                public void process(IgnoreUserUpdatedPacket p) {
                new LoadTaskExecutor(
                        navigationModel.forumDecorator.reloadForums(),
                        navigationModel.personalDecorator.reloadInfo(false)
                ).execute();
                reloadLastPosts();
                }
            },
            new APacketProcessor<FavoritesUpdatedPacket>() {
                @Override
                public void process(FavoritesUpdatedPacket p) {
                    new LoadTaskExecutor(
                    navigationModel.favoritesDecorator.reloadFavorites()
                    ).execute();
                }
            },
            new APacketProcessor<FavoriteCategoryUpdatedPacket>() {
                @Override
                public void process(FavoriteCategoryUpdatedPacket p) {
                    new LoadTaskExecutor(
                    navigationModel.favoritesDecorator.updateFavoriteData(FavoriteType.Category)
                    ).execute();
                }
            },
            new APacketProcessor<NewMessagesUpdatedPacket>() {
                @Override
                public void process(NewMessagesUpdatedPacket p) {
                    new LoadTaskExecutor(
                    navigationModel.personalDecorator.reloadOutbox()
                    ).execute();
                }
            },
            new APacketProcessor<IgnoreUpdatedPacket>() {
                @Override
                public void process(IgnoreUpdatedPacket p) {
                    new LoadTaskExecutor(
                    navigationModel.forumDecorator.loadForumStatistic(p.getForumId()),
                    navigationModel.personalDecorator.reloadInfo(false),
                    navigationModel.personalDecorator.reloadIgnored()
                    ).execute();
                }
            },
            new APacketProcessor<SetForumReadPacket>() {
                @Override
                public void process(SetForumReadPacket p) {
                    new LoadTaskExecutor(
                    navigationModel.personalDecorator.reloadInfo(false),
                    navigationModel.favoritesDecorator.updateFavoriteData(null),
                    navigationModel.forumDecorator.loadForumStatistic(p.getForumId())
                    ).execute();
                }
            },
            new APacketProcessor<ForumsUpdated>() {
                @Override
                public void process(ForumsUpdated p) {
                    new LoadTaskExecutor(
                    navigationModel.forumDecorator.reloadForums()
                    ).execute();
                }
            },
            new APacketProcessor<IForumUpdatePacket>() {
                @Override
                public void process(IForumUpdatePacket p) {
                new LoadTaskExecutor(
                        navigationModel.personalDecorator.reloadInfo(true),
                        navigationModel.favoritesDecorator.updateFavoriteData(null),
                        navigationModel.forumDecorator.loadForumStatistic(p.getForumIds())
                ).execute();
                reloadLastPosts();
                }
            },
            new APacketProcessor<SetSubThreadReadPacket>() {
                @Override
                public void process(SetSubThreadReadPacket p) {
                    new LoadTaskExecutor(
                    navigationModel.personalDecorator.reloadInfo(false),
                    navigationModel.favoritesDecorator.updateFavoriteData(null),
                    navigationModel.forumDecorator.loadForumStatistic(p.getForumId())
                    ).execute();
                }
            },
            new APacketProcessor<SetPostReadPacket>() {
                @Override
                public void process(SetPostReadPacket p) {
                    new LoadTaskExecutor(
                    navigationModel.favoritesDecorator.alterReadStatus(p.getPost(), p.isRead()),
                    navigationModel.forumDecorator.alterReadStatus(p.getPost(), p.isRead()),
                    navigationModel.personalDecorator.alterReadStatus(p.getPost(), p.isRead())
                    ).execute();
                }
            },
            new APacketProcessor<SubscriptionChangedPacket>() {
                @Override
                public void process(SubscriptionChangedPacket p) {
                Collection<ILoadTask> tasks = new ArrayList<>();
                for (SubscriptionChangedPacket.Subscription s : p.getNewSubscriptions()) {
                    ILoadTask task = navigationModel.forumDecorator.updateSubscribed(
                            s.getForumId(),
                            s.isSubscribed()
                    );
                    if (task != null) {
                        tasks.add(task);
                    }
                }

                if (!tasks.isEmpty()) {
                    new LoadTaskExecutor(tasks).execute();
                }
                }
            },
            new APacketProcessor<ReloadDataPacket>() {
                @Override
                public void process(ReloadDataPacket p) {
                new LoadTaskExecutor(
                        navigationModel.personalDecorator.reloadIgnored(),
                        navigationModel.personalDecorator.reloadInfo(true),
                        navigationModel.forumDecorator.reloadForums(),
                        navigationModel.favoritesDecorator.reloadFavorites()
                ).execute();
                reloadLastPosts();
                }
            }
    );

    private final JXTreeTable navigationTree;

    public StartPageView(IAppControl appControl) {
        super(ViewType.StartPage.makeId(0), appControl);

        setLayout(LAYOUT_H);

        navigationTree = setupNavigationComponent();

        JTable recentPostList = setupRecentPostList();

        final JPanel navPane = new JPanel(new BorderLayout(5, 5));
        navPane.setMinimumSize(new Dimension(220, 300));
        navPane.add(new JScrollPane(navigationTree));

        final JPanel recentPane = new JPanel(new BorderLayout(5, 5));
        recentPane.setMinimumSize(new Dimension(180, 300));
        recentPane.add(new JScrollPane(recentPostList));

        add(navPane);
        add(recentPane);

        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (getWidth() < navPane.getMinimumSize().width + recentPane.getMinimumSize().width) {
                    if (getLayout() == LAYOUT_H) {
                        setLayout(LAYOUT_V);
                    }
                } else {
                    if (getLayout() == LAYOUT_V) {
                        setLayout(LAYOUT_H);
                    }
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });
    }

    private JTable setupRecentPostList() {
        final JTable lastPostList = new JTable(recentModel);
        lastPostList.setTableHeader(null);
        final TableColumn column = lastPostList.getColumnModel().getColumn(0);
        column.setWidth(180);
        column.setMinWidth(100);

        lastPostList.setDefaultRenderer(LastPostInfo.class, new TopicCellRenderer());

        lastPostList.addMouseListener(new PopupMouseAdapter() {
            private LastPostInfo getTopicInfo(MouseEvent e) {
                int ind = lastPostList.rowAtPoint(e.getPoint());

                return recentModel.getValueAt(ind, 0);
            }

            @Override
            protected void triggerDoubleClick(MouseEvent e) {
                LastPostInfo info = getTopicInfo(e);
                OpenMessageMethod method = Property.OPEN_MESSAGE_BEHAVIOUR_RECENT_TOPICS.get();
                int messageId = info.getTopicRoot().getMessageId();

                appControl.openMessage(messageId, method);
            }

            @Override
            protected void triggerPopup(MouseEvent e) {
                LastPostInfo info = getTopicInfo(e);

                JPopupMenu menu = PopupMenuBuilder.getRecentPostsMenu(info.getTopicRoot(), appControl);

                final Point p = e.getPoint();
                menu.show(e.getComponent(), p.x, p.y);

            }
        });

        return lastPostList;
    }

    private JXTreeTable setupNavigationComponent() {
        navigationModel.addTreeModelListener(statisticUpdateListener);

        final JXTreeTable viewTable = new JXTreeTable();
        viewTable.setAutoCreateColumnsFromModel(false);
        viewTable.setTreeTableModel(navigationModel);
        viewTable.setTableHeader(null);
        viewTable.setColumnMargin(0);
        viewTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        TableColumnModel columnModel = viewTable.getColumnModel();
        columnModel.addColumn(new TableColumnExt(0, 180));
        TableColumnExt infoColumn = new TableColumnExt(1, 120, new InfoCellRenderer(), null);
        infoColumn.setMaxWidth(160);
        infoColumn.setMinWidth(40);
        columnModel.addColumn(infoColumn);

        viewTable.setTreeCellRenderer(new LabelCellRenderer(viewTable));

        viewTable.addMouseListener(
                new PopupMouseAdapter() {
                    private AnItem getItem(Point point) {
                        TreePath path = viewTable.getPathForLocation(point.x, point.y);

                        return path == null ? null : (AnItem) path.getLastPathComponent();
                    }

                    @Override
                    protected void triggerClick(MouseEvent e) {
                        if (Property.VIEW_NAVIGATION_OPEN_ONE_CLICK.get()) {
                            AnItem item = getItem(e.getPoint());
                            if (item != null) {
                                item.onDoubleClick(appControl);
                            }
                        }
                    }

                    @Override
                    protected void triggerDoubleClick(MouseEvent e) {
                        if (!Property.VIEW_NAVIGATION_OPEN_ONE_CLICK.get()) {
                            AnItem item = getItem(e.getPoint());
                            if (item != null) {
                                item.onDoubleClick(appControl);
                            }
                        }
                    }

                    @Override
                    protected void triggerPopup(MouseEvent e) {
                        AnItem item = getItem(e.getPoint());

                        if (item == null) {
                            return;
                        }

                        JPopupMenu menu = item.getContextMenu(appControl);
                        if (menu != null) {
                            final Point p = e.getPoint();
                            menu.show(e.getComponent(), p.x, p.y);
                        }
                    }
                }
        );

        // Expand Subscribed forums list by default
        viewTable.expandPath(navigationModel.getPathToRoot(navigationModel.getRoot().getChild(1)));

        return viewTable;
    }

    @Override
    public void loadItem(int itemId) {

    }

    @Override
    public boolean containsItem(int messageId) {
        return false;
    }

    @Override
    public void makeVisible(int messageId) {
    }

    @Override
    public IState getObjectState() {
        return null;
    }

    @Override
    public void setObjectState(IState state) {
    }

    @Override
    public String getTabTitle() {
        return Message.View_StartPage_Title.get();
    }

    @Override
    public Icon getTabTitleIcon() {
        return ViewIcon.StartPage;
    }

    @Override
    public JPopupMenu getTabTitleMenu() {
        return null;
    }

    @Override
    public void processPacket(IPacket packet) {
        packetDispatcher.dispatch(packet);
    }

    @Override
    public IViewLayout storeLayout() {
        AnItem root = navigationModel.getRoot();

        int i = 0;
        int size = root.getChildCount();

        boolean[] expanded = new boolean[size];

        while (i < size) {
            AnItem item = root.getChild(i);

            expanded[i] = navigationTree.isExpanded(navigationModel.getPathToRoot(item));

            i++;
        }

        return new NavigationLayout(expanded);
    }

    @Override
    public void setupLayout(IViewLayout o) {
        if (o instanceof NavigationLayout) {
            NavigationLayout l = (NavigationLayout) o;

            AnItem root = navigationModel.getRoot();

            int i = 0;
            int size = root.getChildCount();

            boolean[] expanded = l.getExpandedStatus();

            if (expanded.length < size) {
                // Prevent IndexOutOfBoundsException exception
                size = expanded.length;
            }

            while (i < size) {
                AnItem item = root.getChild(i);

                if (expanded[i]) {
                    navigationTree.expandPath(navigationModel.getPathToRoot(item));
                }

                i++;
            }
        }
    }

    public void addStatisticListener(IStatisticListener statisticListener) {
        statisticUpdateListener.addStatisticListener(statisticListener);
    }

    private void reloadLastPosts() {
        if (!recentModel.isLoading()) {
            recentModel.clear();

            new LatestPostsLoader(recentModel).execute();
        }
    }
}
