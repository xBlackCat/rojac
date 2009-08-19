package org.xblackcat.rojac.gui.popup;

import javax.swing.*;

/**
 * Date: 19 ρεπο 2009
 *
 * @author xBlackCat
 */

public class LinkDownloadablePopupBuilder implements IPopupBuilder {

    /**
     * Builds popup menu for downloadable link (link that points on a file like archive or document). Available actions in the menu are: <ul>
     * <li>"Download to..." - pass a link to external program to download</li>
     * <li>"Open in browser" - open the link in default browser </li>
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