package org.xblackcat.rojac.gui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.dialogs.progress.IProgressTracker;
import org.xblackcat.rojac.service.RojacHelper;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.commands.AffectedIds;
import org.xblackcat.rojac.service.commands.IRequest;
import org.xblackcat.rojac.service.commands.IResultHandler;
import org.xblackcat.rojac.service.janus.JanusService;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.progress.IProgressControl;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

import static ch.lambdaj.Lambda.*;

/**
 * @author xBlackCat
 */
class RequestProcessor extends SwingWorker<Void, Void> {
    private static final Log log = LogFactory.getLog(RequestProcessor.class);

    private final List<IRequest> requests;
    private final IResultHandler handler;

    protected AffectedIds affectedIds;
    protected final IProgressControl progressControl = ServiceFactory.getInstance().getProgressControl();

    public RequestProcessor(IResultHandler handler, IRequest... requests) {
        this.handler = handler;
        this.requests = Arrays.asList(requests);
    }

    @Override
    protected Void doInBackground() throws Exception {
        final JanusService janusService = new JanusService(Property.RSDN_USER_NAME.get(), RojacHelper.getUserPassword());
        janusService.init(Property.SYNCHRONIZER_USE_GZIP.get());
        janusService.testConnection();

        if (log.isDebugEnabled()) {
            log.debug("Start process request(s): " + joinFrom(requests, IRequest.class).getName());
        }
        progressControl.fireJobStart();

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

        progressControl.fireJobStop();
        if (log.isDebugEnabled()) {
            log.debug("Requests are processed.");
        }
    }

}
