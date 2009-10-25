package org.xblackcat.rojac.gui.view.message;

import org.xblackcat.rojac.data.NewMessage;
import org.xblackcat.rojac.gui.IRootPane;

/**
 * @author xBlackCat
 */

public class PreviewMessageView extends MessageView {
    public PreviewMessageView(IRootPane mainFrame) {
        super(mainFrame);

        marks.setEnabled(false);
        answer.setEnabled(false);
    }

    public void showPreview(String subject, String body) {
        NewMessage m = new NewMessage(
                0,
                -1,
                -1,
                subject,
                body
        );

        fillFrame(m);
    }
}