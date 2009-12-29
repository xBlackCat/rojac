package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.gui.IActionListener;
import org.xblackcat.rojac.gui.IItemView;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.view.message.AItemView;
import org.xblackcat.rojac.service.janus.commands.AffectedIds;

import javax.swing.*;

/**
 * @author xBlackCat
 */

public class ThreadDoubleView extends AItemView {
    private final IItemView masterView;
    private final IItemView slaveView;

    /**
     * Create combined forum thread view. Contains from master (upper component) and slave (lover component).
     *
     * @param verticalSplit
     * @param mainFrame
     */
    public ThreadDoubleView(IItemView mv, IItemView sv, boolean verticalSplit, IRootPane mainFrame) {
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
                AffectedIds results = new AffectedIds();
                results.addMessageId(messageId);
                masterView.updateData(results);
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

    public void updateData(AffectedIds messageId) {
        masterView.updateData(messageId);
        slaveView.updateData(messageId);
    }
}
