package org.xblackcat.rojac.gui.view.navigation;

/**
 * 22.02.12 19:56
 *
 * @author xBlackCat
 */
public interface ILoadTask<V> {
    V doBackground() throws Exception;

    void doSwing(V data);
}
