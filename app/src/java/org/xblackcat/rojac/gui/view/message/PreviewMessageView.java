package org.xblackcat.rojac.gui.view.message;

import org.xblackcat.rojac.data.NewMessage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author xBlackCat
 */

public class PreviewMessageView extends MessageView {
    private final JLabel subjectLine = new JLabel();

    public PreviewMessageView() {
        super(null, null);

        controls.setVisible(false);
        subjectLine.setBorder(new EmptyBorder(5, 5, 0, 5));
        titleBar.add(subjectLine, BorderLayout.NORTH);
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
        subjectLine.setText(subject);
    }
}
