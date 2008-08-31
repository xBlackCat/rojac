package org.xblackcat.rojac.gui.frame.mtree;

import org.xblackcat.rojac.data.Message;
import org.xblackcat.utils.ResourceUtils;

/**
 * Date: 15 груд 2007
 *
 * @author xBlackCat
 */
final class FavoriteCellRenderer extends MessageCellRenderer {

    protected boolean setupComponent(MessageItem item, boolean selected) {
        Message m = item.getMessage();
        if (m != null) {
            if (m.getFavoriteIndex() != null) {
                setIcon(ResourceUtils.loadImageIcon("/images/favorite/" + m.getFavoriteIndex() + ".png"));
            } else {
                setIcon(null);
            }
        }

        return false;
    }
}