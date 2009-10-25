package org.xblackcat.rojac.gui.view;

import org.xblackcat.rojac.gui.IRootPane;

import javax.swing.*;

/**
 * @author xBlackCat
 */

public class FavoritesView extends AView {
    public FavoritesView(IRootPane rootPane) {
        super(rootPane);

        add(new JScrollPane(new JTable(10, 2)));
    }

    public JComponent getComponent() {
        return this;
    }

    public void updateData(int... ids) {
    }
}
