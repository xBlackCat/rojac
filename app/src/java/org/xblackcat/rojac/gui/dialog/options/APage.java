package org.xblackcat.rojac.gui.dialog.options;

import org.xblackcat.rojac.i18n.Messages;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author xBlackCat
 */
abstract class APage extends JPanel {
    protected APage() {
        super(new BorderLayout(5, 10));

        setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    protected abstract void applySettings(Window mainFrame);

    public abstract Messages getTitle();

    public abstract void placeFocus();
}
