package org.xblackcat.rojac.gui.view;

import org.xblackcat.rojac.gui.IViewState;

/**
* @author xBlackCat
*/
public final class ThreadState implements IViewState {
    private static final long serialVersionUID = 1L;

    private int openedMessageId;

    public ThreadState(int openedMessageId) {
        this.openedMessageId = openedMessageId;
    }

    public int openedMessageId() {
        return openedMessageId;
    }
}
