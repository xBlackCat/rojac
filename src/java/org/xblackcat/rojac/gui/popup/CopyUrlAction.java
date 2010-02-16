package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.util.ClipboardUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Simple implementation to make copy URL into clipboard.
 *
 * @author xBlackCat
 */
class CopyUrlAction implements ActionListener {
    protected String url;

    public CopyUrlAction(String url) {
        this.url = url;
    }

    public void actionPerformed(ActionEvent e) {
        ClipboardUtils.copyToClipboard(url);
    }
}
