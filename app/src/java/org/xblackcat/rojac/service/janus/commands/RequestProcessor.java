package org.xblackcat.rojac.service.janus.commands;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.RojacHelper;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.janus.JanusService;
import org.xblackcat.rojac.service.progress.IProgressController;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.Arrays;
import java.util.List;

import static ch.lambdaj.Lambda.*;
import static org.xblackcat.rojac.service.options.Property.RSDN_USER_NAME;
import static org.xblackcat.rojac.service.options.Property.SYNCHRONIZER_USE_GZIP;

/**
 * @author xBlackCat
 */
public class RequestProcessor extends RojacWorker<Void, Void> {
    private static final Log log = LogFactory.getLog(RequestProcessor.class);

    private final List<IRequest> requests;
    private final IDataHandler handler;

    private AffectedIds affectedIds;
    private final IProgressController progressController = ServiceFactory.getInstance().getProgressControl();
    private final IProgressTracker tracker = new IProgressTracker() {
        @Override
        public void addLodMessage(Messages message, Object... arguments) {
            progressController.fireJobProgressChanged(0, message, arguments);
        }

        @Override
        public void postException(Throwable t) {
            progressController.fireIdle(Messages.SYNCHRONIZE_COMMAND_EXCEPTION, ExceptionUtils.getFullStackTrace(t));
        }

        @Override
        public void updateProgress(int current, int total) {
            progressController.fireJobProgressChanged((float) current / total);
        }
    };

    public RequestProcessor(IDataHandler handler, IRequest... requests) {
        this.handler = handler;
        this.requests = Arrays.asList(requests);
    }

    @Override
    protected Void perform() throws Exception {
        progressController.fireJobStart();

        final String userName = RSDN_USER_NAME.get();
        final JanusService janusService = new JanusService(userName, RojacHelper.getUserPassword());

        progressController.fireJobProgressChanged(0f, Messages.SYNCHRONIZE_COMMAND_START);
        Boolean useCompression = SYNCHRONIZER_USE_GZIP.get();
        janusService.init(useCompression);
        progressController.fireJobProgressChanged(0f, useCompression ? Messages.SYNCHRONIZE_COMMAND_USE_COMPRESSION : Messages.SYNCHRONIZE_COMMAND_DONT_USE_COMPRESSION);

        try {
            janusService.testConnection();

            if (log.isDebugEnabled()) {
                log.debug("Process request(s): " + joinFrom(requests, IRequest.class).getName());
            }

            affectedIds = aggregate(
                    requests,
                    new AffectedPostsAggregator(),
                    on(IRequest.class).process(tracker, janusService)
            );
        } catch (Exception e) {
            // Just in case
            log.debug("There is an exception in one of commands", e);

            tracker.postException(e);

            affectedIds = new AffectedIds();
        }

        return null;
    }

    @Override
    protected void done() {
        if (handler != null) {
            handler.updateData(affectedIds);
        }

        progressController.fireJobProgressChanged(1);
        progressController.fireJobStop(Messages.SYNCHRONIZE_COMMAND_DONE);
        if (log.isDebugEnabled()) {
            log.debug("Requests are processed.");
        }
    }

}
