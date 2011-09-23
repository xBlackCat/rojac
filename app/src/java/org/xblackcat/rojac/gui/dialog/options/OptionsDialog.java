package org.xblackcat.rojac.gui.dialog.options;

import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.gui.component.ACancelAction;
import org.xblackcat.rojac.gui.component.AnOkAction;
import org.xblackcat.rojac.gui.dialog.LoginDialog;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Shows a dialog to configure the application.
 *
 * @author xBlackCat
 */

public class OptionsDialog extends JDialog {
    private final APage[] pages = new APage[]{
            new PropertiesPage(),
            new ShortCutManagerPage() ,
            new DBSettingsPage()
    };

    public OptionsDialog(Window mainFrame) throws RojacException {
        super(mainFrame, DEFAULT_MODALITY_TYPE);

        setTitle(Message.Dialog_Options_Title.get());

        initializeLayout();

        pack();
        setMinimumSize(getSize());
    }

    private void initializeLayout() {
        JPanel cp = new JPanel(new BorderLayout(5, 10));
        cp.setBorder(new EmptyBorder(10, 10, 10, 10));

        final JTabbedPane centerComp = new JTabbedPane();

        for (APage p : pages) {
            centerComp.addTab(p.getTitle().get(), p);
            centerComp.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    int selected = centerComp.getSelectedIndex();

                    if (selected >= 0) {
                        pages[selected].placeFocus();
                    }
                }
            });
        }

        cp.add(centerComp, BorderLayout.CENTER);

        cp.add(WindowsUtils.createButtonsBar(
                this,
                Message.Button_Ok,
                new AButtonAction(Message.Button_ChangePassword) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        LoginDialog ld = new LoginDialog(OptionsDialog.this);
                        ld.showLoginDialog();
                    }
                },
                new AnOkAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        applySettings();
                        setVisible(false);
                        dispose();
                    }
                },
                new AButtonAction(Message.Button_Apply) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        applySettings();
                    }
                },
                new ACancelAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setVisible(false);
                        dispose();
                    }
                }
        ), BorderLayout.SOUTH);

        setContentPane(cp);

    }

    private void applySettings() {
        for (APage p : pages) {
            p.applySettings(getOwner());
        }

        RojacUtils.storeSettings();
    }
}
