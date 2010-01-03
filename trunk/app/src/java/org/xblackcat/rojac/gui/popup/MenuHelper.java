package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.OpenMessageMethod;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.util.LinkUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author xBlackCat
 */

public final class MenuHelper {
    private MenuHelper() {
    }

    static JMenu copyLinkSubmenu(int messageId) {
        /* Copy link sub-menu. */

        JMenu menu = new JMenu();
        menu.setText(Messages.POPUP_VIEW_THREADS_TREE_COPYURL.get());

        JMenuItem copyUrl = new JMenuItem();
        copyUrl.setText(Messages.POPUP_VIEW_THREADS_TREE_COPYURL_MESSAGE.get());
        copyUrl.addActionListener(new CopyUrlAction(LinkUtils.buildMessageLink(messageId)));
        menu.add(copyUrl);

        JMenuItem copyFlatUrl = new JMenuItem();
        copyFlatUrl.setText(Messages.POPUP_VIEW_THREADS_TREE_COPYURL_FLAT.get());
        copyFlatUrl.addActionListener(new CopyUrlAction(LinkUtils.buildFlatThreadLink(messageId)));
        menu.add(copyFlatUrl);

        JMenuItem copyThreadUrl = new JMenuItem();
        copyThreadUrl.setText(Messages.POPUP_VIEW_THREADS_TREE_COPYURL_THREAD.get());
        copyThreadUrl.addActionListener(new CopyUrlAction(LinkUtils.buildThreadLink(messageId)));
        menu.add(copyThreadUrl);
        return menu;
    }

    static JMenu openMessage(int messageId, IRootPane mainFrame) {
        JMenu menu = new JMenu();
        menu.setText("Open message");

        JMenuItem open = new JMenuItem();
        open.setText("Open message");
        open.addActionListener(new OpenMessageListener(mainFrame, messageId, OpenMessageMethod.Default));
        menu.add(open);

        JMenuItem openHere = new JMenuItem();
        openHere.setText("Open message in current view");
        openHere.addActionListener(new OpenMessageListener(mainFrame, messageId, OpenMessageMethod.ThisView));
        menu.add(openHere);

        JMenuItem openNewTab = new JMenuItem();
        openNewTab.setText("Open in new tab");
        openNewTab.addActionListener(new OpenMessageListener(mainFrame, messageId, OpenMessageMethod.NewTab));
        menu.add(openNewTab);

        return menu;
    }

    static void addOpenLink(JPopupMenu menu, String url) {
        final Desktop desktop = Desktop.getDesktop();

        if (url != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            JMenuItem openInBrowserItem = new JMenuItem(Messages.POPUP_LINK_OPEN_IN_BROWSER.get());
            openInBrowserItem.addActionListener(new OpenUrlAction(url));

            menu.add(openInBrowserItem);
        }
    }

    static JMenuItem copyToClipboard(String url) {
        JMenuItem copyToClipboard = new JMenuItem(Messages.POPUP_LINK_COPY_TO_CLIPBOARD.get());
        copyToClipboard.addActionListener(new CopyUrlAction(url));

        return copyToClipboard;        
    }

    private static class OpenMessageListener implements ActionListener {
        private final IRootPane mainFrame;
        private final int messageId;
        protected OpenMessageMethod openMessageMethod;

        public OpenMessageListener(IRootPane mainFrame, int messageId, OpenMessageMethod openMessageMethod) {
            this.mainFrame = mainFrame;
            this.messageId = messageId;
            this.openMessageMethod = openMessageMethod;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            mainFrame.openMessage(messageId, openMessageMethod);
        }
    }
}
