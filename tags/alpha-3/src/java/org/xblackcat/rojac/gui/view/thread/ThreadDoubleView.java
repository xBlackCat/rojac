package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.gui.*;
import org.xblackcat.rojac.gui.view.AView;
import org.xblackcat.rojac.gui.view.ThreadState;
import org.xblackcat.rojac.gui.view.message.MessageView;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.ShortCutUtils;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author xBlackCat
 */

public class ThreadDoubleView extends AView implements IItemView {
    private final IItemView masterView;
    private final IItemView slaveView;

    /**
     * Create combined forum thread view. Contains from master (upper component) and slave (lover component).
     *
     * @param mv            left/top view (master view)
     * @param sv            right/bottom view (slave view)
     * @param verticalSplit split orientation (true - vertical, false - horizontal)
     * @param appControl
     */
    public ThreadDoubleView(IItemView mv, IItemView sv, boolean verticalSplit, IAppControl appControl) {
        // Copy master view id
        super(mv.getId(), appControl);
        this.masterView = mv;
        this.slaveView = sv;

        masterView.addStateChangeListener(new IStateListener() {
            @Override
            public void stateChanged(IView source, IViewState newState) {
                if (newState instanceof ThreadState) {
                    slaveView.loadItem(((ThreadState) newState).openedMessageId());
                }
                fireViewStateChanged();
            }
        });

        IInfoChangeListener infoChangeListener = new IInfoChangeListener() {
            @Override
            public void infoChanged() {
                fireInfoChanged();
            }
        };
        masterView.addInfoChangeListener(infoChangeListener);
        slaveView.addInfoChangeListener(infoChangeListener);

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
        slaveView.getComponent().addPropertyChangeListener(MessageView.MESSAGE_LOADED, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                masterView.getComponent().firePropertyChange(MessageView.MESSAGE_LOADED, 0, 1);
            }
        });
    }

    public void loadItem(int itemId) {
        masterView.loadItem(itemId);
    }

    @Override
    public void makeVisible(int messageId) {
        masterView.makeVisible(messageId);
        slaveView.makeVisible(messageId);
    }

    @Override
    public ComplexState getState() {
        assert RojacUtils.checkThread(true);

        return new ComplexState(masterView.getState(), slaveView.getState());
    }

    @Override
    public void setState(IViewState state) {
        assert RojacUtils.checkThread(true);

        if (state == null) {
            return;
        }

        if (!(state instanceof ComplexState) && !(state instanceof ThreadState)) {
            RojacUtils.fireDebugException("Invalid state object " + state.toString() + " [" + state.getClass() + "]");
            return;
        }

        // Old state support
        if (state instanceof ThreadState) {
            masterView.setState(state);
            return;
        }

        ComplexState s = (ComplexState) state;

        masterView.setState(s.getMasterState());
        slaveView.setState(s.getSlaveState());
    }

    @Override
    public void setupLayout(IViewLayout o) {
        if (o instanceof ComplexLayout) {
            ComplexLayout l = (ComplexLayout) o;

            masterView.setupLayout(l.getMasterLayout());
            slaveView.setupLayout(l.getSlaveLayout());
        }
    }

    @Override
    public IViewLayout storeLayout() {
        return new ComplexLayout(masterView.storeLayout(), slaveView.storeLayout());
    }

    @Override
    public boolean containsItem(int messageId) {
        return masterView.containsItem(messageId);
    }

    @Override
    public String getTabTitle() {
        return masterView.getTabTitle();
    }

    @Override
    public JPopupMenu getTabTitleMenu() {
        return masterView.getTabTitleMenu();
    }

    @Override
    public Icon getTabTitleIcon() {
        return masterView.getTabTitleIcon();
    }

    @Override
    public final void processPacket(IPacket packet) {
        masterView.processPacket(packet);
        slaveView.processPacket(packet);
    }
}
