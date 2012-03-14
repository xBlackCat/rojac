package org.xblackcat.rojac.gui.hint;

import org.jdesktop.swingx.JXBusyLabel;
import org.xblackcat.rojac.data.User;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.SpringUtilities;

import javax.swing.*;
import java.awt.*;

/**
 * 23.02.12 16:05
 *
 * @author xBlackCat
 */
public class UserInfoPane extends JPanel {
    private final JLabel labelUserId = new JLabel();
    private final JLabel labelUserName = new JLabel();
    private final JLabel labelUserNick = new JLabel();
    private final JLabel labelRealName = new JLabel();
    private final JLabel labelPublicEmail = new JLabel();
    private final JLabel labelHomePage = new JLabel();
    private final JLabel labelSpecialization = new JLabel();
    private final JLabel labelWhereFrom = new JLabel();
    private final JLabel labelOrigin = new JLabel("", SwingConstants.CENTER);
    private final JLabel labelUserClass = new JLabel();

    private final JPanel centralPane;
    private final JXBusyLabel loadingComponent;

    public UserInfoPane() {
        super(new BorderLayout(5, 5));

        centralPane = setupUserInfoPane();
        loadingComponent = setupLoadingComponent();

        JPanel labelPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        labelPane.add(labelUserId);
        labelPane.add(labelUserName);

        add(labelPane, BorderLayout.NORTH);
        add(centralPane, BorderLayout.CENTER);
        add(loadingComponent, BorderLayout.SOUTH);

        setMinimumSize(new Dimension(100, 50));

        Color color = new Color(0xFFFFCC);
        setBackground(color);

        for (Component c : getComponents()) {
            c.setBackground(color);
            if (c instanceof Container) {
                for (Component cc : ((Container) c).getComponents()) {
                    cc.setBackground(color);
                }
            }
        }
    }

    private JPanel setupUserInfoPane() {
        JPanel pane = new JPanel(new BorderLayout(5, 5));

        JPanel fieldsPane = new JPanel(new SpringLayout());
        pane.add(fieldsPane, BorderLayout.CENTER);

        fieldsPane.add(new JLabel(Message.UserInfoPane_Label_UserNick.get()));
        fieldsPane.add(labelUserNick);
        fieldsPane.add(new JLabel(Message.UserInfoPane_Label_RealName.get()));
        fieldsPane.add(labelRealName);
        fieldsPane.add(new JLabel(Message.UserInfoPane_Label_PublicEmail.get()));
        fieldsPane.add(labelPublicEmail);
        fieldsPane.add(new JLabel(Message.UserInfoPane_Label_HomePage.get()));
        fieldsPane.add(labelHomePage);
        fieldsPane.add(new JLabel(Message.UserInfoPane_Label_Specialization.get()));
        fieldsPane.add(labelSpecialization);
        fieldsPane.add(new JLabel(Message.UserInfoPane_Label_WhereFrom.get()));
        fieldsPane.add(labelWhereFrom);
        fieldsPane.add(new JLabel(Message.UserInfoPane_Label_Origin.get()));
        fieldsPane.add(labelOrigin);
        fieldsPane.add(new JLabel(Message.UserInfoPane_Label_UserClass.get()));
        fieldsPane.add(labelUserClass);

        SpringUtilities.makeCompactGrid(fieldsPane, 8, 2, 5, 5, 5, 5);

        pane.add(labelOrigin, BorderLayout.SOUTH);

        return pane;
    }

    private static JXBusyLabel setupLoadingComponent() {
        JXBusyLabel loadingComponent = new JXBusyLabel(new Dimension(40, 40));
        loadingComponent.setHorizontalAlignment(SwingConstants.CENTER);
        loadingComponent.setBusy(true);
        loadingComponent.setDelay(333);
        return loadingComponent;
    }

    public void setUserInfo(User userInfo) {
        assert RojacUtils.checkThread(true);
        assert userInfo != null : "Do not show info for anonymous users!";

        labelUserId.setText("#" + userInfo.getId());
        labelUserName.setText(userInfo.getUserName());
        labelUserNick.setText(userInfo.getUserNick());
        labelRealName.setText(userInfo.getRealName());
        labelHomePage.setText(userInfo.getHomePage());
        labelPublicEmail.setText(userInfo.getPublicEmail());
        labelSpecialization.setText(userInfo.getSpecialization());
        labelOrigin.setText(userInfo.getOrigin());
        labelWhereFrom.setText(userInfo.getWhereFrom());
//        labelUserClass.setText(userInfo.get);

        // Show loading pane
        remove(loadingComponent);
        add(centralPane, BorderLayout.CENTER);
        revalidate();
    }

    public void setUserName(int userId, String userName) {
        assert RojacUtils.checkThread(true);

        labelUserName.setText(userName);
        labelUserId.setText("#" + userId);

        // Show loading pane
        remove(centralPane);
        add(loadingComponent, BorderLayout.CENTER);
        revalidate();
    }
}
