package org.xblackcat.rojac.service.progress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

/**
 * @author xBlackCat
 */

public class ProgressControl implements IProgressControl {
    private static final Log log = LogFactory.getLog(ProgressControl.class);

    private final EventListenerList listenerList = new EventListenerList();

    @Override
    public void fireJobStart() {
        fireProgressChanged(ProgressState.Start, null, null);
    }

    @Override
    public void fireJobStart(String logMessage) {
        fireProgressChanged(ProgressState.Start, null, logMessage);
    }

    @Override
    public void fireJobProgressChanged(float progress) {
        fireProgressChanged(ProgressState.Work, progress, null);
    }

    @Override
    public void fireJobProgressChanged(float progress, String logMessage) {
        fireProgressChanged(ProgressState.Work, progress, logMessage);
    }

    @Override
    public void fireJobStop() {
        fireProgressChanged(ProgressState.Stop, null, null);
    }

    @Override
    public void fireJobStop(String logMessage) {
        fireProgressChanged(ProgressState.Stop, null, logMessage);
    }

    @Override
    public void fireIdle(String logMessage) {
        fireProgressChanged(ProgressState.Idle, null, logMessage);
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
