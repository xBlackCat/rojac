package org.xblackcat.rojac.gui.hint;

import org.xblackcat.rojac.gui.theme.HintIcon;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 21.04.12 14:29
 *
 * @author xBlackCat
 */
public class HintContainer extends JPanel {
    public static final int DEFAULT_TIMEOUT = 5000;

    private final Map<HintInfo, JComponent> hints = new HashMap<>();

    public HintContainer() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public HintInfo addHint(JComponent hint) {
        return addHint(HintIcon.Info, hint);
    }

    public HintInfo addHint(Icon hintIcon, JComponent hint) {
        return addHint(hintIcon, hint, null);
    }

    public HintInfo addHint(Icon hintIcon, JComponent hint, final Runnable onClose) {
        return addHint(hintIcon, hint, onClose, DEFAULT_TIMEOUT);
    }

    public HintInfo addHint(Icon hintIcon, JComponent hint, final Runnable onClose, int timeout) {
        final Timer timer;
        if (timeout > 0) {
            timer = new Timer(timeout, null);
        } else {
            timer = null;
        }
        final HintInfo info = new HintInfo(timer);

        final Runnable closeTarget = new Runnable() {
            @Override
            public void run() {
                removeHint(info);
                if (onClose != null) {
                    onClose.run();
                }
            }
        };
        MessageHint hintComp = new MessageHint(hintIcon, hint, closeTarget);

        if (timer != null) {
            timer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removeHint(info);
                }
            });

            timer.setRepeats(false);
            timer.start();
        }

        hints.put(info, hintComp);
        add(hintComp);
        revalidate();

        return info;
    }

    public void removeHint(HintInfo hintInfo) {
        hintInfo.stopTimer();
        JComponent component = hints.remove(hintInfo);

        if (component != null) {
            // If not already removed
            remove(component);
            revalidate();
        }
    }

    // Remove all shown hints
    public void clearHints() {
        Iterator<HintInfo> i = hints.keySet().iterator();
        while (i.hasNext()) {
            final HintInfo hintInfo = i.next();
            hintInfo.stopTimer();
            i.remove();
        }

        removeAll();

        revalidate();
    }

}
