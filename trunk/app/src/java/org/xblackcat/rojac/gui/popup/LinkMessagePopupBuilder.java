package org.xblackcat.rojac.gui.popup;

import javax.swing.*;

/**
 * Date: 19 ρεπο 2009
 *
 * @author xBlackCat
 */

public class LinkMessagePopupBuilder implements IPopupBuilder {
    @Override
    public PopupTypeEnum getType() {
        return PopupTypeEnum.LinkMessagePopup;
    }

    /**
     * Builds popup menu for RSDN message link. Available actions in the menu are: <ul>
     * <li>"Goto" - open the message in the view window</li>
     * <li>"Open in new tab" - open the message in new tab of the main frame</li>
     * <li>"Open thread in new tab" - open whole thread in browser</li>
     * <li>"Open in browser" - open only the message in browser </li>
     * </ul>
     * @param parameters
     * @return
     */
    @Override
    public JPopupMenu buildMenu(Object... parameters) {
        JPopupMenu menu = new JPopupMenu();

        return menu;
    }
}
