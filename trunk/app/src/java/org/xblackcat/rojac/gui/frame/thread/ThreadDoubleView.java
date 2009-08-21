package org.xblackcat.rojac.gui.frame.thread;

import org.xblackcat.rojac.gui.IActionListener;
import org.xblackcat.rojac.gui.IMessageView;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.frame.message.AMessageView;

import javax.swing.*;
import java.awt.*;

/**
 * Date: 17 лип 2008
 *
 * @author xBlackCat
 */

public class ThreadDoubleView extends AMessageView {
    private final IMessageView masterView;
    private final IMessageView slaveView;

    /**
     * Create combined forum thread view. Contains from master (upper component) and slave (lover component).
     * @param verticalSplit
     * @param mainFrame
     */
    public ThreadDoubleView(IMessageView mv, IMessageView sv, boolean verticalSplit, IRootPane mainFrame) {
        super(new BorderLayout(), mainFrame);
        this.masterView = mv;
        this.slaveView = sv;

        masterView.addActionListener(new IActionListener() {
            public void itemGotFocus(int messageId) {
                slaveView.viewItem(messageId, false);
            }

            public void itemLostFocus(int messageId) {
            }

            public void itemUpdated(int messageId) {
            }
        });

        slaveView.addActionListener(new IActionListener() {
            public void itemGotFocus(int messageId) {
            }

            public void itemLostFocus(int messageId) {
            }

            public void itemUpdated(int messageId) {
                masterView.updateItem(messageId);
            }
        });

        JSplitPane splitPane = new JSplitPane();
        splitPane.setBottomComponent(slaveView.getComponent());
        splitPane.setTopComponent(masterView.getComponent());
        splitPane.setOrientation(verticalSplit ? JSplitPane.VERTICAL_SPLIT : JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(.5);
        add(splitPane);
    }

    public void viewItem(int messageId, boolean isNewMessage) {
        masterView.viewItem(messageId, false);
    }

    public void updateItem(int messageId) {
        masterView.updateItem(messageId);
        slaveView.updateItem(messageId);
    }
}
