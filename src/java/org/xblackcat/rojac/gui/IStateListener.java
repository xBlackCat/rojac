package org.xblackcat.rojac.gui;

import org.xblackcat.rojac.gui.view.ViewId;

import java.util.EventListener;

/**
 * @author xBlackCat
 */

public interface IStateListener extends EventListener {
    void stateChanged(ViewId viewId, IState newState);
}
