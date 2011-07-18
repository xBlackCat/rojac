package org.xblackcat.rojac.gui.view.navigation;

import org.jdesktop.swingx.JXTreeTable;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.IViewLayout;
import org.xblackcat.rojac.gui.NoViewLayout;
import org.xblackcat.rojac.gui.view.AView;
import org.xblackcat.rojac.service.datahandler.IPacket;

import javax.swing.*;
import java.awt.*;

/**
 * @author xBlackCat Date: 15.07.11
 */
public class NavigationView extends AView {
    private NavModel model;
    private JXTreeTable viewTable;

    public NavigationView(IAppControl appControl) {
        super(appControl);

        model = new NavModel();
        viewTable = new JXTreeTable(model);
        JScrollPane container = new JScrollPane(viewTable);
        container.setColumnHeaderView(null);

        add(container, BorderLayout.CENTER);
    }

    @Override
    public String getTabTitle() {
        return null;
    }

    @Override
    public IViewLayout storeLayout() {
        return new NoViewLayout();
    }

    @Override
    public void setupLayout(IViewLayout o) {
    }

    @Override
    public Icon getTabTitleIcon() {
        return null;
    }

    @Override
    public JPopupMenu getTabTitleMenu() {
        return null;
    }

    @Override
    public void processPacket(IPacket packet) {
    }
}
