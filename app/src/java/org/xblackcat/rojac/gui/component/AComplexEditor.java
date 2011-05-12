package org.xblackcat.rojac.gui.component;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;

/**
 * @author xBlackCat
 */

public abstract class AComplexEditor<T> extends JLightPanel {
    public static final String ACTION_DONE = "edit_done";
    public static final String ACTION_CANCEL = "edit_cancel";

    // Flag to ensure that infinite loops do not occur with ActionEvents.
    private boolean firingActionEvent = false;

    protected AComplexEditor() {
    }

    protected AComplexEditor(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    protected AComplexEditor(LayoutManager layout) {
        super(layout);
    }

    protected AComplexEditor(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    public abstract void setValue(T v);

    public abstract T getValue();

    /**
     * Adds an <code>ActionListener</code>.
     * <p/>
     * The <code>ActionListener</code> will receive an <code>ActionEvent</code> when a selection has been made. If the
     * combo box is editable, then an <code>ActionEvent</code> will be fired when editing has stopped.
     *
     * @param l the <code>ActionListener</code> that is to be notified
     *
     * @see #setSelectedItem
     */
    public void addActionListener(ActionListener l) {
        listenerList.add(ActionListener.class, l);
    }

    /**
     * Removes an <code>ActionListener</code>.
     *
     * @param l the <code>ActionListener</code> to remove
     */
    public void removeActionListener(ActionListener l) {
        listenerList.remove(ActionListener.class, l);
    }


    /**
     * Returns an array of all the <code>ActionListener</code>s added to this JTextField with addActionListener().
     *
     * @return all of the <code>ActionListener</code>s added or an empty array if no listeners have been added
     *
     * @since 1.4
     */
    public synchronized ActionListener[] getActionListeners() {
        return (ActionListener[]) listenerList.getListeners(
                ActionListener.class);
    }

    /**
     * Notifies all listeners that have registered interest for notification on this event type.
     *
     * @see javax.swing.event.EventListenerList
     * @param command
     */
    protected void fireActionEvent(String command) {
        if (!firingActionEvent) {
            // Set flag to ensure that an infinite loop is not created
            firingActionEvent = true;
            ActionEvent e = null;
            // Guaranteed to return a non-null array
            Object[] listeners = listenerList.getListenerList();
            long mostRecentEventTime = EventQueue.getMostRecentEventTime();
            int modifiers = 0;
            AWTEvent currentEvent = EventQueue.getCurrentEvent();
            if (currentEvent instanceof InputEvent) {
                modifiers = ((InputEvent) currentEvent).getModifiers();
            } else if (currentEvent instanceof ActionEvent) {
                modifiers = ((ActionEvent) currentEvent).getModifiers();
            }
            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == ActionListener.class) {
                    // Lazily create the event:
                    if (e == null)
                        e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                                command,
                                mostRecentEventTime, modifiers);
                    ((ActionListener) listeners[i + 1]).actionPerformed(e);
                }
            }
            firingActionEvent = false;
        }
    }

    protected void fireEditDone() {
        fireActionEvent(ACTION_DONE);
    }

    protected void fireEditCancel() {
        fireActionEvent(ACTION_CANCEL);
    }
}
