package org.xblackcat.rojac.gui.popup;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.i18n.Messages;

import javax.swing.*;
import java.awt.*;

/**
 * @author xBlackCat
 */

public class LinkPopupBuilder implements IPopupBuilder {
    private static final Log log = LogFactory.getLog(LinkPopupBuilder.class);

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

        final String url = (String) parameters[0];

        JPopupMenu menu = new JPopupMenu();

        JMenuItem headerItem = new JMenuItem("URL: " + url);
        headerItem.setEnabled(false);
        menu.add(headerItem);

        menu.addSeparator();

        final Desktop desktop = Desktop.getDesktop();

        if (url != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            JMenuItem openInBrowserItem = new JMenuItem(Messages.POPUP_LINK_OPEN_IN_BROWSER.get());
            openInBrowserItem.addActionListener(new OpenUrlAction(url));

            menu.add(openInBrowserItem);
        }

        JMenuItem copyToClipboard = new JMenuItem(Messages.POPUP_LINK_COPY_TO_CLIPBOARD.get());
        copyToClipboard.addActionListener(new CopyUrlAction(url));

        menu.add(copyToClipboard);

        return menu;
    }
}