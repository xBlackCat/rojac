package org.xblackcat.rojac.gui.view.navigation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.table.TableColumnExt;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.IViewLayout;
import org.xblackcat.rojac.gui.PopupMouseAdapter;
import org.xblackcat.rojac.gui.view.AView;
import org.xblackcat.rojac.gui.view.model.FavoriteType;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.datahandler.*;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * @author xBlackCat Date: 15.07.11
 */
public class NavigationView extends AView {
    private static final Log log = LogFactory.getLog(NavigationView.class);

    private final PacketDispatcher packetDispatcher = new PacketDispatcher(
            new IPacketProcessor<FavoritesUpdatedPacket>() {
                @Override
                public void process(FavoritesUpdatedPacket p) {
                    model.getFavoritesDecorator().reloadFavorites();
                }
            },
            new IPacketProcessor<FavoriteCategoryUpdatedPacket>() {
                @Override
                public void process(FavoriteCategoryUpdatedPacket p) {
                    model.getFavoritesDecorator().updateFavoriteData(FavoriteType.Category);
                }
            },
            new IPacketProcessor<SetForumReadPacket>() {
                @Override
                public void process(SetForumReadPacket p) {
                    model.getForumDecorator().loadForumStatistic(p.getForumId());
                    model.getFavoritesDecorator().updateFavoriteData(null);
                }
            },
            new IPacketProcessor<ForumsUpdated>() {
                @Override
                public void process(ForumsUpdated p) {
                    model.getForumDecorator().reloadForums();
                }
            },
            new IPacketProcessor<IForumUpdatePacket>() {
                @Override
                public void process(IForumUpdatePacket p) {
                    model.getForumDecorator().loadForumStatistic(p.getForumIds());
                    model.getFavoritesDecorator().updateFavoriteData(null);
                }
            },
            new IPacketProcessor<SetSubThreadReadPacket>() {
                @Override
                public void process(SetSubThreadReadPacket p) {
                    model.getForumDecorator().loadForumStatistic(p.getForumId());
                    model.getFavoritesDecorator().updateFavoriteData(null);
                }
            },
            new IPacketProcessor<SetPostReadPacket>() {
                @Override
                public void process(SetPostReadPacket p) {
                    model.getForumDecorator().loadForumStatistic(p.getForumId());
                    model.getFavoritesDecorator().updateFavoriteData(null);
                }
            },
            new IPacketProcessor<SubscriptionChangedPacket>() {
                @Override
                public void process(SubscriptionChangedPacket p) {
                    for (SubscriptionChangedPacket.Subscription s : p.getNewSubscriptions()) {
                        model.getForumDecorator().updateSubscribed(s.getForumId(), s.isSubscribed());
                    }
                }
            }
    );

    private NavigationModel model;
    private JXTreeTable viewTable;

    public NavigationView(IAppControl appControl) {
        super(appControl);

        model = new NavigationModel();
        viewTable = new JXTreeTable();
        viewTable.setAutoCreateColumnsFromModel(false);
        viewTable.setTreeTableModel(model);
        viewTable.setTableHeader(null);
        viewTable.setColumnMargin(3);

        TableColumnModel columnModel = viewTable.getColumnModel();
        columnModel.addColumn(new TableColumnExt(0, 100));
        TableColumnExt infoColumn = new TableColumnExt(1, 80, new InfoCellRenderer(), null);
        infoColumn.setMaxWidth(140);
        infoColumn.setMinWidth(30);
        columnModel.addColumn(infoColumn);

        viewTable.setTreeCellRenderer(new LabelCellRenderer());

        viewTable.addMouseListener(new PopupMouseAdapter() {
            private AnItem getItem(Point point) {
                TreePath path = viewTable.getPathForLocation(point.x, point.y);

                return path == null ? null : (AnItem) path.getLastPathComponent();
            }

            @Override
            protected void triggerDoubleClick(MouseEvent e) {
                AnItem item = getItem(e.getPoint());
                if (item != null) {
                    item.onDoubleClick(NavigationView.this.appControl);
                }
            }

            @Override
            protected void triggerPopup(MouseEvent e) {
                AnItem item = getItem(e.getPoint());

                if (item == null) {
                    return;
                }

                JPopupMenu menu = item.getContextMenu(NavigationView.this.appControl);
                if (menu != null) {
                    final Point p = e.getPoint();
                    menu.show(e.getComponent(), p.x, p.y);
                }
            }
        });


        JScrollPane container = new JScrollPane(viewTable);

        add(container, BorderLayout.CENTER);

        model.load();
    }

    @Override
    public String getTabTitle() {
        return Message.View_Navigation_Title.get();
    }

    @Override
    public IViewLayout storeLayout() {
        AnItem root = model.getRoot();

        int i = 0;
        int size = root.getChildCount();

        boolean[] expanded = new boolean[size];

        while (i < size) {
            AnItem item = root.getChild(i);

            expanded[i] = viewTable.isExpanded(model.getPathToRoot(item));

            i++;
        }

        return new NavigationLayout(expanded);
    }

    @Override
    public void setupLayout(IViewLayout o) {
        if (o instanceof NavigationLayout) {
            NavigationLayout l = (NavigationLayout) o;

            AnItem root = model.getRoot();

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
                     viewTable.expandPath(model.getPathToRoot(item));
                }

                i++;
            }
        }
    }

    @Override
    public Icon getTabTitleIcon() {
        return null;
    }

    @Override
    public JPopupMenu getTabTitleMenu() {
        return null;
    }

    @Override
    public void processPacket(IPacket packet) {
        packetDispatcher.dispatch(packet);
    }

}
