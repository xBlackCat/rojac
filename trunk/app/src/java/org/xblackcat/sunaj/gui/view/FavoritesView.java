package org.xblackcat.sunaj.gui.view;

import javax.swing.*;
import java.awt.*;

/**
 * Date: 15 лип 2008
 *
 * @author xBlackCat
 */

public class FavoritesView extends JPanel implements IView {
    public FavoritesView() {
        super(new BorderLayout(2, 2));

        add(new JScrollPane(new JTable(10, 2)));
    }

    public void applySettings() {
        // TODO: implement
    }

    public void updateSettings() {
        // TODO: implement
    }

    public JComponent getComponent() {
        return this;
    }
}
