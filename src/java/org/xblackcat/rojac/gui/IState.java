package org.xblackcat.rojac.gui;

import java.io.Serializable;

/**
 * Base interface for storing view state (selected item for threads for example).
 *
 * @author xBlackCat
 */

public interface IState extends Serializable {
    boolean isNavigatable();
}
