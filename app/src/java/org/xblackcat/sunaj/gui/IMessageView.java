package org.xblackcat.sunaj.gui;

/**
 * Date: 29 лют 2008
 *
 * @author xBlackCat
 */

public interface IMessageView extends IView {
    void viewItem(int messageId);

    void updateItem(int messageId);

    void addActionListener(IActionListener l);

    void removeActionListener(IActionListener l);
}
