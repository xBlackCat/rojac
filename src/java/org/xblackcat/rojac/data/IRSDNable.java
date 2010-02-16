package org.xblackcat.rojac.data;

import java.io.Serializable;

/**
 * @author xBlackCat
 */

public interface IRSDNable<T extends Serializable> {
    T getRSDNObject();
}
