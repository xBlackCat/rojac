package org.xblackcat.rojac.gui.frame.mtree;

/**
 * @author xBlackCat
 */
final class AuthorCellRenderer extends MessageCellRenderer {

    protected boolean setupComponent(MessageItem item, boolean selected) {
        setText("Nick");

        return false;
    }
}