package org.xblackcat.rojac.gui.component;

import org.xblackcat.rojac.gui.dialog.extendmark.ExtendedMarkDialog;
import org.xblackcat.rojac.gui.dialog.extendmark.Scope;
import org.xblackcat.rojac.gui.view.thread.SetMessagesReadFlagEx;
import org.xblackcat.rojac.i18n.Message;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author xBlackCat
 */
public class InvokeExtMarkDialogAction extends AButtonAction {
    private final Long messageDate;
    private final Scope scope;
    private final int forumId;
    private final int topicId;
    private final Window owner;

    public InvokeExtMarkDialogAction(Window owner) {
        this(null, Scope.All, 0, 0, owner);
    }

    public InvokeExtMarkDialogAction(Long messageDate, Scope scope, int forumId, int topicId, Window owner) {
        super(Message.Dialog_ExtMark_Title);
        this.messageDate = messageDate;
        this.scope = scope;
        this.forumId = forumId;
        this.topicId = topicId;
        this.owner = owner;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ExtendedMarkDialog emd = new ExtendedMarkDialog(owner);

        if (!emd.selectDate(messageDate, scope)) {
            return;
        }

        new SetMessagesReadFlagEx(
                emd.getReadStatus(),
                emd.getDateDirection(),
                emd.getSelectedDate(),
                forumId,
                topicId,
                emd.getScope()
        ).execute();
    }
}
