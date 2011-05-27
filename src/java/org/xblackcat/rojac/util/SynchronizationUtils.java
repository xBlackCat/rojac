package org.xblackcat.rojac.util;

import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.executor.IExecutor;
import org.xblackcat.rojac.service.janus.commands.Request;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.progress.ProgressChangeEvent;

import javax.swing.*;
import java.awt.*;

import static org.xblackcat.rojac.service.options.Property.SYNCHRONIZER_LOAD_USERS;

/**
 * @author xBlackCat
 */

public final class SynchronizationUtils {
    private static final String SCHEDULED_TASK_ID = "SCHEDULED_SYNCHRONIZER";

    private SynchronizationUtils() {
    }

    public static void startSynchronization(Window mainFrame) {
        Request requests =
                SYNCHRONIZER_LOAD_USERS.get() ?
                        Request.SYNCHRONIZE_WITH_USERS :
                        Request.SYNCHRONIZE;

        requests.process(mainFrame);
    }

    public static void setScheduleSynchronizer(Window mainFrame) {
        Integer period = Property.SYNCHRONIZER_SCHEDULE_PERIOD.get();

        IExecutor executor = ServiceFactory.getInstance().getExecutor();
        executor.killTimer(SCHEDULED_TASK_ID);

        if (period <= 0) {
            return;
        }

        // Convert minutes into seconds
        period *= 60;

        executor.setupPeriodicTask(SCHEDULED_TASK_ID, new ScheduleSynchronization(mainFrame), period);
    }

    public static String makeSizeString(ProgressChangeEvent e) {
        if (e.getProgress() == null || e.getProgress() < 0) {
            return "";
        }

        int size = e.getProgress();
        String factors = " KMGTE";
        int idx = -1;

        int lastSize;
        do {
            lastSize = size;
            size >>= 10;
            idx ++;
        } while (size > 0);

        return Messages.ProgressControl_AffectedBytes.get(lastSize, factors.charAt(idx));
    }

    private static class ScheduleSynchronization implements Runnable {
        private final Window mainFrame;

        public ScheduleSynchronization(Window mainFrame) {
            this.mainFrame = mainFrame;
        }

        @Override
        public void run() {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    startSynchronization(mainFrame);
                }
            });
        }
    }
}
