package org.xblackcat.rojac.gui.view.message;

import org.xblackcat.rojac.gui.IActionListener;
import org.xblackcat.rojac.gui.IItemView;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.view.AView;
import org.xblackcat.rojac.service.janus.commands.AffectedMessage;

/**
 * @author xBlackCat
 */

public abstract class AItemView extends AView implements IItemView {
    protected AItemView(IRootPane mainFrame) {
        super(mainFrame);
    }

    public void addActionListener(IActionListener l) {
        listenerList.add(IActionListener.class, l);
    }

    public void removeActionListener(IActionListener l) {
        listenerList.remove(IActionListener.class, l);
    }

    protected void fireMessageGotFocus(AffectedMessage messageId) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == IActionListener.class) {
                ((IActionListener) listeners[i + 1]).itemGotFocus(messageId);
            }
        }
    }

    protected void fireMessageLostFocus(AffectedMessage messageId) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == IActionListener.class) {
                ((IActionListener) listeners[i + 1]).itemLostFocus(messageId);
            }
        }
    }

    protected void fireItemUpdated(AffectedMessage messageId) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == IActionListener.class) {
                ((IActionListener) listeners[i + 1]).itemUpdated(messageId);
            }
        }
    }
}
