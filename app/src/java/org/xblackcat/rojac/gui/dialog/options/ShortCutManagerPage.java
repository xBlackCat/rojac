package org.xblackcat.rojac.gui.dialog.options;

import org.xblackcat.rojac.gui.component.KeyStrokeCellRenderer;
import org.xblackcat.rojac.gui.component.MessagesCellRenderer;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.util.ShortCutUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author xBlackCat
 */
class ShortCutManagerPage extends APage {
    private ShortCutsTableModel shortCutsModel = new ShortCutsTableModel();
    private JTable table;

    public ShortCutManagerPage() {
        super();

        add(new JLabel(Messages.Dialog_Options_Description_Keymap.get()), BorderLayout.NORTH);

        table = new JTable(shortCutsModel);
        table.setTableHeader(null);
        table.setDefaultEditor(KeyStroke.class, new KeyStrokeCellEditor());

        table.setDefaultRenderer(Messages.class, new MessagesCellRenderer());
        table.setDefaultRenderer(KeyStroke.class, new KeyStrokeCellRenderer());

        table.putClientProperty("JTable.autoStartsEdit", Boolean.FALSE);

        add(new JScrollPane(table));
    }

    protected void applySettings(Window mainFrame) {
        shortCutsModel.commitChanges();

        ShortCutUtils.updateShortCuts(mainFrame);
    }

    @Override
    public Messages getTitle() {
        return Messages.Dialog_Options_Title_Keymap;
    }

    @Override
    public void placeFocus() {
        table.requestFocus();
    }
}
