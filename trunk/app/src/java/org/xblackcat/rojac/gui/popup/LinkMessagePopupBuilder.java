package org.xblackcat.rojac.gui.popup;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.IRootPane;

import javax.swing.*;

/**
 * Date: 19 ρεπο 2009
 *
 * @author xBlackCat
 */

public class LinkMessagePopupBuilder extends AMessagePopupBulder {
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

        final int messageId = ((Integer) parameters[1]).intValue();
        final IRootPane mainFrame = (IRootPane) parameters[3];

        return buildMenu(messageId, mainFrame);
    }
}
