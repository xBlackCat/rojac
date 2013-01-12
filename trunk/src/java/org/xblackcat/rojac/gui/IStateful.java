package org.xblackcat.rojac.gui;

/**
 * @author xBlackCat
 */
public interface IStateful {
    /**
     * Gets current state of the view.
     *
     * @return state object or <code>null</code> if view have no state to store.
     */
    IState getObjectState();

    /**
     * Sets state of view. The state will be set after the view is fully initialized. If passed <code>null</code> a
     * default state will be used for the view.
     *
     * @param state correspond view
     */
    void setObjectState(IState state);
}
