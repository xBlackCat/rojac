package org.xblackcat.rojac.gui.dialogs.progress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.executor.IExecutor;

import javax.swing.*;
import java.awt.*;

/**
 * @author xBlackCat
 */

public class ProgressTrackerDialog extends JDialog {
    private static final Log log = LogFactory.getLog(ProgressTrackerDialog.class);

    private JLabel statusMessage = new JLabel();
    private JTextArea logArea = new JTextArea();
    private JProgressBar logProgress = new JProgressBar();
    private ProgressTrackerDialog.RunnableTask task;

    public ProgressTrackerDialog(Window mainFrame, ITask task) {
        super(mainFrame, ModalityType.MODELESS);
        JPanel cp = new JPanel(new BorderLayout());

        cp.add(logArea, BorderLayout.CENTER);
        cp.add(logProgress, BorderLayout.SOUTH);

        setContentPane(cp);

        this.task = new RunnableTask(task);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        setSize(200, 100);
    }

    public void startTask() {
        IExecutor executor = ServiceFactory.getInstance().getExecutor();

        executor.execute(task);
    }

    private class RunnableTask implements Runnable {
        private final ITask task;

        public RunnableTask(ITask task) {
            this.task = task;
        }

        public void run() {
            IProgressTracker tracker = new ProgressTracker();

            try {
                task.doTask(tracker);
            } catch (RojacException e) {
                log.error("Error in task", e);
                tracker.postException(e);
            } catch (RuntimeException e) {
                log.error("Unexpected exception in task", e);
                tracker.postException(e);
            }

            if (tracker.isSuccess()) {
                // TODO: check autoclose option
            }

            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        }
    }

    private class ProgressTracker implements IProgressTracker {
        private boolean gotException = false;

        public void addLodMessage(final String message) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    logArea.append(message);
                    logArea.append("\n");
                }
            });
        }

        public void setProgress(final int amount, final int total) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    logProgress.setMinimum(0);
                    logProgress.setMaximum(total);
                    logProgress.setValue(amount);
                }
            });
        }

        public void postException(final Throwable t) {
            gotException = true;
            log.error("Got exception", t);
        }

        public boolean isSuccess() {
            return !gotException;
        }
    }
}
