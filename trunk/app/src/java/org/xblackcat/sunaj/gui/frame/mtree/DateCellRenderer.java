package org.xblackcat.sunaj.gui.frame.mtree;

/**
 * Date: 15 груд 2007
 *
 * @author xBlackCat
 */
final class DateCellRenderer extends MessageCellRenderer {

    protected boolean setupComponent(MessageItem item, boolean selected) {
        setText(String.valueOf(item.getMessageId()));

        return false;
    }
}