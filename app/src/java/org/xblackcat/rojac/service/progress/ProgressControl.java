package org.xblackcat.rojac.service.progress;

import javax.swing.event.EventListenerList;

/**
 * @author xBlackCat
 */

public class ProgressControl implements IProgressControl {
    private final EventListenerList listenerList = new EventListenerList();

    @Override
    public void fireJobStart() {
    }

    @Override
    public void fireJobProgressChanged(float progress) {
    }

    @Override
    public void fireJobStop() {
    }

    @Override
    public void addProgressListener(IProgressListener listener) {
        listenerList.add(IProgressListener.class, listener);
    }

    @Override
    public void removeProgressListener(IProgressListener listener) {
        listenerList.remove(IProgressListener.class, listener);
    }

}
