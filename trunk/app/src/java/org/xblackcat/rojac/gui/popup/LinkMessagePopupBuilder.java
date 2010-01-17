package org.xblackcat.rojac.gui.popup;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.IRootPane;

import javax.swing.*;

/**
 * @author xBlackCat
 */

public class LinkMessagePopupBuilder implements IPopupBuilder {
    private static final Log log = LogFactory.getLog(LinkMessagePopupBuilder.class);

    /**
     * Builds popup menu for RSDN message link. Available actions in the menu are: <ul> <li>"Goto" - open the message in
     * the view window</li> <li>"Open in new tab" - open the message in new tab of the main frame</li> <li>"Open thread
     * in new tab" - open whole thread in browser</li> <li>"Open in browser" - open only the message in browser </li>
     * </ul>
     *
     * @param parameters
     *
     * @return
     */
    @Override
    public JPopupMenu buildMenu(Object... parameters) {
        if (ArrayUtils.isEmpty(parameters) || parameters.length < 4) {
            throw new IllegalArgumentException("Invalid amount of parameters for building LinkPopUp menu");
        }

        final int messageId = (Integer) parameters[0];
        final String url = (String) parameters[1];
        final String text = (String) parameters[2];
        final IRootPane mainFrame = (IRootPane) parameters[3];

        final JPopupMenu menu = new JPopupMenu("#" + messageId);

        JMenuItem item = new JMenuItem("#" + messageId);
        item.setEnabled(false);

        menu.add(item);

        menu.addSeparator();
        menu.add(MenuHelper.openMessage(messageId, mainFrame));
        MenuHelper.addOpenLink(menu, url);

        menu.addSeparator();
        menu.add(MenuHelper.copyLinkSubmenu(messageId));

        return menu;
    }
}
