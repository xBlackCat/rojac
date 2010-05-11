package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.gui.IActionListener;
import org.xblackcat.rojac.gui.IItemView;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.view.message.AItemView;
import org.xblackcat.rojac.service.datahandler.ProcessPacket;
import org.xblackcat.rojac.service.janus.commands.AffectedMessage;

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
     * @param mv
     * @param sv
     * @param verticalSplit
     * @param mainFrame
     */
    public ThreadDoubleView(IItemView mv, IItemView sv, boolean verticalSplit, IRootPane mainFrame) {
        super(mainFrame);
        this.masterView = mv;
        this.slaveView = sv;

        masterView.addActionListener(new IActionListener() {
            public void itemGotFocus(AffectedMessage messageId) {
                slaveView.loadItem(messageId);
            }

            public void itemLostFocus(AffectedMessage messageId) {
            }

            public void itemUpdated(AffectedMessage messageId) {
            }
        });

        slaveView.addActionListener(new IActionListener() {
            public void itemGotFocus(AffectedMessage messageId) {
            }

            public void itemLostFocus(AffectedMessage messageId) {
            }

            public void itemUpdated(AffectedMessage messageId) {
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

    public void loadItem(AffectedMessage messageId) {
        masterView.loadItem(messageId);
    }

    public void processPacket(ProcessPacket messageId) {
        masterView.processPacket(messageId);
        slaveView.processPacket(messageId);
    }

    @Override
    public void makeVisible(ITreeItem item) {
        masterView.makeVisible(item);
        slaveView.makeVisible(item);
    }

    @Override
    public ITreeItem searchItem(AffectedMessage id) {
        return masterView.searchItem(id);
    }

    @Override
    public void addActionListener(IActionListener l) {
        masterView.addActionListener(l);
        slaveView.addActionListener(l);
    }

    @Override
    public void removeActionListener(IActionListener l) {
        masterView.removeActionListener(l);
        slaveView.removeActionListener(l);
    }

    @Override
    public String getTabTitle() {
        return masterView.getTabTitle();
    }
}
