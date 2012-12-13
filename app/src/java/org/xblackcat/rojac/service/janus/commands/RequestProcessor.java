package org.xblackcat.rojac.service.janus.commands;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.UserHelper;
import org.xblackcat.rojac.service.executor.TaskType;
import org.xblackcat.rojac.service.executor.TaskTypeEnum;
import org.xblackcat.rojac.service.janus.JanusService;
import org.xblackcat.rojac.service.progress.IProgressController;
import org.xblackcat.rojac.util.RojacWorker;

import static org.xblackcat.rojac.service.options.Property.RSDN_USER_NAME;
import static org.xblackcat.rojac.service.options.Property.SYNCHRONIZER_USE_GZIP;

/**
 * Synchronization processor. Processes requests to Janus WS and passes results to a result processor.
 *
 * @author xBlackCat
 */
@TaskType(TaskTypeEnum.Synchronization)
public class RequestProcessor<T> extends RojacWorker<Void, Void> {
    private static final Log log = LogFactory.getLog(RequestProcessor.class);

    private final Class<? extends IRequest<T>>[] requests;
    private final IResultHandler<T> handler;

    private final IProgressController progressController = ServiceFactory.getInstance().getProgressControl();
    private final ILogTracker tracker = new ILogTracker() {
        @Override
        public void addLodMessage(Message message, Object... arguments) {
            progressController.fireJobProgressChanged(message, arguments);
        }

        @Override
        public void postException(Throwable t) {
            log.warn("Got exception during synchronization", t);
            progressController.fireException(Message.Synchronize_Message_Exception, ExceptionUtils.getStackTrace(t));
        }

        @Override
        public void updateProgress(long current, long total) {
            progressController.fireJobProgressChanged(current, total);
        }
    };

    @SafeVarargs
    public RequestProcessor(IResultHandler<T> handler, Class<? extends IRequest<T>>... requests) {
        this.handler = handler;
        this.requests = requests;
    }

    @Override
    protected Void perform() throws Exception {
        progressController.fireJobStart();

        final String userName = RSDN_USER_NAME.get();
        final JanusService janusService = new JanusService(userName, UserHelper.getUserPassword());

        progressController.fireJobProgressChanged(Message.Synchronize_Message_Start);
        Boolean useCompression = SYNCHRONIZER_USE_GZIP.get();
        try {
            janusService.init(useCompression);
            progressController.fireJobProgressChanged(
                    useCompression ?
                            Message.Synchronize_Message_CompressionUsed :
                            Message.Synchronize_Message_CompressionNotUsed
            );

            janusService.testConnection();

            for (Class<? extends IRequest<T>> r : requests) {
                if (log.isDebugEnabled()) {
                    log.debug("Process request: " + r.getName());
                }

                janusService.clearCookies();
                r.newInstance().process(handler, tracker, janusService);
            }
        } catch (Exception e) {
            // Just in case
            log.debug("There is an exception during execute commands", e);

            tracker.postException(e);
        }

        return null;
    }

    @Override
    protected void done() {
//        progressController.fireJobProgressChanged(1, 1);
        progressController.fireJobStop(Message.Synchronize_Message_Done);
        if (log.isDebugEnabled()) {
            log.debug("Requests are processed.");
        }
    }

}
