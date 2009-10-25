package org.xblackcat.rojac.gui.view.message;

import org.xblackcat.rojac.gui.IActionListener;
import org.xblackcat.rojac.gui.IMessageView;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.view.AView;

import javax.swing.*;

/**
 * @author xBlackCat
 */

public abstract class AMessageView extends AView implements IMessageView {
    protected AMessageView(IRootPane mainFrame) {
        super(mainFrame);
    }

    public JComponent getComponent() {
        return this;
    }

    public void addActionListener(IActionListener l) {
        listenerList.add(IActionListener.class, l);
    }

    public void removeActionListener(IActionListener l) {
        listenerList.remove(IActionListener.class, l);
    }

    protected void fireMessageGotFocus(int messageId) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == IActionListener.class) {
                ((IActionListener) listeners[i + 1]).itemGotFocus(messageId);
            }
        }
    }

    protected void fireMessageLostFocus(int messageId) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == IActionListener.class) {
                ((IActionListener) listeners[i + 1]).itemLostFocus(messageId);
            }
        }
    }

    protected void fireMessageUpdated(int messageId) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == IActionListener.class) {
                ((IActionListener) listeners[i + 1]).itemUpdated(messageId);
            }
        }
    }
}
