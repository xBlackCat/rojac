package org.xblackcat.rojac.data;

import java.io.Serializable;

/**
 * Date: 27 черв 2008
 *
 * @author xBlackCat
 */

public interface IRSDNable<T extends Serializable> {
    T getRSDNObject();
}
