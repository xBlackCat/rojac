package org.xblackcat.rojac.service.janus.commands;

/**
 * @author xBlackCat
 */

abstract class ARequest<T> implements IRequest<T> {
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }
}
