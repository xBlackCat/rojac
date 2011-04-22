package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.OpenMessageMethod;
import org.xblackcat.rojac.gui.view.MessageChecker;
import org.xblackcat.rojac.gui.view.ViewType;
import org.xblackcat.rojac.util.DialogHelper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Simple implementation to make "open-message" action.
 *
 * @author xBlackCat
 */
class OpenMessageAction implements ActionListener {
    private final IAppControl appControl;
    private final int messageId;
    private final int rootId;
    private final OpenMessageMethod openMessageMethod;

    public OpenMessageAction(int messageId, int rootId, IAppControl appControl, OpenMessageMethod openMessageMethod) {
        this.appControl = appControl;
        this.messageId = messageId;
        this.openMessageMethod = openMessageMethod;
        this.rootId = rootId;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO: combine openTab and openMessage into openMessageInTab
        switch (openMessageMethod) {
            case InThread:
                appControl.openTab(ViewType.SingleThread.makeId(rootId));
                appControl.openMessage(messageId);
                break;
            case InForum:
                appControl.openTab(ViewType.Forum.makeId(rootId));
                appControl.openMessage(messageId);
                break;
            case Default:
                appControl.openMessage(messageId);
                break;
            case NewTab:
                new MessageChecker(messageId) {
                    @Override
                    protected void done() {
                        if (data != null) {
                            appControl.openTab(ViewType.SingleMessage.makeId(messageId));
                        } else {
                            DialogHelper.extraMessagesDialog(appControl.getMainFrame(), messageId);
                        }
                    }
                }.execute();
                break;
        }
    }
}
