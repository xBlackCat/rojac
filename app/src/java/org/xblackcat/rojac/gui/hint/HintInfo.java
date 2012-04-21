package org.xblackcat.rojac.gui.hint;

import javax.swing.*;

/**
 * 21.04.12 14:12
 *
 * @author xBlackCat
 */
public final class HintInfo {
    private final Timer timer;

    protected HintInfo(Timer timer) {
        this.timer = timer;
    }

    void stopTimer() {
        timer.stop();
    }
}
