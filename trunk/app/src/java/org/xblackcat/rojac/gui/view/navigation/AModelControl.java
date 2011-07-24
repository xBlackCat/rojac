package org.xblackcat.rojac.gui.view.navigation;

/**
 * @author xBlackCat Date: 21.07.11
 */
abstract class AModelControl {
    // Helper methods
    abstract <T extends AnItem> void safeRemoveChild(AGroupItem<T> parent, T forum);

    abstract <T extends AnItem> void addChild(AGroupItem<T> parent, T child);

    abstract void itemUpdated(AnItem item);

    public abstract void removeChildren(AGroupItem children);
}
