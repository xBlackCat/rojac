package org.xblackcat.rojac.gui.dialog.ignoreunread;

import org.jdesktop.swingx.combobox.EnumComboBoxModel;
import org.xblackcat.rojac.gui.component.ACancelAction;
import org.xblackcat.rojac.gui.component.AnOkAction;
import org.xblackcat.rojac.gui.component.DescribableListRenderer;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * 13.12.11 16:32
 *
 * @author xBlackCat
 */
public class IgnoreTopicsDialog extends JDialog {
    private TopicIgnoringSelection acceptedSelectionType = null;
    private final EnumComboBoxModel<TopicIgnoringSelection> model = new EnumComboBoxModel<>(TopicIgnoringSelection.class);

    public IgnoreTopicsDialog(Window owner) {
        super(owner, Message.Dialog_IgnoreTopics_Title.get(), ModalityType.DOCUMENT_MODAL);

        setContentPane(createContentPane());

        pack();
        setResizable(false);
    }

    private Container createContentPane() {
        JPanel cp = new JPanel(new BorderLayout(5, 5));
        cp.setBorder(new EmptyBorder(10, 10, 10, 10));

        cp.add(new JLabel(Message.Dialog_IgnoreTopics_TopLine.get()), BorderLayout.NORTH);

        JPanel centerPane = new JPanel(new BorderLayout(5, 5));

        JComboBox<TopicIgnoringSelection> selectionTypeCB = new JComboBox<>(model);
        selectionTypeCB.setRenderer(new DescribableListRenderer());
        final JCheckBox showDialogCB = new JCheckBox(Message.Dialog_IgnoreTopics_RememberSettings.get());

        showDialogCB.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Property.IGNORE_TOPICS_DIALOG_SHOW.set(!showDialogCB.isSelected());
            }
        });

        centerPane.add(new JLabel(Message.Dialog_IgnoreTopics_SelectionMethod.get()), BorderLayout.WEST);
        centerPane.add(selectionTypeCB, BorderLayout.CENTER);
        centerPane.add(showDialogCB, BorderLayout.SOUTH);

        cp.add(centerPane);

        cp.add(
                WindowsUtils.createButtonsBar(
                        this,
                        Message.Button_Ok,
                        new AnOkAction() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                acceptedSelectionType = model.getSelectedItem();
                                dispose();
                            }
                        },
                        new ACancelAction() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                acceptedSelectionType = null;
                                dispose();
                            }
                        }
                ),
                BorderLayout.SOUTH
        );

        return cp;
    }

    public TopicIgnoringSelection getAcceptedSelectionType() {
        return acceptedSelectionType;
    }
}
