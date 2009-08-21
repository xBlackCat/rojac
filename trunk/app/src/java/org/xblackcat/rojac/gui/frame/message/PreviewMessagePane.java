package org.xblackcat.rojac.gui.frame.message;

import org.xblackcat.rojac.data.NewMessage;
import org.xblackcat.rojac.gui.IRootPane;

/**
 * Date: 31 груд 2007
 *
 * @author xBlackCat
 */

public class PreviewMessagePane extends MessagePane {
    public PreviewMessagePane(IRootPane mainFrame) {
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