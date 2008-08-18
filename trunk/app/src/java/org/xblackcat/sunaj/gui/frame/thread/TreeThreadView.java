package org.xblackcat.sunaj.gui.frame.thread;

import org.xblackcat.sunaj.gui.IMessageView;
import org.xblackcat.sunaj.gui.frame.message.AMessageView;

import javax.swing.*;
import java.awt.*;

/**
 * Date: 22 бер 2008
 *
 * @author xBlackCat
 */

public class TreeThreadView extends AMessageView {
    private final JTree messages = new JTree();
    private final JLabel forumName = new JLabel();

    private final AThreadTreeModel model = new SimpleThreadTreeModel();

    public TreeThreadView() {
        super(new BorderLayout());

        initializeLayout();
    }

    private void initializeLayout() {
        add(forumName, BorderLayout.NORTH);

        JScrollPane sp = new JScrollPane(messages);
        add(sp, BorderLayout.CENTER);
    }

    public void viewItem(int forumId) {
        fireMessageGotFocus(forumId);
    }

    public void updateItem(int messageId) {
    }
}
