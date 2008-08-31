package org.xblackcat.rojac.gui.frame.message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.NewRating;
import org.xblackcat.rojac.data.Rating;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Date: 3 бер 2008
 *
 * @author xBlackCat
 */

class RatingDialog extends JDialog {
    private final IStorage storage = ServiceFactory.getInstance().getStorage();
    private static final Log log = LogFactory.getLog(RatingDialog.class);

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
        try {
            Rating[] ratings = storage.getRatingAH().getRatingsByMessageId(messageId);

            NewRating[] ownRatings = storage.getNewRatingAH().getNewRatingsByMessageId(messageId);

            marksModel.setData(ratings, ownRatings);
        } catch (StorageException e) {
            log.error("Can not load rating details for message [id=" + messageId + "]", e);
        }
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
