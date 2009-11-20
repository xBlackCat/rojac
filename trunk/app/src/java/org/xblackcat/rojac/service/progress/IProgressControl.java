package org.xblackcat.rojac.service.progress;

/**
 * Interface to describe actions of progress tracker to show any continues operations in the tray or progress dialog.
 *
 * @author xBlackCat
 */

public interface IProgressControl {
    /**
     * Notify listeners about job has been started.
     */
    void fireJobStart();

    /**
     * Notify listeners about job has been started and provide a message for logging.
     *
     * @param logMessage message to log.
     */
    void fireJobStart(String logMessage);

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
     * @param logMessage message to log.
     */
    void fireJobProgressChanged(float progress, String logMessage);

    /**
     * Notify listeners about job has been stopped.
     */
    void fireJobStop();

    /**
     * Notify listeners about job has been stopped and provide a message for logging.
     *
     * @param logMessage message to log.
     */
    void fireJobStop(String logMessage);

    /**
     * Notify listeners about no more tasks is processed.
     */
    void fireIdle();

    /**
     * Notify listeners about no more tasks is processed and provide a message for logging.
     *
     * @param logMessage message to log.
     */
    void fireIdle(String logMessage);

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
