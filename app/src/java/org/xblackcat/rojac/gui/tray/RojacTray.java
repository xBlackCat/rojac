package org.xblackcat.rojac.gui.tray;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.DiscussionStatistic;
import org.xblackcat.rojac.gui.PopupMouseAdapter;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.progress.IProgressListener;
import org.xblackcat.rojac.service.progress.ProgressChangeEvent;
import org.xblackcat.rojac.util.DialogHelper;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

/**
 * @author xBlackCat
 */

public class RojacTray implements IStatisticListener {
    private static final Log log = LogFactory.getLog(RojacTray.class);

    private final boolean supported;
    private RojacState state = RojacState.Initialization;
    private final TrayIcon trayIcon;
    private final JFrame mainFrame;

    public RojacTray(JFrame mainFrame) {
        this.mainFrame = mainFrame;
        boolean supported = SystemTray.isSupported();

        if (supported) {
            trayIcon = new TrayIcon(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB));
            try {
                SystemTray.getSystemTray().add(trayIcon);
            } catch (AWTException e) {
                log.error("Can not add tray icon", e);
                supported = false;
            }
        } else {
            trayIcon = null;
        }

        this.supported = supported;

        if (supported) {
            setupTray();
        }
    }

    private void setupTray() {
        trayIcon.setImage(RojacState.Initialization.getImage());
        trayIcon.setToolTip(RojacState.Initialization.getToolTip());

        ServiceFactory.getInstance().getProgressControl().addProgressListener(new TrayProgressListener());

        trayIcon.addMouseListener(new TrayListener());
        trayIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setVisible(true);
                mainFrame.setState(Frame.NORMAL);
                WindowsUtils.toFront(mainFrame);
            }
        });
    }

    private void toggleFrameVisibility() {
        if (mainFrame.isVisible()) {
            mainFrame.setState(Frame.ICONIFIED);
            mainFrame.setVisible(false);
        } else {
            mainFrame.setVisible(true);
            mainFrame.setState(Frame.NORMAL);
            WindowsUtils.toFront(mainFrame);
        }
    }

    protected void setState(RojacState state, Object... arguments) {
        if (this.state != state) {
            this.state = state;
            trayIcon.setImage(state.getImage());
        }
        trayIcon.setToolTip(state.getToolTip(arguments));
    }

    private JPopupMenu getPopupMenu() {
        JPopupMenu menu = new JPopupMenu();

        Message message = mainFrame.isVisible() ? Message.Tray_Popup_Item_HideMainframe : Message.Tray_Popup_Item_ShowMainframe;
        JMenuItem showHideItem = new JMenuItem(message.get());
        final Font font = menu.getFont();
        if (font != null) {
            showHideItem.setFont(font.deriveFont(Font.BOLD));
        }
        showHideItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleFrameVisibility();
            }
        });
        menu.add(showHideItem);

        menu.addSeparator();

        JMenuItem optionsItem = new JMenuItem(Message.Tray_Popup_Item_Options.get());
        optionsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogHelper.showOptionsDialog(mainFrame);
            }
        });
        menu.add(optionsItem);

        JMenuItem aboutItem = new JMenuItem(Message.Tray_Popup_Item_About.get());
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogHelper.showAboutDialog(mainFrame);
            }
        });
        menu.add(aboutItem);

        menu.addSeparator();

        JMenuItem exitItem = new JMenuItem(Message.Tray_Popup_Item_Exit.get());
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowEvent event = new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING);
                event.setSource(RojacTray.this);
                mainFrame.dispatchEvent(event);
            }
        });
        menu.add(exitItem);

        return menu;
    }

    public boolean isSupported() {
        return supported;
    }

    @Override
    public void setStatistic(DiscussionStatistic statistic) {
        RojacState state = RojacState.NoUnreadMessages;

        if (statistic.getUnreadReplies() > 0) {
            state = RojacState.HaveUnreadReplies;
        } else if (statistic.getUnreadMessages() > 0) {
            state = RojacState.HaveUnreadMessages;
        }

        setState(state, statistic.getUnreadMessages(), statistic.getUnreadReplies());
    }

    private class TrayProgressListener implements IProgressListener {
        protected String lastText;

        @Override
        public void progressChanged(ProgressChangeEvent e) {
            switch (e.getState()) {
                case Idle:
                    setState(RojacState.Initialization);
                    break;
                case Start:
                    break;
                case Work:
                    if (StringUtils.isNotBlank(e.getText())) {
                        lastText = e.getText();
                    }
                    String progress;
                    if (e.getValue() != null && e.getValue() >= 0) {
                        progress = "(" + e.getValue() + "%)";
                    } else {
                        progress = "";
                    }
                    setState(RojacState.Synchronizing, lastText, progress);
                    break;
                case Stop:
                    setState(RojacState.Initialization);

                    setStatistic(DiscussionStatistic.NO_STAT);
                    break;
            }
        }
    }

    private class TrayListener extends PopupMouseAdapter {
        @Override
        protected void triggerDoubleClick(MouseEvent e) {
            toggleFrameVisibility();
        }

        @Override
        protected void triggerPopup(MouseEvent e) {
            Point p = e.getPoint();

            JPopupMenu m = getPopupMenu();
            m.setLocation(p);
            m.setInvoker(m);
            m.setVisible(true);
        }

        @Override
        protected void triggerClick(MouseEvent e) {
            if (mainFrame.isVisible()) {
                WindowsUtils.toFront(mainFrame);
            }
        }
    }
}
