package org.xblackcat.rojac.service.janus.commands;

/**
 * @author xBlackCat
 */

abstract class ARequest implements IRequest {
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }
}
