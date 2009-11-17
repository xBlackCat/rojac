package org.xblackcat.rojac.gui.view;

import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.service.commands.AffectedIds;

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

    public void updateData(AffectedIds changedData) {
    }
}
