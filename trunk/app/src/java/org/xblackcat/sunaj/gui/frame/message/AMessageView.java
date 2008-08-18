package org.xblackcat.sunaj.gui.frame.message;

import org.xblackcat.sunaj.gui.IActionListener;
import org.xblackcat.sunaj.gui.IMessageView;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;

/**
 * Date: 17 лип 2008
 *
 * @author xBlackCat
 */

public abstract class AMessageView extends JPanel implements IMessageView {
    private EventListenerList listenerList = new EventListenerList();

    protected AMessageView(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    protected AMessageView(LayoutManager layout) {
        super(layout);
    }

    protected AMessageView(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    protected AMessageView() {
    }

    public void applySettings() {
    }

    public void updateSettings() {
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
