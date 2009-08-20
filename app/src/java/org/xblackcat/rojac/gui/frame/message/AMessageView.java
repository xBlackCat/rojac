package org.xblackcat.rojac.gui.frame.message;

import org.xblackcat.rojac.gui.IActionListener;
import org.xblackcat.rojac.gui.IMessageView;
import org.xblackcat.rojac.gui.IRootPane;

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
    protected final IRootPane mainFrame;

    protected AMessageView(LayoutManager layout, boolean isDoubleBuffered, IRootPane mainFrame) {
        super(layout, isDoubleBuffered);
        this.mainFrame = mainFrame;
    }

    protected AMessageView(LayoutManager layout, IRootPane mainFrame) {
        super(layout);
        this.mainFrame = mainFrame;
    }

    protected AMessageView(boolean isDoubleBuffered, IRootPane mainFrame) {
        super(isDoubleBuffered);
        this.mainFrame = mainFrame;
    }

    protected AMessageView(IRootPane mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void applySettings() {
    }

    public void updateSettings() {
    }

    public JComponent getComponent() {
        return this;
    }

    public void updateData(int... ids) {
        // TODO: revise!!
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
