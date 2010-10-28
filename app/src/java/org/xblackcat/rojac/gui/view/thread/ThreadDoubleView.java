package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.gui.IActionListener;
import org.xblackcat.rojac.gui.IItemView;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.view.message.AItemView;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.datahandler.IPacketProcessor;
import org.xblackcat.rojac.service.janus.commands.AffectedMessage;
import org.xblackcat.rojac.util.ShortCutUtils;

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
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(.4);
        add(splitPane);

        ShortCutUtils.mergeInputMaps(this, slaveView.getComponent());
        ShortCutUtils.mergeInputMaps(this, masterView.getComponent());
    }

    public void loadItem(AffectedMessage messageId) {
        masterView.loadItem(messageId);
    }

    @Override
    protected IPacketProcessor<? extends IPacket>[] getProcessors() {
        return new IPacketProcessor[] {
                new IPacketProcessor<IPacket>() {
                    @Override
                    public void process(IPacket p) {
                        masterView.processPacket(p);
                        slaveView.processPacket(p);
                    }
                }
        };
    }

    @Override
    public void makeVisible(int messageId) {
        masterView.makeVisible(messageId);
        slaveView.makeVisible(messageId);
    }

    @Override
    public boolean containsItem(int messageId) {
        return masterView.containsItem(messageId);
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
