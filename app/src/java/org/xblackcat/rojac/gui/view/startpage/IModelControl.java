package org.xblackcat.rojac.gui.view.startpage;

/**
 * 17.08.11 15:09
 *
 * @author xBlackCat
 */
interface IModelControl {
    // Helper methods
    <T extends AnItem> void safeRemoveChild(AGroupItem<T> parent, T child);

    <T extends AnItem> void addChild(AGroupItem<T> parent, T child);

    void itemUpdated(AnItem item);

    <T extends AnItem> void removeAllChildren(AGroupItem<T> parent);
}
