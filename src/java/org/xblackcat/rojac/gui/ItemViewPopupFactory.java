package org.xblackcat.rojac.gui;

import bibliothek.gui.Dockable;
import bibliothek.gui.dock.DefaultDockable;
import bibliothek.gui.dock.action.DockActionSource;
import bibliothek.gui.dock.action.popup.ActionPopupMenu;
import bibliothek.gui.dock.action.popup.ActionPopupMenuFactory;
import bibliothek.gui.dock.action.popup.ActionPopupMenuListener;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author xBlackCat
 */
class ItemViewPopupFactory implements ActionPopupMenuFactory {
    public ItemViewPopupFactory() {
    }

    @Override
    public ActionPopupMenu createMenu(
            Component owner, final Dockable dockable, DockActionSource actions, Object source
    ) {
        if (!(dockable instanceof DefaultDockable)) {
            return null;
        }

        final Component component = ((DefaultDockable) dockable).getClientComponent();
        if (!(component instanceof IItemView)) {
            return null;
        }

        final JPopupMenu tabTitleMenu = ((IItemView) component).getTabTitleMenu();
        if (tabTitleMenu == null) {
            return null;
        }

        return new ViewPopupMenu(dockable, tabTitleMenu);
    }

    private static class ViewPopupMenu implements ActionPopupMenu {
        /**
         * all the listeners that were added to this menu
         */
        private java.util.List<ActionPopupMenuListener> listeners = new ArrayList<>();
        private final Dockable dockable;
        private final JPopupMenu menu;

        public ViewPopupMenu(Dockable dockable, JPopupMenu menu) {
            this.dockable = dockable;
            this.menu = menu;
            menu.addPopupMenuListener(
                    new PopupMenuListener() {
                        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                            EventQueue.invokeLater(
                                    () -> fireClosed()
                            );
                        }

                        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

                        }

                        public void popupMenuCanceled(PopupMenuEvent e) {

                        }
                    }
            );

        }

        @Override
        public Dockable getDockable() {
            return dockable;
        }

        @Override
        public void show(Component owner, int x, int y) {
            menu.show(owner, x, y);
        }

        @Override
        public void addListener(ActionPopupMenuListener listener) {
            listeners.add(listener);
        }

        @Override
        public void removeListener(ActionPopupMenuListener listener) {
            listeners.remove(listener);
        }

        private void fireClosed() {
            for (ActionPopupMenuListener listener : listeners.toArray(new ActionPopupMenuListener[listeners.size()])) {
                listener.closed(this);
            }
        }
    }
}
