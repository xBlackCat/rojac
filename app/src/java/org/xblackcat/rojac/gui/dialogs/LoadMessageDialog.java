package org.xblackcat.rojac.gui.dialogs;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.MainFrame;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.util.ClipboardUtils;
import org.xblackcat.rojac.util.LinkUtils;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

/**
 * @author xBlackCat
 */

public class LoadMessageDialog extends JDialog {
    private static final Log log = LogFactory.getLog(LoadMessageDialog.class);

    private Integer messageId;
    private JTextField messageIdText;
    private MainFrame mainFrame;
    protected JCheckBox loadAtOnce;

    public LoadMessageDialog(MainFrame mainFrame) {
        super(mainFrame, true);
        this.mainFrame = mainFrame;

        setTitle(Messages.DIALOG_LOADMESSAGE_TITLE.get());

        initializeLayout();

        pack();
    }

    private void initializeLayout() {
        setLayout(new BorderLayout(0, 10));

        JPanel bp = new JPanel(new GridLayout(1, 0, 10, 5));

        add(WindowsUtils.coverComponent(bp, FlowLayout.CENTER), BorderLayout.SOUTH);

        JButton okButton = WindowsUtils.setupButton(Messages.BUTTON_OK, new OkListener(), Messages.BUTTON_OK);
        bp.add(okButton);
        bp.add(WindowsUtils.setupButton(Messages.BUTTON_CANCEL, new CancelListener(), Messages.BUTTON_CANCEL));

        getRootPane().setDefaultButton(okButton);

        JPanel cp = new JPanel(new BorderLayout());

        NumberFormat f = NumberFormat.getIntegerInstance();
        f.setMaximumFractionDigits(0);
        f.setParseIntegerOnly(true);
        f.setGroupingUsed(false);
        messageIdText = new JFormattedTextField(f);
        EmptyBorder border = new EmptyBorder(0, 10, 0, 10);
        messageIdText.setHorizontalAlignment(JTextField.RIGHT);
        messageIdText.setBorder(border);
        cp.add(messageIdText, BorderLayout.NORTH);

        loadAtOnce = new JCheckBox();
        loadAtOnce.setText(Messages.DIALOG_LOADMESSAGE_LOADATONCE.get());
        cp.add(loadAtOnce, BorderLayout.SOUTH);

        add(cp, BorderLayout.CENTER);

        JLabel l = new JLabel(Messages.DIALOG_LOADMESSAGE_LABEL.get());

        l.setBorder(border);
        add(l, BorderLayout.NORTH);

        ((JComponent) getContentPane()).setBorder(new EmptyBorder(20, 10, 20, 10));

        setResizable(false);
    }

    public Integer readMessageId() {
        String cl = ClipboardUtils.getStringFromClipboard();

        boolean wasSet = false;
        // Check if the clipboard contains integer value
        try {
            int mId = Integer.parseInt(cl);
            if (mId >= 0) {
                messageIdText.setText(cl);
                wasSet = true;
            }
        } catch (NumberFormatException e) {
            // The clipboard is not contains an integer value.
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

        WindowsUtils.center(this, mainFrame);

        setVisible(true);

        return messageId;
    }

    public boolean isLoadAtOnce() {
        return loadAtOnce.isSelected();
    }

    private class OkListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String id = messageIdText.getText();

            if (StringUtils.isBlank(id)) {
                messageId = null;
            } else {
                try {
                    int iId = Integer.parseInt(id);
                    if (iId >= 0) {
                        messageId = Integer.valueOf(iId);
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
    }

    private class CancelListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            messageId = null;
            setVisible(false);
        }
    }
}
