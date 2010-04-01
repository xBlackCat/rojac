package org.xblackcat.rojac.gui.tray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.IDataHandler;
import org.xblackcat.rojac.service.datahandler.ProcessPacket;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.progress.IProgressListener;
import org.xblackcat.rojac.service.progress.ProgressChangeEvent;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.util.RojacWorker;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * @author xBlackCat
 */

public class RojacTray {
    private static final Log log = LogFactory.getLog(RojacTray.class);

    private final boolean supported;
    private RojacState state = RojacState.Initialized;
    protected final TrayIcon trayIcon;
    private final Window mainFrame;

    public RojacTray(Window mainFrame) {
        this.mainFrame = mainFrame;
        boolean supported = SystemTray.isSupported();
        trayIcon = new TrayIcon(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB));

        if (supported) {
            try {
                SystemTray.getSystemTray().add(trayIcon);
            } catch (AWTException e) {
                log.error("Can not add tray icon", e);
                supported = false;
            }
        }

        this.supported = supported;

        if (supported) {
            setupTray();
        }
    }

    private void setupTray() {
        setState(RojacState.Initialized);

        ServiceFactory.getInstance().getProgressControl().addProgressListener(new TrayProgressListener());
        ServiceFactory.getInstance().getDataDispatcher().addDataHandler(new TrayDataDispatcher());

        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });
    }

    protected void setState(RojacState state, Object... arguments) {
        trayIcon.setImage(state.getImage());
        trayIcon.setToolTip(state.getToolTip(arguments));
    }

    public boolean isSupported() {
        return supported;
    }

    private void checkUnreadMessages() {
        RojacWorker<Void, Integer> rw = new UnreadMessagesCount();
        ServiceFactory.getInstance().getExecutor().execute(rw);
    }

    private class UnreadMessagesCount extends RojacWorker<Void, Integer> {
        protected int unreadMessages;
        protected int unreadReplies;

        @Override
        protected Void perform() throws Exception {
            IMessageAH mAH = ServiceFactory.getInstance().getStorage().getMessageAH();

            unreadMessages = mAH.getUnreadMessages();
            Integer userId = Property.RSDN_USER_ID.get();
            if (userId != null && userId > 0) {
                unreadReplies = mAH.getUnreadReplies(userId);
            }

            return null;
        }

        @Override
        protected void done() {
            if (unreadMessages > 0) {
                setState(RojacState.HaveUnreadMessages, unreadMessages, unreadReplies);
            }
        }
    }

    private class TrayProgressListener implements IProgressListener {
        @Override
        public void progressChanged(ProgressChangeEvent e) {
            switch (e.getState()) {
                case Idle:
                    setState(RojacState.Normal);
                    break;
                case Start:
                    break;
                case Work:
                    setState(RojacState.Synchronizing, e.getText(), e.getProgress());
                    break;
                case Stop:
                    setState(RojacState.Normal);

                    checkUnreadMessages();
                    break;
            }
        }
    }

    private class TrayDataDispatcher implements IDataHandler {
        @Override
        public void processPacket(ProcessPacket results) {
        }
    }
}
