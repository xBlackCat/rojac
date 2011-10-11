package org.xblackcat.rojac.gui.dialog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.progress.IProgressListener;
import org.xblackcat.rojac.service.progress.ProgressChangeEvent;
import org.xblackcat.rojac.service.progress.ProgressState;
import org.xblackcat.rojac.util.SynchronizationUtils;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;

import static org.xblackcat.rojac.service.options.Property.*;

/**
 * @author xBlackCat
 */

// TODO: implement progress control based on JXBusyLabel.
public class ProgressTrackerDialog extends JDialog implements IProgressListener {
    private static final Log log = LogFactory.getLog(ProgressTrackerDialog.class);

    private JTextArea logArea = new JTextArea();
    private JProgressBar logProgress = new JProgressBar(JProgressBar.VERTICAL, 0, 100);

    public ProgressTrackerDialog(Window mainFrame) {
        super(mainFrame, ModalityType.MODELESS);
        JPanel cp = new JPanel(new BorderLayout());

        logArea.setEditable(false);

        cp.add(new JScrollPane(logArea), BorderLayout.CENTER);
        logProgress.setStringPainted(true);
        cp.add(logProgress, BorderLayout.EAST);

        setContentPane(cp);

        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        setSize(500, 350);
    }

    @Override
    public void progressChanged(ProgressChangeEvent e) {
        if (e.getState() == ProgressState.Exception) {
            logProgress.setValue(0);
            logProgress.setIndeterminate(true);

            if (DIALOGS_PROGRESS_SHOW_ON_EXCEPTION.get()) {
                getOwner().setVisible(true);
                setVisible(true);
            }
        }

        // Track state
        if (e.getState() == ProgressState.Start) {
            logArea.setText(null);
            logProgress.setValue(0);

            if (DIALOGS_PROGRESS_AUTOSHOW.get()) {
                WindowsUtils.center(this);
                setVisible(true);
            }
        }

        if (e.getState() == ProgressState.Stop && DIALOGS_PROGRESS_AUTOHIDE.get()) {
            setVisible(false);
        }

        if (e.getValue() != null) {
            if (e.isPercents()) {
                logProgress.setValue(e.getValue());
                logProgress.setIndeterminate(false);
                logProgress.setString(null);
            } else {
                logProgress.setIndeterminate(true);
                logProgress.setString(SynchronizationUtils.makeSizeString(e));
            }
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
