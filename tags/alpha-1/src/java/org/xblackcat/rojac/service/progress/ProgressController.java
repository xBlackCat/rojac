package org.xblackcat.rojac.service.progress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.i18n.Messages;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

/**
 * @author xBlackCat
 */

public class ProgressController implements IProgressController {
    private static final Log log = LogFactory.getLog(ProgressController.class);

    private final EventListenerList listenerList = new EventListenerList();

    @Override
    public void fireJobStart() {
        fireProgressChanged(ProgressState.Start, null, null);
    }

    @Override
    public void fireJobStart(Messages message, Object... arguments) {
        fireProgressChanged(ProgressState.Start, null, message.get(arguments));
    }

    @Override
    public void fireJobProgressChanged(float progress) {
        fireProgressChanged(ProgressState.Work, progress, null);
    }

    @Override
    public void fireJobProgressChanged(float progress, Messages message, Object... arguments) {
        fireProgressChanged(ProgressState.Work, progress, message.get(arguments));
    }

    @Override
    public void fireJobStop() {
        fireProgressChanged(ProgressState.Stop, null, null);
    }

    @Override
    public void fireJobStop(Messages message, Object... arguments) {
        fireProgressChanged(ProgressState.Stop, null, message.get(arguments));
    }

    @Override
    public void fireIdle(Messages message, Object... arguments) {
        fireProgressChanged(ProgressState.Idle, null, message.get(arguments));
    }

    @Override
    public void fireIdle() {
        fireProgressChanged(ProgressState.Idle, null, null);
    }

    @Override
    public void addProgressListener(IProgressListener listener) {
        listenerList.add(IProgressListener.class, listener);
    }

    @Override
    public void removeProgressListener(IProgressListener listener) {
        listenerList.remove(IProgressListener.class, listener);
    }

    private void fireProgressChanged(ProgressState state, Float percentage, String logMessage) {
        final ProgressChangeEvent event = new ProgressChangeEvent(this, state, percentage, logMessage);

        if (EventQueue.isDispatchThread()) {
            processEvent(event);
        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        processEvent(event);
                    }
                });
            } catch (InterruptedException e) {
                log.error("Process progress event is interrupted.", e);
            } catch (InvocationTargetException e) {
                log.error("Can not process change event", e);
            }
        }
    }

    /**
     * Process event in EventDispatching thread.
     *
     * @param e event to process.
     */
    private void processEvent(ProgressChangeEvent e) {
        for (IProgressListener l : listenerList.getListeners(IProgressListener.class)) {
            l.progressChanged(e);
        }
    }
}
