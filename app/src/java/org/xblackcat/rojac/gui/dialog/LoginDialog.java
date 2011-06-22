package org.xblackcat.rojac.gui.dialog;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.xblackcat.rojac.gui.component.ACancelAction;
import org.xblackcat.rojac.gui.component.AnOkAction;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.UserHelper;
import org.xblackcat.rojac.service.janus.commands.ASwingThreadedHandler;
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
        setTitle(Message.Dialog_Login_Title.get());

        setContentPane(setupContentPane());

        setResizable(false);
        pack();
    }

    private JComponent setupContentPane() {
        JPanel cp = new JPanel(new BorderLayout(10, 10));
        cp.setBorder(new EmptyBorder(10, 10, 10, 10));

        cp.add(WindowsUtils.createButtonsBar(
                this,
                Message.Button_Ok,
                new CheckCredentialsAction(),
                new ACancelAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        canceled = true;
                        setVisible(false);
                    }
                }
        ), BorderLayout.SOUTH);


        JPanel pane = new JPanel(new BorderLayout(5, 5));
        cp.add(pane, BorderLayout.CENTER);

        pane.add(new JLabel(Message.Dialog_Login_Text.get(), JLabel.CENTER), BorderLayout.NORTH);

        fieldLogin = new JTextField(20);
        fieldPassword = new JPasswordField(20);
        fieldSavePassword = new JCheckBox(Message.Dialog_Login_SavePassword.get(), RSDN_USER_PASSWORD_SAVE.get());

        pane.add(WindowsUtils.createColumn(fieldLogin, fieldPassword), BorderLayout.CENTER);

        JPanel labels = WindowsUtils.createColumn(
                new JLabel(Message.Dialog_Login_UserName.get()),
                new JLabel(Message.Dialog_Login_Password.get())
        );

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

    private class CheckCredentialsAction extends AnOkAction {
        @SuppressWarnings({"unchecked"})
        @Override
        public void actionPerformed(ActionEvent e) {
            if (StringUtils.isEmpty(fieldLogin.getText())) {
                JLOptionPane.showMessageDialog(LoginDialog.this, Message.Dialog_Login_EmptyUserName.get());
                return;
            }

            if (ArrayUtils.isEmpty(fieldPassword.getPassword())) {
                JLOptionPane.showMessageDialog(LoginDialog.this, Message.Dialog_Login_EmptyPassword.get());
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

    private class UserChecker extends ASwingThreadedHandler<Integer> {
        private boolean setUserId(Integer userId) {
            if (userId == null || userId == 0) {
                return false;
            }

            RSDN_USER_ID.set(userId);
            return true;
        }

        @Override
        public void execute(Integer userId) {
            if (!setUserId(userId)) {
                int res = JLOptionPane.showConfirmDialog(
                        LoginDialog.this,
                        Message.Dialog_Login_InvalidUserName.get(),
                        Message.Dialog_Login_InvalidUserName_Title.get(),
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
