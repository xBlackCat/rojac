package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.gui.IActionListener;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.IItemView;
import org.xblackcat.rojac.gui.view.AnItemView;
import org.xblackcat.rojac.gui.view.message.MessageView;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.datahandler.IPacketProcessor;
import org.xblackcat.rojac.util.ShortCutUtils;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author xBlackCat
 */

public class ThreadDoubleView extends AnItemView {
    private final IItemView masterView;
    private final IItemView slaveView;

    /**
     * Create combined forum thread view. Contains from master (upper component) and slave (lover component).
     *
     * @param mv
     * @param sv
     * @param verticalSplit
     * @param appControl
     */
    public ThreadDoubleView(IItemView mv, IItemView sv, boolean verticalSplit, IAppControl appControl) {
        // Copy master view id
        super(mv.getId(), appControl);
        this.masterView = mv;
        this.slaveView = sv;

        masterView.addActionListener(new IActionListener() {
            public void itemGotFocus(Integer forumId, Integer messageId) {
                slaveView.loadItem(messageId);
            }

            public void itemLostFocus(Integer forumId, Integer messageId) {
            }

            public void itemUpdated(Integer forumId, Integer messageId) {
            }
        });

        slaveView.addActionListener(new IActionListener() {
            public void itemGotFocus(Integer forumId, Integer messageId) {
            }

            public void itemLostFocus(Integer forumId, Integer messageId) {
            }

            public void itemUpdated(Integer forumId, Integer messageId) {
            }
        });

        JSplitPane splitPane = new JSplitPane();
        splitPane.setBottomComponent(slaveView.getComponent());
        splitPane.setTopComponent(masterView.getComponent());
        splitPane.setOrientation(verticalSplit ? JSplitPane.VERTICAL_SPLIT : JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(.1);
        add(splitPane);

        ShortCutUtils.mergeInputMaps(this, slaveView.getComponent());
        ShortCutUtils.mergeInputMaps(this, masterView.getComponent());

        slaveView.getComponent().addPropertyChangeListener(MessageView.MESSAGE_VIEWED_FLAG, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                masterView.getComponent().firePropertyChange(
                        MessageView.MESSAGE_VIEWED_FLAG,
                        ((Integer) evt.getOldValue()).intValue(),
                        ((Integer) evt.getNewValue()).intValue()
                );
            }
        });
    }

    public void loadItem(int itemId) {
        masterView.loadItem(itemId);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    protected IPacketProcessor<IPacket>[] getProcessors() {
        return new IPacketProcessor[]{
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
    public int getVisibleId() {
        return slaveView.getVisibleId();
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
