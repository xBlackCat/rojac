package org.xblackcat.rojac.gui.dialog.options;

import org.xblackcat.rojac.gui.component.KeyStrokeCellRenderer;
import org.xblackcat.rojac.gui.component.MessagesCellRenderer;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.util.ShortCutUtils;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author xBlackCat
 */
class ShortCutManagerPage extends APage {
    private ShortCutsTableModel shortCutsModel = new ShortCutsTableModel();
    private JTable table;

    public ShortCutManagerPage() {
        super();

        add(new JLabel(Message.Dialog_Options_Description_Keymap.get()), BorderLayout.NORTH);

        table = new JTable(shortCutsModel);
        table.setTableHeader(null);
        table.setDefaultEditor(KeyStroke.class, new KeyStrokeCellEditor());

        table.setDefaultRenderer(Message.class, new MessagesCellRenderer());
        table.setDefaultRenderer(KeyStroke.class, new KeyStrokeCellRenderer());

        ActionMap actionMap = table.getActionMap();
        while (actionMap != null) {
            actionMap.remove("cancel");
            actionMap = actionMap.getParent();
        }

        final ListSelectionModel columnSelectionModel = table.getColumnModel().getSelectionModel();
        columnSelectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    columnSelectionModel.setSelectionInterval(2, 2);
                }
            }
        }
        );

        table.putClientProperty("JTable.autoStartsEdit", Boolean.FALSE);

        add(new JScrollPane(table), BorderLayout.CENTER);

        add(
                WindowsUtils.coverComponent(
                        new JButton(new AbstractAction(Message.Button_Default.get()) {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                shortCutsModel.setInitial();
                            }
                        }),
                        FlowLayout.RIGHT,
                        getBackground()
                ),
                BorderLayout.SOUTH
        );
    }

    protected void applySettings(Window mainFrame) {
        shortCutsModel.commitChanges();

        ShortCutUtils.updateShortCuts(mainFrame);
    }

    @Override
    public Message getTitle() {
        return Message.Dialog_Options_Title_Keymap;
    }

    @Override
    public void placeFocus() {
        table.requestFocus();
    }
}
