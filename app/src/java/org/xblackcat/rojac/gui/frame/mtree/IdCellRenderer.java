package org.xblackcat.rojac.gui.frame.mtree;

/**
 * @author xBlackCat
 */
final class IdCellRenderer extends MessageCellRenderer {

    protected boolean setupComponent(MessageItem item, boolean selected) {
        setText(String.valueOf(item.getMessageId()));

        return false;
    }
}
