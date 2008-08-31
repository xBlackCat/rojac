package org.xblackcat.rojac.service.converter;

/**
 * Date: 20 лют 2008
 *
 * @author xBlackCat
 */

public enum TagEnum {
    Bold {
        public ITag getTag() {
            return new BoldTestTag();
        }
    };

    public abstract ITag getTag();
}
