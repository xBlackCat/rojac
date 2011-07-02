package org.xblackcat.rojac.gui;

import java.util.EventListener;

/**
 * @author xBlackCat
 */

public interface IStateListener extends EventListener {
    void stateChanged(IView source, IState newState);
}
