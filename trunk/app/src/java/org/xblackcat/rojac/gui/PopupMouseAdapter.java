package org.xblackcat.rojac.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author xBlackCat
 */

public abstract class PopupMouseAdapter extends MouseAdapter {
    @Override
    public final void mouseClicked(MouseEvent e) {
        checkMenu(e);
    }

    @Override
    public final void mousePressed(MouseEvent e) {
        checkMenu(e);
    }

    @Override
    public final void mouseReleased(MouseEvent e) {
        checkMenu(e);
    }

    private void checkMenu(MouseEvent e) {
        if (e.isPopupTrigger()) {
            triggerPopup(e);
        } else if (e.getClickCount() > 1 && e.getButton() == MouseEvent.BUTTON1) {
            triggerDoubleClick(e);
        }
    }

    protected abstract void triggerDoubleClick(MouseEvent e);

    protected abstract void triggerPopup(MouseEvent e);
}
