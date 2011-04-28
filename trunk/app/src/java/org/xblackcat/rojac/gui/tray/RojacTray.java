package org.xblackcat.rojac.gui.tray;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.FavoriteStatData;
import org.xblackcat.rojac.i18n.Messages;
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
import java.awt.event.*;
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

        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    toggleFrameVisibility();
                }
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
        updateState();
    }

    protected void setState(RojacState state, Object... arguments) {
        if (this.state != state) {
            this.state = state;
            trayIcon.setImage(state.getImage());
            updatePopUpMenu(state);
        }
        trayIcon.setToolTip(state.getToolTip(arguments));
    }

    private void updatePopUpMenu(final RojacState state) {
        PopupMenu menu = new PopupMenu();
        menu.setFont(UIManager.getFont("Label.font"));

        MenuItem showHideItem = new MenuItem();
        Messages message = mainFrame.isVisible() ? Messages.Tray_Popup_Item_HideMainframe : Messages.Tray_Popup_Item_ShowMainframe;
        showHideItem.setLabel(message.get());
        showHideItem.setFont(menu.getFont().deriveFont(Font.BOLD));
        showHideItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleFrameVisibility();
            }
        });
        menu.add(showHideItem);

        menu.addSeparator();

        MenuItem optionsItem = new MenuItem(Messages.Tray_Popup_Item_Options.get());
        optionsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogHelper.showOptionsDialog(mainFrame);
            }
        });
        menu.add(optionsItem);

        MenuItem aboutItem = new MenuItem(Messages.Tray_Popup_Item_About.get());
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogHelper.showAboutDialog(mainFrame);
            }
        });
        menu.add(aboutItem);

        menu.addSeparator();

        MenuItem exitItem = new MenuItem(Messages.Tray_Popup_Item_Exit.get());
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowEvent event = new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING);
                event.setSource(RojacTray.this);
                mainFrame.dispatchEvent(event);
            }
        });
        menu.add(exitItem);

        trayIcon.setPopupMenu(menu);
    }

    public boolean isSupported() {
        return supported;
    }

    private void checkUnreadMessages() {
        new UnreadMessagesCountGetter().execute();
    }

    public void updateState() {
        if (supported) {
            // Recheck update menu.
            updatePopUpMenu(state);
        }
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
                                    Messages.Tray_Balloon_SynchronizationComplete_Title.get(),
                                    Messages.Tray_Balloon_SynchronizationComplete_Text.get(
                                            p.getForumIds().length,
                                            p.getThreadIds().length,
                                            p.getMessageIds().length
                                    ),
                                    TrayIcon.MessageType.INFO
                            );
                        }
                    }
                }
        );

        @Override
        public void processPacket(IPacket packet) {
            dispatcher.dispatch(packet);
        }
    }
}
