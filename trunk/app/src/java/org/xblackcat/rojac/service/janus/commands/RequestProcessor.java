package org.xblackcat.rojac.service.janus.commands;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.RojacHelper;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.janus.JanusService;
import org.xblackcat.rojac.service.progress.IProgressController;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

import static ch.lambdaj.Lambda.*;
import static org.xblackcat.rojac.service.options.Property.RSDN_USER_NAME;
import static org.xblackcat.rojac.service.options.Property.SYNCHRONIZER_USE_GZIP;

/**
 * @author xBlackCat
 */
public class RequestProcessor extends SwingWorker<Void, Void> {
    private static final Log log = LogFactory.getLog(RequestProcessor.class);

    private final List<IRequest> requests;
    private final IResultHandler handler;

    protected AffectedIds affectedIds;
    protected final IProgressController progressController = ServiceFactory.getInstance().getProgressControl();

    public RequestProcessor(IResultHandler handler, IRequest... requests) {
        this.handler = handler;
        this.requests = Arrays.asList(requests);
    }

    @Override
    protected Void doInBackground() throws Exception {
        progressController.fireJobStart();

        final String userName = RSDN_USER_NAME.get();
        final JanusService janusService = new JanusService(userName, RojacHelper.getUserPassword());

        progressController.fireJobProgressChanged(0f, "Synchronize [user name = " + userName + "].");
        janusService.init(SYNCHRONIZER_USE_GZIP.get());
        janusService.testConnection();

        if (log.isDebugEnabled()) {
            log.debug("Start process request(s): " + joinFrom(requests, IRequest.class).getName());
        }

        IProgressTracker trac = new RequestTracker();

        try {
            affectedIds = aggregate(
                    requests,
                    new AffectedPostsAggregator(),
                    on(IRequest.class).process(trac, janusService)
            );
        } catch (Exception e) {
            // Just in case
            log.error("There is an exception in one of commands", e);
            affectedIds = new AffectedIds();
        }

        return null;
    }

    @Override
    protected void done() {
        if (handler != null) {
            handler.process(affectedIds);
        }

        progressController.fireJobStop();
        if (log.isDebugEnabled()) {
            log.debug("Requests are processed.");
        }
    }

}
