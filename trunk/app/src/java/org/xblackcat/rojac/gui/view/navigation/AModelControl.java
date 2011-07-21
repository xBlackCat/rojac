package org.xblackcat.rojac.gui.view.navigation;

/**
 * @author xBlackCat Date: 21.07.11
 */
public abstract class AModelControl {
    // Helper methods
    abstract void safeRemoveChild(AGroupItem parent, AnItem forum);

    abstract void addChild(AGroupItem parent, AnItem child);

    abstract void itemUpdated(AnItem item);
}
