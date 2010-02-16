package org.xblackcat.rojac.service.progress;

import org.xblackcat.rojac.i18n.Messages;

/**
 * Interface to describe actions of progress tracker to show any continues operations in the tray or progress dialog.
 *
 * @author xBlackCat
 */

public interface IProgressController {
    /**
     * Notify listeners about job has been started.
     */
    void fireJobStart();

    /**
     * Notify listeners about job has been started and provide a message for logging.
     *
     * @param message
     * @param arguments
     */
    void fireJobStart(Messages message, Object... arguments);

    /**
     * Notify listeners about changing jobs progress (completeness) state.
     *
     * @param progress new value of progress.
     */
    void fireJobProgressChanged(float progress);

    /**
     * Notify listeners about changing jobs progress (completeness) state and provide a message for logging.
     *
     * @param progress   new value of progress.
     * @param message
     * @param arguments
     */
    void fireJobProgressChanged(float progress, Messages message, Object... arguments);

    /**
     * Notify listeners about job has been stopped.
     */
    void fireJobStop();

    /**
     * Notify listeners about job has been stopped and provide a message for logging.
     *
     * @param message
     * @param arguments
     */
    void fireJobStop(Messages message, Object... arguments);

    /**
     * Notify listeners about no more tasks is processed.
     */
    void fireIdle();

    /**
     * Notify listeners about no more tasks is processed and provide a message for logging.
     *
     * @param message
     * @param arguments
     */
    void fireIdle(Messages message, Object... arguments);

    /**
     * Registers a new progress listener.
     *
     * @param l listener to register.
     */
    void addProgressListener(IProgressListener l);

    /**
     * Removes a progress listener.
     *
     * @param l listener to remove.
     */
    void removeProgressListener(IProgressListener l);
}
