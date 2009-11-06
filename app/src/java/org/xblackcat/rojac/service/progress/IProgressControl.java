package org.xblackcat.rojac.service.progress;

/**
 * Interface to describe actions of progress tracker to show any continues operations in the tray or progress dialog.
 *
 * @author xBlackCat
 */

public interface IProgressControl {
    void fireJobStart();

    void fireJobProgressChanged(float progress);

    void fireJobStop();

    void addProgressListener(IProgressListener listener);

    void removeProgressListener(IProgressListener listener);
}
