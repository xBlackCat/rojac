package org.xblackcat.rojac.gui.dialogs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.painter.BusyPainter;
import org.xblackcat.rojac.service.progress.IProgressListener;
import org.xblackcat.rojac.service.progress.ProgressChangeEvent;
import org.xblackcat.rojac.service.progress.ProgressState;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import static org.xblackcat.rojac.service.options.Property.DIALOGS_PROGRESS_AUTOHIDE;
import static org.xblackcat.rojac.service.options.Property.DIALOGS_PROGRESS_AUTOSHOW;

/**
 * @author xBlackCat
 */

// TODO: implement progress control based on JXBusyLabel.
public class ProgressTrackerDialog extends JDialog implements IProgressListener {
    private static final Log log = LogFactory.getLog(ProgressTrackerDialog.class);

    private JTextArea logArea = new JTextArea();
    protected BusyPainter busyPainter;
    protected JXBusyLabel logProgress;

    public ProgressTrackerDialog(Window mainFrame) {
        super(mainFrame, ModalityType.MODELESS);
        JPanel cp = new JPanel(new BorderLayout());

        cp.add(new JScrollPane(logArea), BorderLayout.CENTER);
        logProgress = new JXBusyLabel(new Dimension(100, 100));
        cp.add(logProgress, BorderLayout.EAST);

        setContentPane(cp);

        logProgress.setDirection(JXBusyLabel.Direction.RIGHT);

        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        setSize(200, 100);
        busyPainter = logProgress.getBusyPainter();

        busyPainter.setPoints(50);
        busyPainter.setPaintCentered(false);
        busyPainter.setFrame(0);
        busyPainter.setTrailLength(10);
        busyPainter.setPointShape(new Rectangle2D.Float(0, 0, 17.5f, 1));
        busyPainter.setTrajectory(new Ellipse2D.Float(7.5f, 7.5f, 36.0f, 36.0f));
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
            busyPainter.setFrame((int) (e.getProgress() * busyPainter.getPoints()));
            logProgress.repaint();
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
