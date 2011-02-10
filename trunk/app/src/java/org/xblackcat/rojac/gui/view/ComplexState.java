package org.xblackcat.rojac.gui.view;

import org.xblackcat.rojac.gui.IViewState;

/**
 * @author xBlackCat
 */

public final class ComplexState implements IViewState {
    private static final long serialVersionUID = 1L;

    private IViewState masterState;
    private IViewState slaveState;

    public ComplexState(IViewState masterState, IViewState slaveState) {
        this.masterState = masterState;
        this.slaveState = slaveState;
    }

    public IViewState getMasterState() {
        return masterState;
    }

    public IViewState getSlaveState() {
        return slaveState;
    }
}
