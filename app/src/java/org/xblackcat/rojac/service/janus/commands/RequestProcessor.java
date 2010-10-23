package org.xblackcat.rojac.service.janus.commands;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.UserHelper;
import org.xblackcat.rojac.service.executor.TaskType;
import org.xblackcat.rojac.service.executor.TaskTypeEnum;
import org.xblackcat.rojac.service.janus.JanusService;
import org.xblackcat.rojac.service.progress.IProgressController;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.Arrays;
import java.util.List;

import static ch.lambdaj.Lambda.forEach;
import static ch.lambdaj.Lambda.joinFrom;
import static org.xblackcat.rojac.service.options.Property.RSDN_USER_NAME;
import static org.xblackcat.rojac.service.options.Property.SYNCHRONIZER_USE_GZIP;

/**
 * Synchronization processor. Processes requests to Janus WS and passes results to a result processor.
 *
 * @author xBlackCat
 */
@TaskType(TaskTypeEnum.Synchronization)
public class RequestProcessor extends RojacWorker<Void, Void> {
    private static final Log log = LogFactory.getLog(RequestProcessor.class);

    private final List<IRequest> requests;
    private final IResultHandler handler;

    private AffectedMessage[] processedMessages;
    private final IProgressController progressController = ServiceFactory.getInstance().getProgressControl();
    private final IProgressTracker tracker = new IProgressTracker() {
        @Override
        public void addLodMessage(Messages message, Object... arguments) {
            progressController.fireJobProgressChanged(0, message, arguments);
        }

        @Override
        public void postException(Throwable t) {
            progressController.fireIdle(Messages.Synchronize_Command_Exception, ExceptionUtils.getFullStackTrace(t));
        }

        @Override
        public void updateProgress(int current, int total) {
            progressController.fireJobProgressChanged((float) current / total);
        }
    };

    public RequestProcessor(IResultHandler handler, IRequest... requests) {
        this.handler = handler;
        this.requests = Arrays.asList(requests);
    }

    @Override
    protected Void perform() throws Exception {
        progressController.fireJobStart();

        final String userName = RSDN_USER_NAME.get();
        final JanusService janusService = new JanusService(userName, UserHelper.getUserPassword());

        progressController.fireJobProgressChanged(0f, Messages.Synchronize_Command_Start);
        Boolean useCompression = SYNCHRONIZER_USE_GZIP.get();
        janusService.init(useCompression);
        progressController.fireJobProgressChanged(
                0f,
                useCompression ?
                        Messages.Synchronize_Command_CompressionUsed :
                        Messages.Synchronize_Command_CompressionNotUsed
        );

        try {
            janusService.testConnection();

            if (log.isDebugEnabled()) {
                log.debug("Process request(s): " + joinFrom(requests, IRequest.class).getName());
            }

            forEach(requests, IRequest.class).process(handler, tracker, janusService);
        } catch (Exception e) {
            // Just in case
            log.debug("There is an exception in one of commands", e);

            tracker.postException(e);

            processedMessages = new AffectedMessage[0];
        }

        return null;
    }

    @Override
    protected void done() {
        if (handler != null) {
            handler.process(processedMessages);
        }

        progressController.fireJobProgressChanged(1);
        progressController.fireJobStop(Messages.Synchronize_Command_Done);
        if (log.isDebugEnabled()) {
            log.debug("Requests are processed.");
        }
    }

}
