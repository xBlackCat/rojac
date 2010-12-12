package org.xblackcat.rojac.gui.view;

import org.xblackcat.rojac.gui.IActionListener;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.IItemView;

/**
 * @author xBlackCat
 */

public abstract class AnItemView extends AView implements IItemView {
    protected AnItemView(ViewId id, IAppControl appControl) {
        super(id, appControl);
    }

    public void addActionListener(IActionListener l) {
        listenerList.add(IActionListener.class, l);
    }

    public void removeActionListener(IActionListener l) {
        listenerList.remove(IActionListener.class, l);
    }

    protected void fireMessageGotFocus(int forumId, Integer messageId) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == IActionListener.class) {
                ((IActionListener) listeners[i + 1]).itemGotFocus(forumId, messageId);
            }
        }
    }

    protected void fireMessageLostFocus(int forumId, Integer messageId) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == IActionListener.class) {
                ((IActionListener) listeners[i + 1]).itemLostFocus(forumId, messageId);
            }
        }
    }

    protected void fireItemUpdated(Integer forumId, Integer messageId) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == IActionListener.class) {
                ((IActionListener) listeners[i + 1]).itemUpdated(forumId, messageId);
            }
        }
    }
}
