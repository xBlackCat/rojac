package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.IViewLayout;
import org.xblackcat.rojac.gui.view.AView;
import org.xblackcat.rojac.service.datahandler.IPacket;

import javax.swing.*;

/**
 * @author xBlackCat Date: 15.07.11
 */
public class NavigationView extends AView {

    public NavigationView(IAppControl appControl) {
        super(appControl);
    }

    @Override
    public String getTabTitle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public IViewLayout storeLayout() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setupLayout(IViewLayout o) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Icon getTabTitleIcon() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public JPopupMenu getTabTitleMenu() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void processPacket(IPacket packet) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
