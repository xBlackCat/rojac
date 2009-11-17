package org.xblackcat.rojac.service.commands;

/**
 * @author xBlackCat
 */

abstract class ARequest implements IRequest {
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }
}
