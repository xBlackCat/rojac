package org.xblackcat.rojac.gui.tray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.RojacHelper;
import org.xblackcat.rojac.util.RojacUtils;

import java.awt.*;

/**
 * Date: 22 ρεπο 2009
 *
 * @author xBlackCat
 */

public class RojacTray {
    private static final Log log = LogFactory.getLog(RojacTray.class);

    private final boolean supported;
    private RojacState state = RojacState.Initialized;
    protected final TrayIcon trayIcon;

    public RojacTray() {
        boolean supported = SystemTray.isSupported();
        trayIcon = new TrayIcon(RojacHelper.loadImage(""), RojacUtils.VERSION_STRING);

        if (supported) {
            try {
                SystemTray.getSystemTray().add(trayIcon);
            } catch (AWTException e) {
                log.error("Can not add tray icon", e);
                supported = false;
            }
        }

        this.supported = supported;
    }

    private void updateTray() {

    }
}
