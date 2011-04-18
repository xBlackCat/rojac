package org.xblackcat.rojac.gui.dialog.subscribtion;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.gui.component.ACancelAction;
import org.xblackcat.rojac.gui.component.AnOkAction;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.SubscriptionChanged;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.util.RojacWorker;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @author xBlackCat
 */
public class SubscriptionDialog extends JDialog {
    private SubscribeForumModel model = new SubscribeForumModel();

    public SubscriptionDialog(Window owner) {
        super(owner);

        initializeLayout();

        pack();
        setSize(400, 500);

        new ForumLoader(model).execute();
    }

    private void initializeLayout() {
        JPanel content = new JPanel(new BorderLayout(5, 5));

        JTable forumList = new JTable(model);

        TableColumn c1 = forumList.getColumnModel().getColumn(0);
        c1.setWidth(20);
        c1.setPreferredWidth(20);
        c1.setMaxWidth(30);

        TableColumn c2 = forumList.getColumnModel().getColumn(1);
        c2.setWidth(100);
        c2.setPreferredWidth(100);
        c2.setMaxWidth(120);

        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
        forumList.setRowSorter(sorter);

        sorter.setSortsOnUpdates(true);
        sorter.setSortable(0, true);
        sorter.setSortable(1, true);
        sorter.setSortable(2, true);

        sorter.setSortKeys(Arrays.asList(new RowSorter.SortKey(2, SortOrder.ASCENDING), new RowSorter.SortKey(1, SortOrder.ASCENDING)));

        forumList.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        content.add(new JScrollPane(forumList), BorderLayout.CENTER);
        content.add(WindowsUtils.createButtonsBar(
                this,
                Messages.Button_Ok,
                new AnOkAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        final SubscriptionChanged packet = model.getSubscription();
                        if (packet.isNotEmpty()) {
                            new ForumSubscriber(packet).execute();
                        }
                        dispose();
                    }
                },
                new ACancelAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                }
        ), BorderLayout.SOUTH);

        setContentPane(content);
    }

    private class ForumSubscriber extends RojacWorker<Void, Void> {
        private final SubscriptionChanged packet;

        public ForumSubscriber(SubscriptionChanged packet) {
            this.packet = packet;
        }

        @Override
        protected Void perform() throws Exception {
            IForumAH fah = ServiceFactory.getInstance().getStorage().getForumAH();
            for (SubscriptionChanged.Subscription s : packet.getNewSubscriptions()) {
                fah.setSubscribeForum(s.getForumId(), s.isSubscribed());
            }
            return null;
        }

        @Override
        protected void done() {
            ServiceFactory.getInstance().getDataDispatcher().processPacket(packet);
        }
    }

}
