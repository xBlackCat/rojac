package org.xblackcat.rojac.gui.popup;

import org.apache.commons.lang.ArrayUtils;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.util.LinkUtils;

import javax.swing.*;

/** @author xBlackCat */

class LinkMessagePopupBuilder implements IPopupBuilder {
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

        final Integer messageId = (Integer) parameters[0];
        final String url = (String) parameters[1];
        final String text = (String) parameters[2];
        final IRootPane mainFrame = (IRootPane) parameters[3];

        return builtInternal(messageId, url, text, mainFrame);
    }

    /**
     * Real menu builder.
     *
     * @param messageId message id to build 'copy link' and 'open link' actions.
     * @param url
     * @param text
     * @param mainFrame
     *
     * @return
     */
    private JPopupMenu builtInternal(Integer messageId, String url, String text, IRootPane mainFrame) {
        final JPopupMenu menu = new JPopupMenu();

        String headerText = messageId == null ? text : "#" + messageId;
        JMenuItem headerItem = new JMenuItem(headerText);
        headerItem.setEnabled(false);
        menu.add(headerItem);

        menu.addSeparator();
        if (messageId != null) {
            menu.add(MenuHelper.openMessage(messageId, mainFrame));
            menu.add(MenuHelper.openMessageInTab(messageId, mainFrame));
            menu.addSeparator();
            MenuHelper.addOpenLink(menu, Messages.POPUP_LINK_OPEN_MESSAGE_IN_BROWSER, url);
            MenuHelper.addOpenLink(menu, Messages.POPUP_LINK_OPEN_THREAD_IN_BROWSER, LinkUtils.buildThreadLink(messageId));

        } else {
            MenuHelper.addOpenLink(menu, Messages.POPUP_LINK_OPEN_IN_BROWSER, url);
        }

        menu.add(MenuHelper.copyToClipboard(url));
        if (messageId != null) {
            menu.add(MenuHelper.copyLinkSubmenu(messageId));
        }

        return menu;
    }
}
