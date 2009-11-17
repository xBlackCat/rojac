package org.xblackcat.rojac.service.commands;

import ch.lambdaj.function.aggregate.Aggregator;
import gnu.trove.TIntHashSet;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.gui.dialogs.progress.IProgressTracker;
import org.xblackcat.rojac.service.options.Property;

import java.util.Collection;
import java.util.LinkedList;

import static ch.lambdaj.Lambda.*;

/**
 * @author xBlackCat
 */

public class SynchronizeCommand extends ARsdnCommand {
    // Delegated commands
    private final Collection<ARsdnCommand> commands;

    public SynchronizeCommand(IResultHandler tiResultHandler) {
        super(tiResultHandler);

        commands = new LinkedList<ARsdnCommand>();
        commands.add(new PostChangesCommand(tiResultHandler));

        if (Property.SYNCHRONIZER_LOAD_USERS.get()) {
                commands.add(new LoadUsersCommand(tiResultHandler));
        }
        commands.add(new GetNewPostsCommand(tiResultHandler));
        commands.add(new LoadExtraMessagesCommand(tiResultHandler));
    }

    @Override
    public AffectedPosts process(IProgressTracker trac) throws RojacException {
        forEach(commands).setJanusService(janusService);

        return aggregate(commands, new AffectedPostsAggregator(), on(ARsdnCommand.class).process(trac));
    }

    private static class AffectedPostsAggregator implements Aggregator<AffectedPosts> {
        @Override
        public AffectedPosts aggregate(Iterable<? extends AffectedPosts> iterable) {
            TIntHashSet mIds = new TIntHashSet();
            TIntHashSet fIds = new TIntHashSet();

            for (AffectedPosts p : iterable) {
                mIds.addAll(p.getAffectedMessageIds());
                fIds.addAll(p.getAffectedForumIds());
        }

            return new AffectedPosts(mIds, fIds);
        }
    }
}
