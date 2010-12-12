package org.xblackcat.rojac.gui.view;

import java.io.Serializable;

/**
* @author xBlackCat
*/
final class ThreadState implements Serializable {
    private static final long serialVersionUID = 1L;

    private int openedMessageId;

    public ThreadState(int openedMessageId) {
        this.openedMessageId = openedMessageId;
    }

    public int openedMessageId() {
        return openedMessageId;
    }
}
