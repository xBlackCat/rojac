package org.xblackcat.rojac.gui.tray;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.FavoriteStatData;
import org.xblackcat.rojac.gui.PopupMouseAdapter;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.*;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.progress.IProgressListener;
import org.xblackcat.rojac.service.progress.ProgressChangeEvent;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.util.DialogHelper;
import org.xblackcat.rojac.util.RojacWorker;

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

public class RojacTray {
    private static final Log log = LogFactory.getLog(RojacTray.class);

    private final boolean supported;
    private RojacState state = RojacState.Initialized;
    protected final TrayIcon trayIcon;
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
        trayIcon.setImage(RojacState.Initialized.getImage());
        trayIcon.setToolTip(RojacState.Initialized.getToolTip());

        ServiceFactory.getInstance().getProgressControl().addProgressListener(new TrayProgressListener());
        ServiceFactory.getInstance().getDataDispatcher().addDataHandler(new TrayDataDispatcher());

        trayIcon.addMouseListener(new PopupMouseAdapter() {
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
        });
    }

    private void toggleFrameVisibility() {
        if (mainFrame.isVisible()) {
            mainFrame.setState(Frame.ICONIFIED);
            mainFrame.setVisible(false);
        } else {
            mainFrame.setVisible(true);
            mainFrame.setState(Frame.NORMAL);
            mainFrame.toFront();
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

    private void checkUnreadMessages() {
        new UnreadMessagesCountGetter().execute();
    }

    private class UnreadMessagesCountGetter extends RojacWorker<Void, Integer> {
        protected int unreadMessages;
        protected FavoriteStatData unreadReplies;

        @Override
        protected Void perform() throws Exception {
            IMessageAH mAH = ServiceFactory.getInstance().getStorage().getMessageAH();

            unreadMessages = mAH.getUnreadMessages();
            Integer userId = Property.RSDN_USER_ID.get();
            if (userId != null && userId > 0) {
                unreadReplies = mAH.getUserRepliesStat(userId);
            }

            return null;
        }

        @Override
        protected void done() {
            if (unreadMessages > 0) {
                setState(RojacState.HaveUnreadMessages, unreadMessages, unreadReplies.asString());
            }
        }
    }

    private class TrayProgressListener implements IProgressListener {
        protected String lastText;

        @Override
        public void progressChanged(ProgressChangeEvent e) {
            switch (e.getState()) {
                case Idle:
                    setState(RojacState.Normal);
                    break;
                case Start:
                    break;
                case Work:
                    if (StringUtils.isNotBlank(e.getText())) {
                        lastText = e.getText();
                    }
                    String progress;
                    if (e.getProgress() != null && e.getProgress() >= 0) {
                        progress = "(" + e.getProgress() + "%)";
                    } else {
                        progress = "";
                    }
                    setState(RojacState.Synchronizing, lastText, progress);
                    break;
                case Stop:
                    setState(RojacState.Normal);

                    checkUnreadMessages();
                    break;
            }
        }
    }

    private class TrayDataDispatcher implements IDataHandler {
        private final PacketDispatcher dispatcher = new PacketDispatcher(
                new IPacketProcessor<SynchronizationCompletePacket>() {
                    @Override
                    public void process(SynchronizationCompletePacket p) {
                        if (Property.TRAY_NOTIFICATION_SYNC_COMPLETE.get()) {
                            trayIcon.displayMessage(
                                    Message.Tray_Balloon_SynchronizationComplete_Title.get(),
                                    Message.Tray_Balloon_SynchronizationComplete_Text.get(
                                            p.getForumIds().length,
                                            p.getThreadIds().length,
                                            p.getMessageIds().length
                                    ),
                                    TrayIcon.MessageType.INFO
                            );
                        }
                    }
                },
                new IPacketProcessor<IPacket>() {
                    @Override
                    public void process(IPacket p) {
                        checkUnreadMessages();
                    }
                }
        );

        @Override
        public void processPacket(IPacket packet) {
            dispatcher.dispatch(packet);
        }
    }
}
