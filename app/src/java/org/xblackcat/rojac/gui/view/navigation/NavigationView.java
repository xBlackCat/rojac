package org.xblackcat.rojac.gui.view.navigation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.table.TableColumnExt;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.IViewLayout;
import org.xblackcat.rojac.gui.NoViewLayout;
import org.xblackcat.rojac.gui.view.AView;
import org.xblackcat.rojac.service.datahandler.*;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * @author xBlackCat Date: 15.07.11
 */
public class NavigationView extends AView {
    private static final Log log = LogFactory.getLog(NavigationView.class);

    private final PacketDispatcher packetDispatcher = new PacketDispatcher(
            new IPacketProcessor<SetForumReadPacket>() {
                @Override
                public void process(SetForumReadPacket p) {
                    // TODO: update specified forum and all others items
//                    model.setRead(p.isRead(), p.getForumId());
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
                }
            },
            new IPacketProcessor<SetSubThreadReadPacket>() {
                @Override
                public void process(SetSubThreadReadPacket p) {
                    model.getForumDecorator().loadForumStatistic(p.getForumId());
                }
            },
            new IPacketProcessor<SetPostReadPacket>() {
                @Override
                public void process(SetPostReadPacket p) {
                    model.getForumDecorator().loadForumStatistic(p.getForumId());
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

    private NavModel model;
    private JXTreeTable viewTable;

    public NavigationView(IAppControl appControl) {
        super(appControl);

        model = new NavModel();
        viewTable = new JXTreeTable();
        viewTable.setAutoCreateColumnsFromModel(false);
        viewTable.setTreeTableModel(model);
        viewTable.setTableHeader(null);

        TableColumnModel columnModel = viewTable.getColumnModel();
        columnModel.addColumn(new TableColumnExt(0, 100));
        columnModel.addColumn(new TableColumnExt(1, 50, new InfoCellRenderer(), null));

        viewTable.setTreeCellRenderer(new LabelCellRenderer());

        JScrollPane container = new JScrollPane(viewTable);

        add(container, BorderLayout.CENTER);

        model.load();
    }

    @Override
    public String getTabTitle() {
        return null;
    }

    @Override
    public IViewLayout storeLayout() {
        return new NoViewLayout();
    }

    @Override
    public void setupLayout(IViewLayout o) {
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
