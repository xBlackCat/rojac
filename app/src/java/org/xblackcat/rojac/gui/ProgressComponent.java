package org.xblackcat.rojac.gui;

import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.progress.IProgressListener;
import org.xblackcat.rojac.service.progress.ProgressChangeEvent;
import org.xblackcat.rojac.service.progress.ProgressState;
import org.xblackcat.rojac.util.SynchronizationUtils;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;

/**
 * @author xBlackCat
 */
class ProgressComponent extends JPanel implements IProgressListener {
    private final Window trackerDialog;
    private final JProgressBar bar = new JProgressBar(0, 100);
    private ProgressState lastState;

    public ProgressComponent(Window dialog) {
        super(new BorderLayout(5, 0));

        setBorder(new EmptyBorder(2, 2, 2, 2));

        bar.setStringPainted(true);

        add(bar, BorderLayout.CENTER);
        setVisible(false);

        setMaximumSize(new Dimension(60, 60));

        this.trackerDialog = dialog;

        dialog.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
                setVisible(false);
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                if (lastState == ProgressState.Start ||
                        lastState == ProgressState.Work) {
                    setVisible(true);
                }
            }
        });
        bar.addMouseListener(new PopupMouseAdapter() {
            @Override
            protected void triggerDoubleClick(MouseEvent e) {
                trackerDialog.setVisible(true);
            }

            @Override
            protected void triggerPopup(MouseEvent e) {

            }
        });
    }

    @Override
    public void progressChanged(ProgressChangeEvent e) {
        lastState = e.getState();
        if (lastState == ProgressState.Exception) {
            setVisible(false);
        }

        // Track state
        if (e.getState() == ProgressState.Start) {
            bar.setValue(0);
            if (!trackerDialog.isVisible() && !Property.DIALOGS_PROGRESS_AUTOSHOW.get()) {
                WindowsUtils.center(trackerDialog);
                setVisible(true);
            }
        }

        if (e.getState() == ProgressState.Stop) {
            setVisible(false);
        }

        if (e.getProgress() != null) {
            if (e.isPercents()) {
                bar.setValue(e.getProgress());
                bar.setIndeterminate(false);
                bar.setString(null);
            } else {
                bar.setIndeterminate(true);
                bar.setString(SynchronizationUtils.makeSizeString(e));
            }
        }

        if (e.getText() != null) {
            bar.setToolTipText(e.getText());
        }
    }

    public void setOrientation(int orientation) {
        bar.setOrientation(orientation);
    }
}
