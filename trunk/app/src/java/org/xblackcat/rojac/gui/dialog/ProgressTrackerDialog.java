package org.xblackcat.rojac.gui.dialog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.progress.IProgressListener;
import org.xblackcat.rojac.service.progress.ProgressChangeEvent;
import org.xblackcat.rojac.service.progress.ProgressState;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import static org.xblackcat.rojac.service.options.Property.DIALOGS_PROGRESS_AUTOHIDE;
import static org.xblackcat.rojac.service.options.Property.DIALOGS_PROGRESS_AUTOSHOW;

/**
 * @author xBlackCat
 */

// TODO: implement progress control based on JXBusyLabel.
public class ProgressTrackerDialog extends JDialog implements IProgressListener {
    private static final Log log = LogFactory.getLog(ProgressTrackerDialog.class);

    private JTextArea logArea = new JTextArea();
    private JProgressBar logProgress;

    public ProgressTrackerDialog(Window mainFrame) {
        super(mainFrame, ModalityType.MODELESS);
        JPanel cp = new JPanel(new BorderLayout());

        cp.add(new JScrollPane(logArea), BorderLayout.CENTER);
        logProgress = new JProgressBar(JProgressBar.VERTICAL, 0, 100);
        logProgress.setStringPainted(true);
        cp.add(logProgress, BorderLayout.EAST);

        setContentPane(cp);

        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                logArea.setText(null);
                logProgress.setValue(0);
            }
        });

        setSize(500, 350);
    }

    @Override
    public void progressChanged(ProgressChangeEvent e) {
        // Track state
        if (e.getState() == ProgressState.Start && DIALOGS_PROGRESS_AUTOSHOW.get()) {
            setVisible(true);
        }

        if (e.getState() == ProgressState.Stop && DIALOGS_PROGRESS_AUTOHIDE.get()) {
            setVisible(false);
        }

        if (e.getProgress() != null) {
            int progress = e.getProgress();
            if (progress >= 0) {
                logProgress.setValue(progress);
            }
            logProgress.setIndeterminate(progress < 0);
        }

        if (e.getText() != null) {
            Document document = logArea.getDocument();

            try {
                document.insertString(document.getLength(), e.getText(), new SimpleAttributeSet());
                document.insertString(document.getLength(), "\n", new SimpleAttributeSet());
            } catch (BadLocationException e1) {
                log.error("Can not add log text.", e1);
            }
        }
    }
}
