package org.xblackcat.rojac.gui.dialog.subscribtion;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.component.ACancelAction;
import org.xblackcat.rojac.gui.component.AnOkAction;
import org.xblackcat.rojac.gui.component.InvokeExtMarkDialogAction;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.*;
import org.xblackcat.rojac.service.janus.commands.Request;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.util.RojacWorker;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

/**
 * @author xBlackCat
 */
public class SubscriptionDialog extends JDialog {
    private static final Log log = LogFactory.getLog(SubscriptionDialog.class);

    private SubscribeForumModel model = new SubscribeForumModel();
    private final IDataHandler handler = new IDataHandler() {
        @Override
        public void processPacket(IPacket packet) {
            if (packet instanceof ForumsUpdated) {
                new ForumLoader(model).execute();
            }
        }
    };
    private final IDataDispatcher dispatcher = ServiceFactory.getInstance().getDataDispatcher();
    private final Runnable onClose;

    public SubscriptionDialog(final Window owner, Runnable onClose) {
        super(owner, Message.Dialog_Subscription_Title.get(), ModalityType.MODELESS);

        this.onClose = onClose;

        initializeLayout();

        pack();
        setSize(400, 500);

        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // Handle event 'forums loaded'
                if (e.getFirstRow() == 0 &&
                        e.getLastRow() == Integer.MAX_VALUE &&
                        e.getColumn() == TableModelEvent.ALL_COLUMNS &&
                        e.getType() == TableModelEvent.UPDATE) {
                    if (model.getRowCount() == 0) {
                        // No forums exists. Ask for load them
                        int res = JLOptionPane.showConfirmDialog(
                                SubscriptionDialog.this,
                                Message.WarnDialog_NoForums_Question.get(),
                                Message.WarnDialog_NoForums_Title.get(),
                                JOptionPane.YES_NO_OPTION);

                        if (res == JOptionPane.YES_OPTION) {
                            Request.GET_FORUMS_LIST.process(SubscriptionDialog.this);
                        }
                    }
                }
            }
        });

        dispatcher.addDataHandler(handler);

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

        content.add(WindowsUtils.createToolBar(
                WindowsUtils.setupImageButton("update", new UpdateForumListAction(SubscriptionDialog.this)),
                null,
                WindowsUtils.setupImageButton("mark_read_extented", new InvokeExtMarkDialogAction(this))
        ), BorderLayout.NORTH);

        content.add(new JScrollPane(forumList), BorderLayout.CENTER);
        content.add(WindowsUtils.createButtonsBar(
                this,
                Message.Button_Ok,
                new AnOkAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        final SubscriptionChangedPacket packet = model.getSubscription();
                        if (packet.isNotEmpty()) {
                            new ForumSubscriber(packet).execute();
                        }

                        closeDialog();
                    }
                },
                new ACancelAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        closeDialog();
                    }
                }
        ), BorderLayout.SOUTH);

        setContentPane(content);
    }

    private void closeDialog() {
        if (onClose != null) {
            try {
                onClose.run();
            } catch (Exception e) {
                log.warn("Exception in 'onClose' handler", e);
            }
        }
        dispatcher.removeDataHandler(handler);
        dispose();
    }

    private class ForumSubscriber extends RojacWorker<Void, Void> {
        private final SubscriptionChangedPacket packet;

        public ForumSubscriber(SubscriptionChangedPacket packet) {
            this.packet = packet;
        }

        @Override
        protected Void perform() throws Exception {
            IForumAH fah = ServiceFactory.getInstance().getStorage().getForumAH();
            for (SubscriptionChangedPacket.Subscription s : packet.getNewSubscriptions()) {
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
