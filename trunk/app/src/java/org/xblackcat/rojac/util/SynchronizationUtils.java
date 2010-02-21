package org.xblackcat.rojac.util;

import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.MainFrame;
import org.xblackcat.rojac.service.PacketType;
import org.xblackcat.rojac.service.ProcessPacket;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.executor.IExecutor;
import org.xblackcat.rojac.service.janus.commands.AffectedMessage;
import org.xblackcat.rojac.service.janus.commands.IResultHandler;
import org.xblackcat.rojac.service.janus.commands.Request;
import org.xblackcat.rojac.service.options.Property;

import static org.xblackcat.rojac.service.options.Property.SYNCHRONIZER_LOAD_USERS;

/**
 * @author xBlackCat
 */

public final class SynchronizationUtils {
    private static final String SCHEDULED_TASK_ID = "SCHEDULED_SYNCHRONIZER";

    private SynchronizationUtils() {
    }

    public static void startSynchronization(MainFrame mainFrame) {
        Request requests =
                SYNCHRONIZER_LOAD_USERS.get() ?
                        Request.SYNCHRONIZE_WITH_USERS :
                        Request.SYNCHRONIZE;

        requests.process(mainFrame, new NewMessagesHandler(mainFrame));
    }

    public static void loadExtraMessages(MainFrame mainFrame) {
        Request.EXTRA_MESSAGES.process(mainFrame, new NewMessagesHandler(mainFrame));        
    }

    public static void setScheduleSynchronizer(MainFrame mainFrame) {
        Integer period = Property.SYNCHRONIZER_SCHEDULE_PERIOD.get();

        IExecutor executor = ServiceFactory.getInstance().getExecutor();
        executor.killTimer(SCHEDULED_TASK_ID);

        if (period <= 0) {
            return;
        }

        executor.setupPeriodicTask(SCHEDULED_TASK_ID, new ScheduleSynchronization(mainFrame), period);
    }

    private static class NewMessagesHandler implements IResultHandler {
        private final IRootPane rootPane;

        private NewMessagesHandler(IRootPane rootPane) {
            this.rootPane = rootPane;
        }

        @Override
        public void process(AffectedMessage... messages) {
            rootPane.processPacket(new ProcessPacket(PacketType.AddMessages, messages));
        }
    }

    private static class ScheduleSynchronization implements Runnable {
        private final MainFrame mainFrame;

        public ScheduleSynchronization(MainFrame mainFrame) {
            this.mainFrame = mainFrame;
        }

        @Override
        public void run() {
            startSynchronization(mainFrame);
        }
    }
}
