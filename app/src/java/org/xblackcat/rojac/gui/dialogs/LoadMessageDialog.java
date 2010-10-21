package org.xblackcat.rojac.gui.dialogs;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.util.ClipboardUtils;
import org.xblackcat.rojac.util.LinkUtils;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;

import static org.xblackcat.rojac.service.options.Property.SYNCHRONIZER_LOAD_BROKEN_TOPICS_AT_ONCE;

/**
 * @author xBlackCat
 */

public class LoadMessageDialog extends JDialog {
    private static final Log log = LogFactory.getLog(LoadMessageDialog.class);

    private Integer messageId;
    private JTextField messageIdText;
    protected JCheckBox loadAtOnce;

    public LoadMessageDialog(Window mainFrame) {
        this(mainFrame, null);
    }

    public LoadMessageDialog(Window mainFrame, Integer messageId) {
        super(mainFrame, DEFAULT_MODALITY_TYPE);
        this.messageId = messageId;

        setTitle(Messages.Dialog_LoadMessage_Title.get());

        initializeLayout();

        pack();
    }

    private void initializeLayout() {
        setLayout(new BorderLayout(0, 10));

        JPanel bp = new JPanel(new GridLayout(1, 0, 10, 5));

        add(WindowsUtils.coverComponent(bp, FlowLayout.CENTER), BorderLayout.SOUTH);

        add(WindowsUtils.createButtonsBar(
                this,
                Messages.Button_Ok,
                new AButtonAction(Messages.Button_Ok) {
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
                new AButtonAction(Messages.Button_Cancel) {
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

        loadAtOnce = new JCheckBox(
                Messages.Dialog_LoadMessage_LoadAtOnce.get(),
                SYNCHRONIZER_LOAD_BROKEN_TOPICS_AT_ONCE.get()
        );
        cp.add(loadAtOnce, BorderLayout.SOUTH);

        add(cp, BorderLayout.CENTER);

        String label;
        if (messageId == null) {
            label = Messages.Dialog_LoadMessage_Label.get();
        } else {
            label = Messages.Dialog_LoadMessage_MessageNotExists.get(messageId);
            messageIdText.setEditable(false);
        }
        JLabel l = new JLabel(label);

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
            Integer mId = LinkUtils.getMessageId(cl);
            if (mId != null && mId >= 0) {
                messageIdText.setText(String.valueOf(mId));
                wasSet = true;
            }
        }

        if (!wasSet) {
            messageIdText.setText(null);
        }

        messageIdText.selectAll();

        WindowsUtils.center(this, getOwner());

        setVisible(true);

        SYNCHRONIZER_LOAD_BROKEN_TOPICS_AT_ONCE.set(loadAtOnce.isSelected());

        return messageId;
    }

    public boolean isLoadAtOnce() {
        return loadAtOnce.isSelected();
    }
}
