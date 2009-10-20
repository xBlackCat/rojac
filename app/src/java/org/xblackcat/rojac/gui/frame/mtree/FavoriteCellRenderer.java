package org.xblackcat.rojac.gui.frame.mtree;

import org.xblackcat.rojac.data.Message;
import org.xblackcat.rojac.service.RojacHelper;

/**
 * @author xBlackCat
 */
final class FavoriteCellRenderer extends MessageCellRenderer {

    protected boolean setupComponent(MessageItem item, boolean selected) {
        Message m = item.getMessage();
        if (m != null) {
            if (m.getFavoriteIndex() != null) {
                setIcon(RojacHelper.loadIcon("favorite/" + m.getFavoriteIndex() + ".png"));
            } else {
                setIcon(null);
            }
        }

        return false;
    }
}