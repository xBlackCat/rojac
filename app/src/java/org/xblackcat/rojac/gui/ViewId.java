package org.xblackcat.rojac.gui;

import org.xblackcat.rojac.gui.view.ViewType;

import java.io.Serializable;

/**
 * @author xBlackCat
 */

public interface ViewId extends Serializable {
    ViewType getType();
}
