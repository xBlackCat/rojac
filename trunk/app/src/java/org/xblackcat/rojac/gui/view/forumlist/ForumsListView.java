package org.xblackcat.rojac.gui.view.forumlist;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumStatistic;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.IViewLayout;
import org.xblackcat.rojac.gui.PopupMouseAdapter;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.gui.component.ShortCut;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.gui.theme.ViewIcon;
import org.xblackcat.rojac.gui.view.AView;
import org.xblackcat.rojac.gui.view.ViewType;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.datahandler.*;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.UIUtils;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

/**
 * Main class for forum view.
 *
 * @author xBlackCat
 */
public class ForumsListView extends AView {
    private static final Log log = LogFactory.getLog(ForumsListView.class);

    // Data and models
    private final ForumTableModel forumsModel = new ForumTableModel();
    private final ForumsRowFilter forumsRowFilter = new ForumsRowFilter();
    private final TableRowSorter<ForumTableModel> forumTableModelTableRowSorter = new TableRowSorter<>(forumsModel);
    private final JToggleButton filled_only;
    private final JToggleButton subscribed_only;
    private final JToggleButton unread_only;

    private final PacketDispatcher packetDispatcher = new PacketDispatcher(
            new IPacketProcessor<SetForumReadPacket>() {
                @Override
                public void process(SetForumReadPacket p) {
                    loadForumStatistic(p.getForumId());
                }
            },
            new IPacketProcessor<ForumsUpdated>() {
                @Override
                public void process(ForumsUpdated p) {
                    new ForumLoader().execute();
                }
            },
            new IPacketProcessor<IForumUpdatePacket>() {
                @Override
                public void process(IForumUpdatePacket p) {
                    loadForumStatistic(p.getForumIds());
                }
            },
            new IPacketProcessor<SetSubThreadReadPacket>() {
                @Override
                public void process(SetSubThreadReadPacket p) {
                    loadForumStatistic(p.getForumId());
                }
            },
            new IPacketProcessor<SetPostReadPacket>() {
                @Override
                public void process(SetPostReadPacket p) {
                    loadForumStatistic(p.getPost().getForumId());
                }
            },
            new IPacketProcessor<SubscriptionChangedPacket>() {
                @Override
                public void process(SubscriptionChangedPacket p) {
                    for (SubscriptionChangedPacket.Subscription s : p.getNewSubscriptions()) {
                        forumsModel.setSubscribed(s.getForumId(), s.isSubscribed());
                    }
                }
            }
    );

    public ForumsListView(IAppControl appControl) {
        super(appControl);
        final JTable forums = new JTable(forumsModel);
        forums.setTableHeader(null);
        add(new JScrollPane(forums));

        forums.setDefaultRenderer(ForumData.class, new MultiLineForumRenderer());
//        forums.setDefaultRenderer(ForumData.class, new ForumCellRenderer());
        forums.addMouseListener(new PopupMouseAdapter() {
            private ForumData getForumData(MouseEvent e) {
                int ind = forums.rowAtPoint(e.getPoint());

                int modelInd = forums.convertRowIndexToModel(ind);

                return forumsModel.getValueAt(modelInd, 0);
            }

            @Override
            protected void triggerDoubleClick(MouseEvent e) {
                int forumId = getForumData(e).getForumId();
                ForumsListView.this.appControl.openTab(ViewType.Forum.makeId(forumId));
            }

            @Override
            protected void triggerPopup(MouseEvent e) {
                JPopupMenu menu = PopupMenuBuilder.getForumViewMenu(
                        getForumData(e).getForum(), ForumsListView.this.appControl
                );

                final Point p = e.getPoint();
                menu.show(e.getComponent(), p.x, p.y);
            }
        });

        forumTableModelTableRowSorter.setComparator(0, new ForumDataComparator());
        forumTableModelTableRowSorter.setStringConverter(new ForumsTableStringConverter());
        forumTableModelTableRowSorter.setSortable(0, true);
        forumTableModelTableRowSorter.setSortsOnUpdates(false);
        forumTableModelTableRowSorter.setSortKeys(Arrays.asList(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
        forumTableModelTableRowSorter.setRowFilter(forumsRowFilter);
        forumTableModelTableRowSorter.sort();
        forums.setRowSorter(forumTableModelTableRowSorter);

        forumsModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    forumTableModelTableRowSorter.sort();
                }
            }
        });

        filled_only = WindowsUtils.registerToggleButton(
                this,
                "filled_only",
                new FilterSetAction(ShortCut.ShowOnlyNotEmpty, ForumFilterState.NotEmpty)
        );
        subscribed_only = WindowsUtils.registerToggleButton(
                this,
                "subscribed_only",
                new FilterSetAction(ShortCut.ShowOnlySubscribed, ForumFilterState.Subscribed)
        );
        unread_only = WindowsUtils.registerToggleButton(
                this,
                "unread_only",
                new FilterSetAction(ShortCut.ShowOnlyUnread, ForumFilterState.Unread)
        );

        JToolBar toolBar = WindowsUtils.createToolBar(
                filled_only,
                subscribed_only,
                unread_only
        );


        add(toolBar, BorderLayout.NORTH);

        new ForumLoader().execute();
    }

    private void updateButtonsState() {
        filled_only.setSelected(forumsRowFilter.is(ForumFilterState.NotEmpty));
        subscribed_only.setSelected(forumsRowFilter.is(ForumFilterState.Subscribed));
        unread_only.setSelected(forumsRowFilter.is(ForumFilterState.Unread));
    }

    @Override
    public IViewLayout storeLayout() {
        return new ForumListViewLayout(forumsRowFilter.getState());
    }

    @Override
    public void setupLayout(IViewLayout o) {
        if (o instanceof ForumListViewLayout) {
            forumsRowFilter.setState(((ForumListViewLayout) o).getStates());

            updateButtonsState();
        }
    }

    @Override
    public String getTabTitle() {
        return Message.View_Forums_Title.get();
    }

    @Override
    public Icon getTabTitleIcon() {
        return UIUtils.getIcon(ViewIcon.ForumList);
    }

    @Override
    public JPopupMenu getTabTitleMenu() {
        return null;
    }

    private void loadForumStatistic(int... forumId) {
        new ForumUpdater(forumId).execute();
    }

    @Override
    public final void processPacket(IPacket packet) {
        packetDispatcher.dispatch(packet);
    }

    private class ForumUpdater extends AForumUpdater<Void, ForumStatistic> {
        private final int[] forumIds;

        public ForumUpdater(int... forumIds) {
            this.forumIds = forumIds;
        }

        @Override
        protected Void perform() throws Exception {
            for (int forumId : forumIds) {
                publish(getForumStatistic(forumId));
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

    private class ForumLoader extends AForumUpdater<Void, ForumData> {
        @Override
        protected Void perform() throws Exception {
            try {
                for (Forum f : fah.getAllForums()) {
                    int forumId = f.getForumId();

                    publish(
                            new ForumData(
                                    f,
                                    getForumStatistic(forumId)
                            )
                    );
                }
            } catch (StorageException e) {
                log.error("Can not load forum list", e);
                throw e;
            }

            return null;
        }

        @Override
        protected void process(List<ForumData> forums) {
            forumsModel.fillForums(forums);
        }
    }

    private class FilterSetAction extends AButtonAction {
        protected final ForumFilterState option;

        public FilterSetAction(ShortCut shortCut, ForumFilterState option) {
            super(shortCut);
            this.option = option;
        }

        public void actionPerformed(ActionEvent e) {
            boolean newState = !forumsRowFilter.is(option);
            forumsRowFilter.set(option, newState);
            forumTableModelTableRowSorter.sort();
            updateButtonsState();
        }
    }
}
