package org.xblackcat.rojac.gui.frame.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.IRootPane;

/**
 * @author xBlackCat
 */

public class SingleThreadView extends ATreeThreadView {
    private static final Log log = LogFactory.getLog(SingleThreadView.class);

    public SingleThreadView(IRootPane mainFrame) {
        super(mainFrame);
    }

    public void viewItem(int rootMessageId, boolean isNewMessage) {
        MessageItem mi = new MessageItem(null, rootMessageId);
        model.setRoot(mi);
        loadForumInfo(mi.getMessage(model).getForumId());

        messages.setSelectionRow(0);
    }

    public void updateItem(int messageId) {
    }

}
