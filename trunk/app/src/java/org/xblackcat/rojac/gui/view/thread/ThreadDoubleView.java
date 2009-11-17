package org.xblackcat.rojac.gui.view.thread;

import org.apache.commons.lang.ArrayUtils;
import org.xblackcat.rojac.gui.IActionListener;
import org.xblackcat.rojac.gui.IMessageView;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.view.message.AMessageView;
import org.xblackcat.rojac.service.commands.AffectedPosts;

import javax.swing.*;

/**
 * @author xBlackCat
 */

public class ThreadDoubleView extends AMessageView {
    private final IMessageView masterView;
    private final IMessageView slaveView;

    /**
     * Create combined forum thread view. Contains from master (upper component) and slave (lover component).
     *
     * @param verticalSplit
     * @param mainFrame
     */
    public ThreadDoubleView(IMessageView mv, IMessageView sv, boolean verticalSplit, IRootPane mainFrame) {
        super(mainFrame);
        this.masterView = mv;
        this.slaveView = sv;

        masterView.addActionListener(new IActionListener() {
            public void itemGotFocus(int messageId) {
                slaveView.loadItem(messageId);
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
                masterView.updateData(new AffectedPosts(new int[] {messageId}, ArrayUtils.EMPTY_INT_ARRAY));
            }
        });

        JSplitPane splitPane = new JSplitPane();
        splitPane.setBottomComponent(slaveView.getComponent());
        splitPane.setTopComponent(masterView.getComponent());
        splitPane.setOrientation(verticalSplit ? JSplitPane.VERTICAL_SPLIT : JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(.4);
        splitPane.setResizeWeight(.4);
        add(splitPane);
    }

    public void loadItem(int messageId) {
        masterView.loadItem(messageId);
    }

    public void updateData(AffectedPosts messageId) {
        masterView.updateData(messageId);
        slaveView.updateData(messageId);
    }
}
