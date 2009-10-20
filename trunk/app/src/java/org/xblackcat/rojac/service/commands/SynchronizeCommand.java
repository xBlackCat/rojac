package org.xblackcat.rojac.service.commands;

import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.gui.frame.progress.IProgressTracker;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author xBlackCat
 */

public class SynchronizeCommand extends ARsdnCommand<AffectedPosts> {
    // Delegated commands
    private final ARsdnCommand[] commands;

    public SynchronizeCommand(IResultHandler<AffectedPosts> tiResultHandler) {
        super(tiResultHandler);

        commands = new ARsdnCommand[]{
                new PostChangesCommand(tiResultHandler),
//                new LoadUsersCommand(tiResultHandler),
                new GetNewPostsCommand(tiResultHandler),
                new LoadExtraMessagesCommand(tiResultHandler)
        };
    }

    @Override
    protected AffectedPosts process(IProgressTracker trac) throws RojacException {
        Collection<AffectedPosts> results = new LinkedList<AffectedPosts>();

        for (ARsdnCommand<AffectedPosts> c : commands) {
            c.janusService = janusService;

            results.add(c.process(trac));
        }

        return new AffectedPosts().merge(results.toArray(new AffectedPosts[results.size()]));
    }

    private static class DumbHandler implements IResultHandler<Boolean> {
        @Override
        public void process(Boolean results) throws RojacException {
        }
    }
}
