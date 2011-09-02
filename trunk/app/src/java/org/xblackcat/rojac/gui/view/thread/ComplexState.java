package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.gui.IState;

/**
 * @author xBlackCat
 */

final class ComplexState implements IState {
    private static final long serialVersionUID = 1L;

    private IState masterState;
    private IState slaveState;

    public ComplexState(IState masterState, IState slaveState) {
        this.masterState = masterState;
        this.slaveState = slaveState;
    }

    public IState getMasterState() {
        return masterState;
    }

    public IState getSlaveState() {
        return slaveState;
    }

    @Override
    public boolean isNavigatable() {
        return masterState != null && masterState.isNavigatable() ||
                slaveState != null && slaveState.isNavigatable();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ComplexState that = (ComplexState) o;

        if (masterState != null ? !masterState.equals(that.masterState) : that.masterState != null) {
            return false;
        }
        if (slaveState != null ? !slaveState.equals(that.slaveState) : that.slaveState != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = masterState != null ? masterState.hashCode() : 0;
        result = 31 * result + (slaveState != null ? slaveState.hashCode() : 0);
        return result;
    }
}
