package org.xblackcat.rojac.gui.popup;

import org.apache.commons.lang.ArrayUtils;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.util.ClipboardUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Date: 19 ρεπο 2009
 *
 * @author xBlackCat
 */

public class TreeThreadViewPopupBuilder implements IPopupBuilder {

    @Override
    public JPopupMenu buildMenu(Object... parameters) {
        if (ArrayUtils.isEmpty(parameters) || parameters.length != 1) {
            throw new IllegalArgumentException("Invalid parameters amount.");
        }

        final int messageId = ((Integer)parameters[0]).intValue();
        final JPopupMenu menu = new JPopupMenu("#" + messageId);

        JMenuItem item = new JMenuItem("#" + messageId);
        item.setEnabled(false);

        menu.add(item);
        menu.addSeparator();

        ActionListener copyMessageUrlAction = new CopyUrlAction("http://rsdn.ru/forum/message/" + messageId + ".1.aspx");
        ActionListener copyThreadUrlAction = new CopyUrlAction("http://rsdn.ru/forum/message/" + messageId + ".aspx");
        ActionListener copyFlatThreadUrlAction = new CopyUrlAction("http://rsdn.ru/forum/message/" + messageId + ".flat.aspx");

        JMenu copy = new JMenu();
        copy.setText(Messages.VIEW_THREADS_TREE_MENU_COPYURL.getMessage());

        JMenuItem copyUrl = new JMenuItem();
        copyUrl.setText(Messages.VIEW_THREADS_TREE_MENU_COPYURL_MESSAGE.getMessage());
        copyUrl.addActionListener(copyMessageUrlAction);
        copy.add(copyUrl);

        JMenuItem copyFlatUrl = new JMenuItem();
        copyFlatUrl.setText(Messages.VIEW_THREADS_TREE_MENU_COPYURL_FLAT.getMessage());
        copyFlatUrl.addActionListener(copyFlatThreadUrlAction);
        copy.add(copyFlatUrl);

        JMenuItem copyThreadUrl = new JMenuItem();
        copyThreadUrl.setText(Messages.VIEW_THREADS_TREE_MENU_COPYURL_THREAD.getMessage());
        copyThreadUrl.addActionListener(copyThreadUrlAction);
        copy.add(copyThreadUrl);


        menu.add(copy);


        return menu;
    }

    private class CopyUrlAction implements ActionListener {
        protected String url;

        public CopyUrlAction(String url) {
            this.url = url;
        }

        public void actionPerformed(ActionEvent e) {
            ClipboardUtils.copyToClipboard(url);
        }
    }
}