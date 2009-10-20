package org.xblackcat.rojac.service.converter;

/**
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
