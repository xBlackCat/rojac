package org.xblackcat.rojac.gui;

import org.xblackcat.rojac.data.DiscussionStatistic;
import org.xblackcat.rojac.gui.hint.HintContainer;
import org.xblackcat.rojac.gui.hint.HintInfo;
import org.xblackcat.rojac.gui.theme.ReadStatusIcon;
import org.xblackcat.rojac.gui.tray.IStatisticListener;
import org.xblackcat.rojac.gui.view.model.ReadStatus;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.RojacWorker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
* 10.05.12 13:59
*
* @author xBlackCat
*/
class UnreadRepliesHintController implements IStatisticListener {
    private final HintContainer hintContainer;
    private final IAppControl appControl;

    private HintInfo hintInfo;

    public UnreadRepliesHintController(IAppControl appControl, HintContainer hintContainer) {
        this.hintContainer = hintContainer;
        this.appControl = appControl;
    }

    @Override
    public void statisticUpdated(DiscussionStatistic stat) {
        if (hintInfo != null) {
            hintContainer.removeHint(hintInfo);
            hintInfo = null;
        }

        final int unreadReplies = stat.getUnreadReplies();
        if (unreadReplies > 0) {
            Icon hintIcon = ReadStatusIcon.Response.getIcon(ReadStatus.Unread);
            final JLabel hint = new JLabel(Message.Hint_YouHaveUnreadReplies.get(unreadReplies));

            hintInfo = hintContainer.addHint(hintIcon, hint, null, 0);

            hint.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            hint.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    new RojacWorker<Void, Integer>() {
                        @Override
                        protected Void perform() throws Exception {
                            final IMessageAH mAH = Storage.get(IMessageAH.class);

                            Integer postId = mAH.getFirstUnreadReply(Property.RSDN_USER_ID.get());
                            if (postId != null) {
                                publish(postId);
                            } else {
                                // Should I panic?
                            }

                            return null;
                        }

                        @Override
                        protected void process(java.util.List<Integer> chunks) {
                            for (Integer id : chunks) {
                                appControl.openMessage(id, Property.OPEN_MESSAGE_BEHAVIOUR_UNREAD_REPLY.get());
                            }
                        }
                    }.execute();
                }
            });
        }
    }
}
