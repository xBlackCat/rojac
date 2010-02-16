package org.xblackcat.rojac.gui.view.message;

import org.xblackcat.rojac.data.NewMessage;

/**
 * @author xBlackCat
 */

public class PreviewMessageView extends MessageView {
    public PreviewMessageView() {
        super(null);

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