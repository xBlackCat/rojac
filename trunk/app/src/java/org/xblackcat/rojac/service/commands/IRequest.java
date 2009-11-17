package org.xblackcat.rojac.service.commands;

import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.gui.dialogs.progress.IProgressTracker;
import org.xblackcat.rojac.service.janus.IJanusService;

/**
 * @author xBlackCat
 */

public interface IRequest {
    AffectedIds process(IProgressTracker trac, IJanusService janusService) throws RojacException;

    String getName();
}
