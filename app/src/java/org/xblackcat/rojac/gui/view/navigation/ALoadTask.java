package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IStorage;

/**
 * @author xBlackCat
 */
public abstract class ALoadTask<V> implements ILoadTask<V> {
    protected final IStorage storage = ServiceFactory.getInstance().getStorage();
}
