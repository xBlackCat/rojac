package org.xblackcat.rojac.service.commands;

import org.xblackcat.rojac.RojacException;

/**
 * @author xBlackCat
 */

public interface IResultHandler {
    void process(AffectedPosts results) throws RojacException;
}
