package org.xblackcat.rojac.gui.popup;

import javax.swing.*;

/**
 * Date: 19 ρεπο 2009
 *
 * @author xBlackCat
 */

interface IPopupBuilder {
    PopupTypeEnum getType();

    JPopupMenu buildMenu(Object ...parameters);
}
