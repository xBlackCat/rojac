package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.gui.IViewLayout;

/**
 * @author xBlackCat
 */
class ComplexLayout implements IViewLayout {
    private static final long serialVersionUID = 1L;

    private IViewLayout masterLayout;
    private IViewLayout slaveLayout;

    ComplexLayout(IViewLayout masterLayout, IViewLayout slaveLayout) {
        this.masterLayout = masterLayout;
        this.slaveLayout = slaveLayout;
    }

    public IViewLayout getMasterLayout() {
        return masterLayout;
    }

    public IViewLayout getSlaveLayout() {
        return slaveLayout;
    }
}
