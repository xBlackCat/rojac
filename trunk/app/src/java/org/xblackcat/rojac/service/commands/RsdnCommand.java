package org.xblackcat.rojac.service.commands;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.gui.dialogs.progress.IProgressTracker;
import org.xblackcat.rojac.gui.dialogs.progress.ITask;
import org.xblackcat.rojac.service.RojacHelper;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.janus.JanusService;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IStorage;

import java.util.Arrays;
import java.util.Collection;

import static ch.lambdaj.Lambda.aggregate;
import static ch.lambdaj.Lambda.on;

/**
 * @author xBlackCat
 */

public class RsdnCommand implements ITask {
    protected final Log log = LogFactory.getLog(getClass());
    private boolean executed = false;

    protected final IStorage storage;
    private final IResultHandler resultHandler;

    private final Collection<IRequest> requests;

    public RsdnCommand(IResultHandler resultHandler, IRequest... requests) {
        this.resultHandler = resultHandler;
        this.requests = Arrays.asList(requests);

        storage = ServiceFactory.getInstance().getStorage();
    }

    public void doTask(IProgressTracker trac) throws RojacException {
        synchronized (this) {
            if (!executed) {
                executed = true;
            } else {
                throw new RsdnProcessorException("Can not execute command twice: " + this.getClass().getSimpleName());
            }
        }

        JanusService janusService = new JanusService(Property.RSDN_USER_NAME.get(), RojacHelper.getUserPassword());
        janusService.init(Property.SYNCHRONIZER_USE_GZIP.get());
        janusService.testConnection();

        AffectedPosts affectedPosts = aggregate(
                requests,
                new AffectedPostsAggregator(),
                on(IRequest.class).process(trac, janusService)
        );

        if (resultHandler != null) {
            resultHandler.process(affectedPosts);
        }
    }

}