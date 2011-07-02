package org.xblackcat.rojac.gui.view;

import org.xblackcat.rojac.gui.IState;

/**
* @author xBlackCat
*/
public final class ThreadState implements IState {
    private static final long serialVersionUID = 1L;

    private int openedMessageId;

    public ThreadState(int openedMessageId) {
        this.openedMessageId = openedMessageId;
    }

    public int openedMessageId() {
        return openedMessageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ThreadState that = (ThreadState) o;

        return openedMessageId == that.openedMessageId;

    }

    @Override
    public int hashCode() {
        return openedMessageId;
    }
}
