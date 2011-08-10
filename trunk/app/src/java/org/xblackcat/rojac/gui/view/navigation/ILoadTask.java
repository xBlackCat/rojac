package org.xblackcat.rojac.gui.view.navigation;

/**
 * @author xBlackCat
 */
interface ILoadTask<V> {
    ILoadTask[] NO_TASKS = new ILoadTask[0];

    V doBackground() throws Exception;

    void doSwing(V data);
}
