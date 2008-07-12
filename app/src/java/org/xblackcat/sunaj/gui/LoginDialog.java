package org.xblackcat.sunaj.gui;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.xblackcat.sunaj.i18n.Messages;
import org.xblackcat.sunaj.service.ServiceFactory;
import org.xblackcat.sunaj.service.options.IOptionsService;
import org.xblackcat.sunaj.service.options.Password;
import org.xblackcat.sunaj.service.options.Property;
import org.xblackcat.sunaj.util.WindowsUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

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
        setTitle(Messages.DIALOG_LOGIN_TITLE.getMessage());

        setContentPane(setupContentPane());

        pack();
    }

    private JComponent setupContentPane() {
        JPanel cp = new JPanel(new BorderLayout(10, 10));
        cp.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel buttons = new JPanel(new GridLayout(1, 0, 10, 5));

        JButton okButton = new JButton(new AbstractAction(Messages.BUTTON_OK.getMessage()) {
            public void actionPerformed(ActionEvent e) {
                canceled = false;
                setVisible(false);
            }
        });
        buttons.add(okButton);
        buttons.add(new JButton(new AbstractAction(Messages.BUTTON_CANCEL.getMessage()) {
            public void actionPerformed(ActionEvent e) {
                canceled = true;
                setVisible(false);
            }
        }));

        cp.add(WindowsUtils.coverComponent(buttons, FlowLayout.CENTER), BorderLayout.SOUTH);

        JPanel pane = new JPanel(new BorderLayout(5, 5));
        cp.add(pane, BorderLayout.CENTER);

        pane.add(new JLabel(Messages.DIALOG_LOGIN_TEXT.getMessage(), JLabel.CENTER), BorderLayout.NORTH);

        fieldLogin = new JTextField(20);
        fieldPassword = new JPasswordField(20);
        fieldSavePassword = new JCheckBox(Messages.DIALOG_LOGIN_SAVE_PASSWORD.getMessage());

        JPanel fields = new JPanel(new GridLayout(0, 1));
        fields.add(fieldLogin);
        fields.add(fieldPassword);

        pane.add(fields, BorderLayout.CENTER);

        JPanel labels = new JPanel(new GridLayout(0, 1));
        labels.add(new JLabel(Messages.DIALOG_LOGIN_USERNAME.getMessage()));
        labels.add(new JLabel(Messages.DIALOG_LOGIN_PASSWORD.getMessage()));

        pane.add(labels, BorderLayout.WEST);

        pane.add(fieldSavePassword, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(okButton);

        return cp;
    }

    public boolean showLoginDialog() {
        IOptionsService os = ServiceFactory.getInstance().getOptionsService();

        String login = os.getProperty(Property.RSDN_USER_NAME);
        Password password = os.getProperty(Property.RSDN_USER_PASSWORD);
        boolean save = os.getProperty(Property.RSDN_USER_PASSWORD_SAVE);

        fieldLogin.setText(login);
        fieldPassword.setText(password == null ? null : new String(password.getPassword()));
        fieldSavePassword.setSelected(save);

        setVisible(true);

        if (!canceled) {
            login = fieldLogin.getText();
            if (StringUtils.isNotBlank(login)) {
                os.setProperty(Property.RSDN_USER_NAME, login);
            }

            char[] p = fieldPassword.getPassword();
            if (!ArrayUtils.isEmpty(p)) {
                password = new Password(p);
                os.setProperty(Property.RSDN_USER_PASSWORD, password);
            }

            os.setProperty(Property.RSDN_USER_PASSWORD_SAVE, fieldSavePassword.isSelected());
        }

        return canceled;
    }
}
