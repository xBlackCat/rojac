package org.xblackcat.rojac.gui.popup;

import org.apache.commons.lang.ArrayUtils;

import javax.swing.*;

/**
 * @author xBlackCat
 */

class LinkPopupBuilder implements IPopupBuilder {
    /**
     * Builds popup menu for downloadable link (link that points on a file like archive or document). Available actions
     * in the menu are: <ul> <li>"Open in browser" - open the link in browser </li> <li>"Copy to clipboard" - copy the
     * link to clipboard</li> </ul>
     *
     * @param parameters
     *
     * @return
     */
    @Override
    public JPopupMenu buildMenu(Object... parameters) {
        if (ArrayUtils.isEmpty(parameters) || parameters.length < 1) {
            throw new IllegalArgumentException("Invalid amount of parameters for building LinkPopUp menu");
        }

        final String url = (String) parameters[1];
        final String text = (String) parameters[2];

        return buildInternal(url, text);
    }

    /**
     * Real menu builder.
     *
     * @param url  
     * @param text
     *
     * @return built pop-up menu.
     */
    private JPopupMenu buildInternal(String url, String text) {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem headerItem = new JMenuItem(text);
        headerItem.setEnabled(false);
        menu.add(headerItem);

        menu.addSeparator();

        MenuHelper.addOpenLink(menu, url);
        menu.add(MenuHelper.copyToClipboard(url));

        return menu;
    }
}
