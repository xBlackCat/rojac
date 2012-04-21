package org.xblackcat.rojac.gui.hint;

import org.xblackcat.rojac.gui.theme.HintIcon;
import org.xblackcat.rojac.util.BalloonTipUtils;

import javax.swing.*;
import java.awt.*;
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
        final Timer timer = new Timer(5000, null);
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

        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeHint(info);
            }
        });

        timer.setRepeats(false);
        timer.start();

        hints.put(info, hintComp);
        add(hintComp);
        revalidate();

        return info;
    }

    public void removeHint(HintInfo hintInfo) {
        hintInfo.stopTimer();
        JComponent component = hints.remove(hintInfo);

        remove(component);
        revalidate();
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

    /**
     * 21.04.12 13:16
     *
     * @author xBlackCat
     */
    public static class MessageHint extends JPanel {
        public MessageHint(Icon icon, JComponent comp, Runnable onClose) {
            super(new BorderLayout(5, 0));

            Color color = new Color(0xFFFFCC);
            setBackground(color);
            setAlignmentX(0);

            final JLabel iconLabel = new JLabel(icon);
            add(iconLabel, BorderLayout.WEST);

            add(comp, BorderLayout.CENTER);

            add(BalloonTipUtils.balloonTipCloseButton(onClose), BorderLayout.EAST);
        }
    }
}
