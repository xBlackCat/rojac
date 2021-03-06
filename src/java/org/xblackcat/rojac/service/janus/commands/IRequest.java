package org.xblackcat.rojac.service.janus.commands;

import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.service.janus.IJanusService;

/**
 * @author xBlackCat
 */

public interface IRequest<T> {
    void process(IResultHandler<T> handler, ILogTracker trac, IJanusService janusService) throws RojacException;

    String getName();
}
