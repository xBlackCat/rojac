package org.xblackcat.rojac.gui.dialogs;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.RojacHelper;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author xBlackCat
 */

public class LoginDialog extends JDialog {
    private boolean canceled;

    private JTextField fieldLogin;
    private JPasswordField fieldPassword;
    private JCheckBox fieldSavePassword;

    public LoginDialog(Window mainFrame) {
        super(mainFrame, DEFAULT_MODALITY_TYPE);
        setTitle(Messages.DIALOG_LOGIN_TITLE.get());

        setContentPane(setupContentPane());

        setResizable(false);
        pack();
    }

    private JComponent setupContentPane() {
        JPanel cp = new JPanel(new BorderLayout(10, 10));
        cp.setBorder(new EmptyBorder(10, 10, 10, 10));

        cp.add(WindowsUtils.createButtonsBar(
                this,
                Messages.BUTTON_OK,
                new AButtonAction(Messages.BUTTON_OK) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (StringUtils.isEmpty(fieldLogin.getText())) {
                            JLOptionPane.showMessageDialog(LoginDialog.this, Messages.DIALOG_LOGIN_EMPTY_USERNAME.get());
                            return;
                        }

                        if (ArrayUtils.isEmpty(fieldPassword.getPassword())) {
                            JLOptionPane.showMessageDialog(LoginDialog.this, Messages.DIALOG_LOGIN_EMPTY_PASSWORD.get());
                            return;
                        }

                        canceled = false;
                        setVisible(false);
                    }
                },
                new AButtonAction(Messages.BUTTON_CANCEL) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        canceled = true;
                        setVisible(false);
                    }
                }
        ), BorderLayout.SOUTH);


        JPanel pane = new JPanel(new BorderLayout(5, 5));
        cp.add(pane, BorderLayout.CENTER);

        pane.add(new JLabel(Messages.DIALOG_LOGIN_TEXT.get(), JLabel.CENTER), BorderLayout.NORTH);

        fieldLogin = new JTextField(20);
        fieldPassword = new JPasswordField(20);
        fieldSavePassword = new JCheckBox(Messages.DIALOG_LOGIN_SAVE_PASSWORD.get(), RojacHelper.shouldStorePassword());

        JPanel fields = new JPanel(new GridLayout(0, 1));
        fields.add(fieldLogin);
        fields.add(fieldPassword);

        pane.add(fields, BorderLayout.CENTER);

        JPanel labels = new JPanel(new GridLayout(0, 1));
        labels.add(new JLabel(Messages.DIALOG_LOGIN_USERNAME.get()));
        labels.add(new JLabel(Messages.DIALOG_LOGIN_PASSWORD.get()));

        pane.add(labels, BorderLayout.WEST);

        pane.add(fieldSavePassword, BorderLayout.SOUTH);

        return cp;
    }

    public boolean showLoginDialog() {
        String login = RojacHelper.getUserName();
        String password = RojacHelper.getUserPassword();
        boolean save = RojacHelper.shouldStorePassword();

        fieldLogin.setText(login);
        fieldPassword.setText(password);
        fieldSavePassword.setSelected(save);

        setVisible(true);

        if (!canceled) {
            login = fieldLogin.getText();
            if (StringUtils.isNotBlank(login)) {
                RojacHelper.setUserName(login);
            }

            char[] p = fieldPassword.getPassword();
            if (!ArrayUtils.isEmpty(p)) {
                RojacHelper.setUserPassword(p);
            }

            RojacHelper.shouldStorePassword(fieldSavePassword.isSelected());
        }

        return canceled;
    }
}
