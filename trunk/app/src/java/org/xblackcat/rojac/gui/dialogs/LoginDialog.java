package org.xblackcat.rojac.gui.dialogs;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.UserHelper;
import org.xblackcat.rojac.service.janus.commands.AffectedMessage;
import org.xblackcat.rojac.service.janus.commands.IResultHandler;
import org.xblackcat.rojac.service.janus.commands.Request;
import org.xblackcat.rojac.service.options.Password;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

import static org.xblackcat.rojac.service.options.Property.*;

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
        setTitle(Messages.Dialog_Login_Title.get());

        setContentPane(setupContentPane());

        setResizable(false);
        pack();
    }

    private JComponent setupContentPane() {
        JPanel cp = new JPanel(new BorderLayout(10, 10));
        cp.setBorder(new EmptyBorder(10, 10, 10, 10));

        cp.add(WindowsUtils.createButtonsBar(
                this,
                Messages.Button_Ok,
                new CheckCredentialsAction(),
                new AButtonAction(Messages.Button_Cancel) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        canceled = true;
                        setVisible(false);
                    }
                }
        ), BorderLayout.SOUTH);


        JPanel pane = new JPanel(new BorderLayout(5, 5));
        cp.add(pane, BorderLayout.CENTER);

        pane.add(new JLabel(Messages.Dialog_Login_Text.get(), JLabel.CENTER), BorderLayout.NORTH);

        fieldLogin = new JTextField(20);
        fieldPassword = new JPasswordField(20);
        fieldSavePassword = new JCheckBox(Messages.Dialog_Login_SavePassword.get(), RSDN_USER_PASSWORD_SAVE.get());

        JPanel fields = new JPanel(new GridLayout(0, 1));
        fields.add(fieldLogin);
        fields.add(fieldPassword);

        pane.add(fields, BorderLayout.CENTER);

        JPanel labels = new JPanel(new GridLayout(0, 1));
        labels.add(new JLabel(Messages.Dialog_Login_UserName.get()));
        labels.add(new JLabel(Messages.Dialog_Login_Password.get()));

        pane.add(labels, BorderLayout.WEST);

        pane.add(fieldSavePassword, BorderLayout.SOUTH);

        return cp;
    }

    public boolean showLoginDialog() {
        String userName = RSDN_USER_NAME.get();
        Password password = RSDN_USER_PASSWORD.get();
        boolean save = RSDN_USER_PASSWORD_SAVE.get();

        fieldLogin.setText(userName);
        fieldPassword.setText(password == null ? null : password.toString());
        fieldSavePassword.setSelected(save);

        setVisible(true);

        if (canceled) {
            RSDN_USER_NAME.set(userName);
            RSDN_USER_PASSWORD.set(password);
            RSDN_USER_PASSWORD_SAVE.set(save);
        }

        return canceled;
    }

    private class CheckCredentialsAction extends AButtonAction {
        public CheckCredentialsAction() {
            super(Messages.Button_Ok);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (StringUtils.isEmpty(fieldLogin.getText())) {
                JLOptionPane.showMessageDialog(LoginDialog.this, Messages.Dialog_Login_EmptyUserName.get());
                return;
            }

            if (ArrayUtils.isEmpty(fieldPassword.getPassword())) {
                JLOptionPane.showMessageDialog(LoginDialog.this, Messages.Dialog_Login_EmptyPassword.get());
                return;
            }

            String userName = fieldLogin.getText();
            if (StringUtils.isNotBlank(userName)) {
                RSDN_USER_NAME.set(userName);
            }

            char[] p = fieldPassword.getPassword();
            if (!ArrayUtils.isEmpty(p)) {
                UserHelper.setUserPassword(p);
            }

            RSDN_USER_PASSWORD_SAVE.set(fieldSavePassword.isSelected());

            Request.GET_USER_ID.process(new UserChecker());
        }
    }

    private class UserChecker implements IResultHandler {
        private boolean setUserId(AffectedMessage... results) {
            if (ArrayUtils.isEmpty(results)) {
                return false;
            }

            int userId = results[0].getForumId();

            if (userId == 0) {
                return false;
            }

            RSDN_USER_ID.set(userId);
            return true;
        }

        @Override
        public void process(AffectedMessage... results) {
            if (!setUserId(results)) {
                int res = JLOptionPane.showConfirmDialog(
                        LoginDialog.this,
                        Messages.Dialog_Login_InvalidUserName.get(),
                        Messages.Dialog_Login_InvalidUserName_Title.get(),
                        JOptionPane.YES_NO_OPTION
                        );
                if (res == JOptionPane.NO_OPTION) {
                    // Let user to enter another login/password.
                    return;
                }
            }

            canceled = false;
            setVisible(false);
        }
    }
}
