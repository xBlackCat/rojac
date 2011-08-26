package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.gui.IViewLayout;

/**
 * @author xBlackCat
 */
class ComplexLayout implements IViewLayout {
    private static final long serialVersionUID = 2L;

    private IViewLayout masterLayout;
    private IViewLayout slaveLayout;
    private int dividerLocation;

    ComplexLayout(IViewLayout masterLayout, IViewLayout slaveLayout, int dividerLocation) {
        this.masterLayout = masterLayout;
        this.slaveLayout = slaveLayout;
        this.dividerLocation = dividerLocation;
    }

    public IViewLayout getMasterLayout() {
        return masterLayout;
    }

    public IViewLayout getSlaveLayout() {
        return slaveLayout;
    }

    public int getDividerLocation() {
        return dividerLocation;
    }
}
