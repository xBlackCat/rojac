package org.xblackcat.rojac.gui.view.message;

import org.xblackcat.rojac.data.NewRating;
import org.xblackcat.rojac.data.Rating;
import org.xblackcat.rojac.data.User;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.executor.IExecutor;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.util.RojacWorker;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @author xBlackCat
 */

class RatingDialog extends JDialog {
    private final IStorage storage = ServiceFactory.getInstance().getStorage();

    private final MarksTableModel marksModel;
    private final int messageId;

    public RatingDialog(Window owner, int messageId) {
        super(owner, ModalityType.MODELESS);
        this.messageId = messageId;

        marksModel = new MarksTableModel();

        initializeLayout();

        updateData();

        setSize(200, 100);
    }

    private void updateData() {
        RojacWorker<Void, MarkItem> sw = new RojacWorker<Void, MarkItem>() {
            @Override
            protected Void perform() throws Exception {
                Rating[] ratings = storage.getRatingAH().getRatingsByMessageId(messageId);

                NewRating[] ownRatings = storage.getNewRatingAH().getNewRatingsByMessageId(messageId);

                MarkItem[] items = new MarkItem[ratings.length + ownRatings.length];

                int ind = 0;
                while (ind < ratings.length) {
                    Rating r = ratings[ind];

                    User user = storage.getUserAH().getUserById(r.getUserId());

                    items[ind] = new MarkItem(r, user);
                    ind++;
                }

                int i = 0;
                while (i < ownRatings.length) {
                    NewRating r = ownRatings[i++];
                    items[ind++] = new MarkItem(r);
                }

                publish(items);
                
                return null;
            }

            @Override
            protected void process(List<MarkItem> chunks) {
                marksModel.setData(chunks.toArray(new MarkItem[chunks.size()]));
            }
        };

        IExecutor executor = ServiceFactory.getInstance().getExecutor();
        executor.execute(sw);
    }

    private void initializeLayout() {
        final JTable marksTable = new JTable(marksModel);

        marksTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        marksTable.setPreferredScrollableViewportSize(new Dimension(75, 100));
        marksTable.setTableHeader(null);
        marksTable.setShowGrid(false);

        marksTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
                    int row = marksTable.rowAtPoint(e.getPoint());

                    if (row != -1) {
                        MarkItem item = marksModel.getValueAt(row, 0);

                        if (item.isNewRate()) {
                            // TODO: ask for removing new rate
                        } else {
                            // TODO: show user information dialog
                        }
                    }
                }
            }
        });

        TableColumnModel tcm = marksTable.getColumnModel();
        TableColumn cMark = tcm.getColumn(0);
        cMark.setResizable(true);
        cMark.setCellRenderer(new RateTableCellRenderer());

        JComponent t = new JScrollPane(marksTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(t, BorderLayout.CENTER);
    }
}
