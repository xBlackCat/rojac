package org.xblackcat.rojac.gui.view.navigation;

/**
 * @author xBlackCat
 */
interface ILoadTask<V> {
    V doBackground() throws Exception;

    void doSwing(V data);
}
