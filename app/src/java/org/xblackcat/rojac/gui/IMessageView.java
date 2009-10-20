package org.xblackcat.rojac.gui;

/**
 * @author xBlackCat
 */

public interface IMessageView extends IView {
    void viewItem(int messageId, boolean isNewMessage);

    void updateItem(int messageId);

    void addActionListener(IActionListener l);

    void removeActionListener(IActionListener l);
}
