package org.xblackcat.rojac.gui.view;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.IItemView;
import org.xblackcat.rojac.gui.IStateListener;

/**
 * @author xBlackCat Date: 15.07.11
 */
public abstract class AnItemView extends AView implements IItemView {
    private final ViewId id;

    protected AnItemView(ViewId id, IAppControl appControl) {
        super(appControl);

        this.id = id;
    }

    public ViewId getId() {
        return id;
    }

    public void addStateChangeListener(IStateListener l) {
        listenerList.add(IStateListener.class, l);
    }

    public void removeStateChangeListener(IStateListener l) {
        listenerList.remove(IStateListener.class, l);
    }

    protected void fireViewStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == IStateListener.class) {
                ((IStateListener) listeners[i + 1]).stateChanged(getId(), getObjectState());
            }
        }
    }

    @Override
    public String toString() {
        return id.getType() + ": " + getTabTitle();
    }
}
