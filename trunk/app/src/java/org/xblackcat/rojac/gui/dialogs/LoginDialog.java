package org.xblackcat.rojac.gui.dialogs;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.xblackcat.rojac.gui.MainFrame;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.UserHelper;
import org.xblackcat.rojac.service.options.IOptionsService;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Date: 12 лип 2008
 *
 * @author xBlackCat
 */

public class LoginDialog extends JDialog {
    private boolean canceled;

    private JTextField fieldLogin;
    private JPasswordField fieldPassword;
    private JCheckBox fieldSavePassword;

    public LoginDialog(MainFrame mainFrame) {
        super(mainFrame, true);
        setTitle(Messages.DIALOG_LOGIN_TITLE.get());

        setContentPane(setupContentPane());

        pack();
    }

    private JComponent setupContentPane() {
        JPanel cp = new JPanel(new BorderLayout(10, 10));
        cp.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel buttons = new JPanel(new GridLayout(1, 0, 10, 5));

        JButton okButton = WindowsUtils.setupButton(Messages.BUTTON_OK, new ActionListener() {
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
        }, Messages.BUTTON_OK);
        buttons.add(okButton);
        buttons.add(WindowsUtils.setupButton(Messages.BUTTON_CANCEL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                canceled = true;
                setVisible(false);
            }
        }, Messages.BUTTON_CANCEL));

        cp.add(WindowsUtils.coverComponent(buttons, FlowLayout.CENTER), BorderLayout.SOUTH);

        JPanel pane = new JPanel(new BorderLayout(5, 5));
        cp.add(pane, BorderLayout.CENTER);

        pane.add(new JLabel(Messages.DIALOG_LOGIN_TEXT.get(), JLabel.CENTER), BorderLayout.NORTH);

        fieldLogin = new JTextField(20);
        fieldPassword = new JPasswordField(20);
        fieldSavePassword = new JCheckBox(Messages.DIALOG_LOGIN_SAVE_PASSWORD.get());

        JPanel fields = new JPanel(new GridLayout(0, 1));
        fields.add(fieldLogin);
        fields.add(fieldPassword);

        pane.add(fields, BorderLayout.CENTER);

        JPanel labels = new JPanel(new GridLayout(0, 1));
        labels.add(new JLabel(Messages.DIALOG_LOGIN_USERNAME.get()));
        labels.add(new JLabel(Messages.DIALOG_LOGIN_PASSWORD.get()));

        pane.add(labels, BorderLayout.WEST);

        pane.add(fieldSavePassword, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(okButton);

        return cp;
    }

    public boolean showLoginDialog(IOptionsService optionsService) {
        String login = UserHelper.getUserName();
        String password = UserHelper.getUserPassword();
        boolean save = UserHelper.shouldStorePassword();

        fieldLogin.setText(login);
        fieldPassword.setText(password);
        fieldSavePassword.setSelected(save);

        setVisible(true);

        if (!canceled) {
            login = fieldLogin.getText();
            if (StringUtils.isNotBlank(login)) {
                UserHelper.setUserName(login);
            }

            char[] p = fieldPassword.getPassword();
            if (!ArrayUtils.isEmpty(p)) {
                UserHelper.setUserPassword(p);
            }

            UserHelper.shouldStorePassword(fieldSavePassword.isSelected());
        }

        return canceled;
    }
}
