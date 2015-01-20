package org.xblackcat.rojac.gui.dialog.db;

import org.h2.Driver;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.util.RojacWorker;
import org.xblackcat.rojac.util.WindowsUtils;
import org.xblackcat.sjpu.storage.IStorage;
import org.xblackcat.sjpu.storage.StorageBuilder;
import org.xblackcat.sjpu.storage.connection.DBConfig;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

import static org.xblackcat.rojac.i18n.Message.Dialog_DbSettings_Label_Url;

/**
 * @author xBlackCat
 */
public class DBSettingsPane extends JPanel {
    private JTextField fieldUrl;
    private final JButton checkButton;

    /**
     */
    public DBSettingsPane() {
        super(new BorderLayout(5, 5));


        add(getFieldsList(), BorderLayout.CENTER);
        checkButton = WindowsUtils.setupButton(new CheckDBSettings());
        JPanel cover = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        cover.setBorder(null);
        cover.add(checkButton);
        cover.setBackground(getBackground());
        add(cover, BorderLayout.SOUTH);
    }

    private JComponent getFieldsList() {
        JPanel fieldsPane = new JPanel();
        fieldsPane.setBorder(new EmptyBorder(0, 5, 0, 5));
        GroupLayout groupLayout = new GroupLayout(fieldsPane);
        fieldsPane.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);

        fieldUrl = new JTextField(50);

        JLabel labelUrl = new JLabel(Dialog_DbSettings_Label_Url.get());

        GroupLayout.SequentialGroup hGroup = groupLayout.createSequentialGroup();

        GroupLayout.SequentialGroup vGroup = groupLayout.createSequentialGroup();

        groupLayout.setVerticalGroup(vGroup);
        groupLayout.setHorizontalGroup(hGroup);

        hGroup
                .addGroup(
                        groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                .addComponent(labelUrl)
                )
                .addGroup(
                        groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING, true)
                                .addComponent(fieldUrl)
                );

        vGroup
                .addGroup(
                        groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                                .addComponent(labelUrl)
                                .addComponent(fieldUrl)
                );

        return fieldsPane;
    }

    public DBConfig getCurrentSettings() {
        return new DBConfig(
                Driver.class.getName(),
                fieldUrl.getText(),
                null,
                null,
                10
        );
    }

    private class CheckDBSettings extends AButtonAction {
        public CheckDBSettings() {
            super(Message.Button_Check);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final DBConfig curSet = getCurrentSettings();

            if (curSet == null) {
                return;
            }

            checkButton.setEnabled(false);

            Runnable buttonEnabler = () -> checkButton.setEnabled(true);
            new RojacWorker<Void, Throwable>(buttonEnabler) {
                @Override
                protected Void perform() throws Exception {
                    try {
                        IStorage storage = StorageBuilder.defaultStorage(curSet);

                        ITestConnectionAH ah = storage.get(ITestConnectionAH.class);
                        Number answer = ah.getAnswerToTheUltimateQuestionOfLifeTheUniverseAndEverything();

                        if (answer != null && answer.intValue() == 42) {
                            publish((Throwable) null);
                        } else {
                            publish(new IllegalStateException("Database math is wrong!"));
                        }

                        storage.shutdown();
                    } catch (Exception e1) {
                        publish(e1);
                    }


                    return null;
                }

                @Override
                protected void process(List<Throwable> chunks) {
                    for (Throwable check : chunks) {
                        if (check == null) {
                            JLOptionPane.showMessageDialog(
                                    DBSettingsPane.this,
                                    Message.Dialog_DbCheck_Success.get(),
                                    Message.Dialog_DbCheck_Title.get(),
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        } else {
                            if (check instanceof RojacException) {
                                check = check.getCause();
                            }

                            JLOptionPane.showMessageDialog(
                                    DBSettingsPane.this,
                                    Message.Dialog_DbCheck_Fail.get(check.getLocalizedMessage()),
                                    Message.Dialog_DbCheck_Title.get(),
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                }
            }.execute();
        }
    }
}
