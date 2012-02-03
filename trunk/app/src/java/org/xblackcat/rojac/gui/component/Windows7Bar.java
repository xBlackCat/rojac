package org.xblackcat.rojac.gui.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bridj.Platform;
import org.bridj.Pointer;
import org.bridj.cpp.com.COMRuntime;
import org.bridj.cpp.com.shell.ITaskbarList3;
import org.bridj.jawt.JAWTUtils;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xBlackCat
 */
public class Windows7Bar {
    private static final Log log = LogFactory.getLog(Windows7Bar.class);

    private static final ITaskbarList3 list;
    private static final Map<Window, Windows7Bar> taskBars = new ConcurrentHashMap<>();

    private Pointer<Integer> hwnd;

    static {
        ITaskbarList3 listBar = null;

        if (Platform.isWindows7()) {
            try {
                listBar = COMRuntime.newInstance(ITaskbarList3.class);
            } catch (Throwable e) {
                // No classes installed
                log.error("Can not initialize Windows 7 task bar", e);
            }
        }
        list = listBar;
    }

    public static boolean isSupported() {
        return list != null;
    }

    public synchronized static Windows7Bar getWindowBar(final Window window) throws IllegalStateException {
        if (!isSupported()) {
            return null;
        }
        
        if (taskBars.containsKey(window)) {
            return taskBars.get(window);
        }

        final Windows7Bar taskBar = new Windows7Bar();

        window.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                long peerHandle = JAWTUtils.getNativePeerHandle(window);
                taskBar.hwnd = Pointer.pointerToAddress(peerHandle, Integer.class, null);
            }
        });

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                taskBars.remove(window);

                if (taskBars.isEmpty() && list != null) {
                    list.Release();
                }
            }
        });

        taskBars.put(window, taskBar);

        return taskBar;
    }

    public void setState(ITaskbarList3.TbpFlag state) {
        if (list != null && hwnd != null) {
            list.SetProgressState(hwnd, state);
        }
    }

    public void setProgress(int completed, int total) {
        if (list != null && hwnd != null) {
            list.SetProgressValue(hwnd, completed, total);
        }
    }
}
