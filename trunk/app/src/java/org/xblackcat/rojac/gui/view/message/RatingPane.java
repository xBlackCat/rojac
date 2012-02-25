package org.xblackcat.rojac.gui.view.message;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.TablecellBalloonTip;
import net.java.balloontip.styles.BalloonTipStyle;
import net.java.balloontip.styles.RoundedBalloonStyle;
import org.xblackcat.rojac.data.Rating;
import org.xblackcat.rojac.data.User;
import org.xblackcat.rojac.gui.hint.UserInfoPane;
import org.xblackcat.rojac.service.storage.IRatingAH;
import org.xblackcat.rojac.service.storage.IUserAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.RojacWorker;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

/**
 * @author xBlackCat
 */

class RatingPane extends JPanel {
    private final RatingListModel marksModel;
    private final int messageId;
    private JTable marksList;

    public RatingPane(int messageId) {
        super(new BorderLayout());
        this.messageId = messageId;

        marksModel = new RatingListModel();
        marksList = new JTable(marksModel);

        marksList.setTableHeader(null);
        marksList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        marksList.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        marksList.setColumnSelectionAllowed(false);
        marksList.setRowSelectionAllowed(true);
        marksList.setShowVerticalLines(false);
        marksList.setDefaultRenderer(MarkItem.class, new RateTableCellRenderer());

        initializeLayout();

        setSize(200, 100);

        updateData();
    }

    private void updateData() {
        new RatingUpdater().execute();
    }

    private void initializeLayout() {
        Color color = new Color(0xFFFFCC);
        setBackground(color);

        final ListSelectionModel selectionModel = marksList.getSelectionModel();

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }

                final int row = selectionModel.getMaxSelectionIndex();

                if (row != -1) {
                    MarkItem item = (MarkItem) marksModel.getValueAt(row, 0);

                    if (item.isNewRate()) {
                        // TODO: ask for removing new rate
                    } else {
                        final UserInfoPane userInfo = new UserInfoPane();
                        userInfo.setUserInfo(item.getUser());
                        userInfo.setPreferredSize(new Dimension(300, 300));

//                        BalloonTipStyle tipStyle = new LeftRightRoundedBalloonStyle(5, 5, userInfo.getBackground(), Color.black);
                        BalloonTipStyle tipStyle = new RoundedBalloonStyle(5, 5, userInfo.getBackground(), Color.black);

                        LeftCenterPositioner positioner = new LeftCenterPositioner(15, 15);
                        final BalloonTip tip = new TablecellBalloonTip(marksList, userInfo, row, 1,
                                tipStyle, BalloonTip.Orientation.RIGHT_ABOVE,
                                BalloonTip.AttachLocation.EAST, 15, 15, false);
//                        final BalloonTip tip = new TablecellBalloonTip(marksList, userInfo, row, 0, tipStyle, positioner, null);

                        tip.setVisible(true);

                        selectionModel.addListSelectionListener(new ListSelectionListener() {
                            @Override
                            public void valueChanged(ListSelectionEvent e) {
                                if (!selectionModel.isSelectedIndex(row)) {
                                    selectionModel.removeListSelectionListener(this);
                                    tip.closeBalloon();
                                }
                            }
                        });
                    }
                }
            }
        });


        JScrollPane t = new JScrollPane(marksList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        t.setPreferredSize(new Dimension(200, 100));
        t.setBackground(color);
        marksList.setBackground(color);

        add(t, BorderLayout.CENTER);
    }

    public void trackFocus(final Runnable onFocusLost) {
        marksList.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                // Clear tooltips
                marksModel.setData(RatingListModel.NO_MARKS);
                onFocusLost.run();
            }
        });

        marksList.requestFocus();
    }

    private class RatingUpdater extends RojacWorker<Void, MarkItem[]> {
        @Override
        protected Void perform() throws Exception {
            IUserAH userAH = Storage.get(IUserAH.class);

            Rating[] ratings = Storage.get(IRatingAH.class).getRatingsByMessageId(messageId);

//            NewRating[] ownRatings = Storage.get(INewRatingAH.class).getNewRatingsByMessageId(messageId);

            TIntObjectMap<MarkItem> userMarks = new TIntObjectHashMap<>();

            for (Rating r : ratings) {
                int userId = r.getUserId();

                MarkItem item = userMarks.get(userId);

                if (item == null) {
                    User user = userAH.getUserById(userId);

                    item = new MarkItem(userId, user);
                    userMarks.put(userId, item);
                }

                item.addMark(r.getRate());
            }

//            int i = 0;
//            while (i < ownRatings.length) {
//                NewRating r = ownRatings[i++];
//                items[ind++] = new MarkItem(r);
//            }
//
            publish(userMarks.values(new MarkItem[userMarks.size()]));

            return null;
        }

        @Override
        protected void process(List<MarkItem[]> chunks) {
            MarkItem[] items = chunks.iterator().next();

            int maxWidth = 0;

            for (MarkItem mi : items) {
                maxWidth = Math.max(maxWidth, mi.getMarkIcons().getIconWidth());
            }


            marksModel.setData(items);
            TableColumn column = marksList.getColumnModel().getColumn(0);

            maxWidth += 5;
            column.setPreferredWidth(maxWidth);
            column.setWidth(maxWidth);
            column.setMaxWidth(maxWidth);
        }
    }
}
