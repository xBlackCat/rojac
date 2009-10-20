package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.util.LinkUtils;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * @author xBlackCat
 */

public abstract class AMessagePopupBulder implements IPopupBuilder {
    protected JPopupMenu buildMenu(int messageId, IRootPane mainFrame) {
        final JPopupMenu menu = new JPopupMenu("#" + messageId);

        JMenuItem item = new JMenuItem("#" + messageId);
        item.setEnabled(false);

        menu.add(item);
        menu.addSeparator();

        ActionListener copyMessageUrlAction = new CopyUrlAction(LinkUtils.buildMessageLink(messageId));
        ActionListener copyThreadUrlAction = new CopyUrlAction(LinkUtils.buildThreadLink(messageId));
        ActionListener copyFlatThreadUrlAction = new CopyUrlAction(LinkUtils.buildFlatThreadLink(messageId));

        JMenu copy = new JMenu();
        copy.setText(Messages.POPUP_VIEW_THREADS_TREE_COPYURL.get());

        JMenuItem copyUrl = new JMenuItem();
        copyUrl.setText(Messages.POPUP_VIEW_THREADS_TREE_COPYURL_MESSAGE.get());
        copyUrl.addActionListener(copyMessageUrlAction);
        copy.add(copyUrl);

        JMenuItem copyFlatUrl = new JMenuItem();
        copyFlatUrl.setText(Messages.POPUP_VIEW_THREADS_TREE_COPYURL_FLAT.get());
        copyFlatUrl.addActionListener(copyFlatThreadUrlAction);
        copy.add(copyFlatUrl);

        JMenuItem copyThreadUrl = new JMenuItem();
        copyThreadUrl.setText(Messages.POPUP_VIEW_THREADS_TREE_COPYURL_THREAD.get());
        copyThreadUrl.addActionListener(copyThreadUrlAction);
        copy.add(copyThreadUrl);


        menu.add(copy);


        return menu;
    }
}
