package org.xblackcat.rojac.gui;

/**
 * @author xBlackCat Date: 15.07.11
 */
public interface ILayoutful {
    /**
     * Returns an object with current layout informaion.
     *
     * @return layout config object.
     */
    IViewLayout storeLayout();

    /**
     * Restores the view layout by data stored in the layout object.
     *
     * @param o layout config object.
     */
    void setupLayout(IViewLayout o);

    void addInfoChangeListener(IInfoChangeListener l);

    void removeInfoChangeListener(IInfoChangeListener l);
}
