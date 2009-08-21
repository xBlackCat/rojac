package org.xblackcat.rojac.gui;

/**
 * Date: 29 лют 2008
 *
 * @author xBlackCat
 */

public interface IMessageView extends IView {
    void viewItem(int messageId, boolean isNewMessage);

    void updateItem(int messageId);

    void addActionListener(IActionListener l);

    void removeActionListener(IActionListener l);
}
