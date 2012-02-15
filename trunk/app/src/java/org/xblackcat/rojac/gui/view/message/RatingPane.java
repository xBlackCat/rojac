package org.xblackcat.rojac.gui.view.message;

import org.xblackcat.rojac.data.NewRating;
import org.xblackcat.rojac.data.Rating;
import org.xblackcat.rojac.data.User;
import org.xblackcat.rojac.service.storage.INewRatingAH;
import org.xblackcat.rojac.service.storage.IRatingAH;
import org.xblackcat.rojac.service.storage.IUserAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.RojacWorker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @author xBlackCat
 */

class RatingPane extends JPanel {

    private final RatingListModel marksModel;
    private final int messageId;
    private JList<MarkItem> marksList;

    public RatingPane(int messageId) {
        super(new BorderLayout());
        this.messageId = messageId;

        marksModel = new RatingListModel();
        marksList = new JList<>(marksModel);

        initializeLayout();

        updateData();

        setSize(200, 100);
    }

    private void updateData() {
        new RatingUpdater().execute();
    }

    private void initializeLayout() {
        Color color = new Color(0xFFFFCC);
        setBackground(color);

        marksList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
                    int row = marksList.locationToIndex(e.getPoint());

                    if (row != -1) {
                        MarkItem item = marksModel.getElementAt(row);

                        if (item.isNewRate()) {
                            // TODO: ask for removing new rate
                        } else {
                            // TODO: show user information dialog
                        }
                    }
                }
            }
        });

        marksList.setCellRenderer(new RateTableCellRenderer());

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
                onFocusLost.run();
            }
        });

        marksList.requestFocus();
    }

    private class RatingUpdater extends RojacWorker<Void, MarkItem> {
        @Override
        protected Void perform() throws Exception {
            IUserAH userAH = Storage.get(IUserAH.class);

            Rating[] ratings = Storage.get(IRatingAH.class).getRatingsByMessageId(messageId);

            NewRating[] ownRatings = Storage.get(INewRatingAH.class).getNewRatingsByMessageId(messageId);

            MarkItem[] items = new MarkItem[ratings.length + ownRatings.length];

            int ind = 0;
            while (ind < ratings.length) {
                Rating r = ratings[ind];

                User user = userAH.getUserById(r.getUserId());

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
    }
}
