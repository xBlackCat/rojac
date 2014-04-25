package org.xblackcat.rojac.gui.dialog;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.combobox.EnumComboBoxModel;
import org.xblackcat.rojac.gui.OpenMessageMethod;
import org.xblackcat.rojac.gui.component.ACancelAction;
import org.xblackcat.rojac.gui.component.AnOkAction;
import org.xblackcat.rojac.gui.component.DescribableListRenderer;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.util.ClipboardUtils;
import org.xblackcat.rojac.util.LinkUtils;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;

/**
 * @author xBlackCat
 */

public class OpenMessageDialog extends JDialog {
    private static final Log log = LogFactory.getLog(OpenMessageDialog.class);

    private Integer messageId;
    private JTextField messageIdText;
    private EnumComboBoxModel<OpenMessageMethod> model;

    public OpenMessageDialog(Window mainFrame) {
        super(mainFrame, DEFAULT_MODALITY_TYPE);

        setTitle(Message.Dialog_OpenMessage_Title.get());

        initializeLayout();

        pack();
    }

    private void initializeLayout() {
        setLayout(new BorderLayout(0, 10));

        JPanel bp = new JPanel(new GridLayout(1, 0, 10, 5));

        add(WindowsUtils.coverComponent(bp, FlowLayout.CENTER), BorderLayout.SOUTH);

        add(WindowsUtils.createButtonsBar(
                this,
                Message.Button_Ok,
                new AnOkAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String id = messageIdText.getText();

                        if (StringUtils.isBlank(id)) {
                            messageId = null;
                        } else {
                            try {
                                int iId = Integer.parseInt(id);
                                if (iId >= 0) {
                                    messageId = iId;
                                } else {
                                    messageId = null;
                                }
                            } catch (NumberFormatException e1) {
                                if (log.isWarnEnabled()) {
                                    log.warn("Can not parse user input", e1);
                                }
                                messageId = null;
                            }
                        }

                        setVisible(false);
                    }
                },
                new ACancelAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        messageId = null;
                        setVisible(false);
                    }
                }
        ), BorderLayout.SOUTH);

        JPanel cp = new JPanel(new BorderLayout());

        NumberFormat f = NumberFormat.getIntegerInstance();
        f.setMaximumFractionDigits(0);
        f.setParseIntegerOnly(true);
        f.setGroupingUsed(false);
        messageIdText = new JFormattedTextField(f);
        messageIdText.setHorizontalAlignment(JTextField.RIGHT);
        cp.add(messageIdText, BorderLayout.NORTH);

        model = new EnumComboBoxModel<>(OpenMessageMethod.class);
        model.setSelectedItem(Property.OPEN_MESSAGE_BEHAVIOUR_GENERAL.get());
        JComboBox<OpenMessageMethod> comboBox = new JComboBox<>(model);
        comboBox.setRenderer(new DescribableListRenderer());

        cp.add(comboBox, BorderLayout.SOUTH);

        add(cp, BorderLayout.CENTER);

        JLabel l = new JLabel(Message.Dialog_OpenMessage_Label.get());

        add(l, BorderLayout.NORTH);

        ((JComponent) getContentPane()).setBorder(new EmptyBorder(20, 10, 20, 10));

        setResizable(false);
    }

    public Integer readMessageId() {
        String cl = ClipboardUtils.getStringFromClipboard();

        boolean wasSet = false;
        // If a message id is given - just use it.
        if (messageId != null) {
            messageIdText.setText(String.valueOf(messageId));
            wasSet = true;
        }

        // Check if the clipboard contains integer value
        if (!wasSet) {
            try {
                int mId = Integer.parseInt(cl);
                if (mId >= 0) {
                    messageIdText.setText(cl);
                    wasSet = true;
                }
            } catch (NumberFormatException e) {
                // The clipboard is not contains an integer value.
            }
        }

        if (!wasSet) {
            // Check if the clipboard contains URL to message or topic
            Integer mId = LinkUtils.getMessageIdFromUrl(cl);
            if (mId != null && mId >= 0) {
                messageIdText.setText(String.valueOf(mId));
                wasSet = true;
            }
        }

        if (!wasSet) {
            messageIdText.setText(null);
        }

        messageIdText.selectAll();

        WindowsUtils.center(this);

        setVisible(true);

        return messageId;
    }

    public OpenMessageMethod getOpenMethod() {
        return model.getSelectedItem();
    }
}
