package org.xblackcat.rojac.gui.view;

import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.IView;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.gui.keyboard.ShortCut;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author xBlackCat
 */

public abstract class AView extends JPanel implements IView {
    protected final IStorage storage = ServiceFactory.getInstance().getStorage();

    protected final IRootPane mainFrame;

    public AView(IRootPane rootPane) {
        super(new BorderLayout());
        this.mainFrame = rootPane;
    }

    public void applySettings() {
    }

    public void updateSettings() {
    }

    public final JComponent getComponent() {
        return this;
    }

    protected final JButton registerImageButton(String buttonName, AButtonAction action) {
        ShortCut sc = action.getShortCut();
        if (sc != null) {
            getInputMap(sc.getCondition()).put(sc.getKeyStroke(), sc.name());
            getActionMap().put(sc.name(), action);
        }

        return WindowsUtils.setupImageButton(buttonName, action);
    }
}
