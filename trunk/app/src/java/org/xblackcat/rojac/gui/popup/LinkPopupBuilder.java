package org.xblackcat.rojac.gui.popup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.util.ClipboardUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Date: 19 ρεπο 2009
 *
 * @author xBlackCat
 */

public class LinkPopupBuilder implements IPopupBuilder {
    private static final Log log = LogFactory.getLog(LinkPopupBuilder.class);

    @Override
    public PopupTypeEnum getType() {
        return PopupTypeEnum.LinkDownloadablePopup;
    }

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
        final PopupHelper.LinkParameters p = PopupHelper.getLinkParameters(parameters);

        JPopupMenu menu = new JPopupMenu();

        JMenuItem headerItem = new JMenuItem("URL: " + p.getUrl());
        headerItem.setEnabled(false);
        menu.add(headerItem);

        menu.addSeparator();

        final Desktop desktop = Desktop.getDesktop();

        if (p.getUrl() != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            JMenuItem openInBrowserItem = new JMenuItem(Messages.POPUP_LINK_OPEN_IN_BROWSER.getMessage());
            openInBrowserItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        desktop.browse(new URI(p.getUrl()));
                    } catch (IOException e1) {
                        log.error("Can not open url " + p.getUrl() + " in default browser");
                    } catch (URISyntaxException e1) {
                        log.error("Can not obtain URI of URL: " + p.getUrl());
                    }
                }
            });

            menu.add(openInBrowserItem);
        }

        JMenuItem copyToClipboard = new JMenuItem(Messages.POPUP_LINK_COPY_TO_CLIPBOARD.getMessage());
        copyToClipboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClipboardUtils.copyToClipboard(p.getUrl());
            }
        });

        menu.add(copyToClipboard);

        return menu;
    }
}