package org.xblackcat.rojac.gui.popup;

import org.apache.commons.lang.ArrayUtils;
import org.xblackcat.rojac.gui.IRootPane;

import javax.swing.*;

/**
 * Date: 19 ρεπο 2009
 *
 * @author xBlackCat
 */

public class TreeThreadViewPopupBuilder extends AMessagePopupBulder {
    @Override
    public JPopupMenu buildMenu(Object... parameters) {
        if (ArrayUtils.isEmpty(parameters) || parameters.length != 2) {
            throw new IllegalArgumentException("Invalid parameters amount.");
        }

        final int messageId = ((Integer)parameters[0]).intValue();
        final IRootPane mainFrame = (IRootPane) parameters[1];

        return buildMenu(messageId, mainFrame);
    }

}