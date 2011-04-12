package org.xblackcat.rojac.gui.dialog.subscribtion;

import org.xblackcat.rojac.gui.component.ACancelAction;
import org.xblackcat.rojac.gui.component.AnOkAction;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author xBlackCat
 */
public class SubscribtionDialog extends JDialog {
    private SubscribeForumModel model = new SubscribeForumModel();

    public SubscribtionDialog(Window owner) {
        super(owner);

        initializeLayout();

        pack();
        setSize(300, 500);

        new ForumLoader(model).execute();
    }

    private void initializeLayout() {
        JPanel content = new JPanel(new BorderLayout(5, 5));

        JTable forumList = new JTable(model);

        content.add(new JScrollPane(forumList), BorderLayout.CENTER);
        content.add(WindowsUtils.createButtonsBar(
                this,
                Messages.Button_Ok,
                new AnOkAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                },
                new ACancelAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                }
        ), BorderLayout.SOUTH);

        setContentPane(content);
    }

}
