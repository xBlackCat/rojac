package org.xblackcat.rojac.service.janus.commands;

import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.service.janus.IJanusService;

/**
 * @author xBlackCat
 */

public interface IRequest {
    AffectedMessage[] process(IProgressTracker trac, IJanusService janusService) throws RojacException;

    String getName();
}
