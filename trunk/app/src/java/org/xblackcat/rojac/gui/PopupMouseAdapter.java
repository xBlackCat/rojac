package org.xblackcat.rojac.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author xBlackCat
 */

public abstract class PopupMouseAdapter extends MouseAdapter {
    @Override
    public final void mouseClicked(MouseEvent e) {
        checkActions(e);
    }

    @Override
    public final void mousePressed(MouseEvent e) {
        checkActions(e);
    }

    @Override
    public final void mouseReleased(MouseEvent e) {
        checkActions(e);
    }

    private void checkActions(MouseEvent e) {
        if (e.isPopupTrigger()) {
            triggerPopup(e);
        } else if (e.getButton() == MouseEvent.BUTTON1 && e.getID() == MouseEvent.MOUSE_CLICKED) {
            if (e.getClickCount() == 2) {
                triggerDoubleClick(e);
            } else {
                triggerClick(e);
            }
        }
    }

    protected void triggerClick(MouseEvent e) {}

    protected abstract void triggerDoubleClick(MouseEvent e);

    protected abstract void triggerPopup(MouseEvent e);
}
