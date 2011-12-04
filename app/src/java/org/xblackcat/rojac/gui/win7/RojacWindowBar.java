package org.xblackcat.rojac.gui.win7;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bridj.Platform;
import org.bridj.Pointer;
import org.bridj.cpp.com.COMRuntime;
import org.bridj.cpp.com.shell.ITaskbarList3;
import org.bridj.jawt.JAWTUtils;
import org.xblackcat.rojac.gui.MainFrame;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.progress.IProgressListener;
import org.xblackcat.rojac.service.progress.ProgressChangeEvent;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author xBlackCat
 */
public class RojacWindowBar {
    private static final Log log = LogFactory.getLog(RojacWindowBar.class);

    private final boolean supported;
    private final ITaskbarList3 list;
    private Pointer<Integer> hwnd;

    public RojacWindowBar() {
        supported = Platform.isWindows7();

        ITaskbarList3 listBar = null;
        if (supported) {
            try {
                listBar = COMRuntime.newInstance(ITaskbarList3.class);
            } catch (ClassNotFoundException e) {
                log.warn("Can not access to Windows bar", e);
            }
        }

        this.list = listBar;
    }

    public boolean isSupported() {
        return supported;
    }

    public void installBar(final MainFrame mainFrame) {
        if (!isSupported()) {
            throw new IllegalStateException("Windows 7 task bar is not supported");
        }

        mainFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                long peerHandle = JAWTUtils.getNativePeerHandle(mainFrame);
                hwnd = Pointer.pointerToAddress(peerHandle, Integer.class, null);
            }
        });

        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (list != null) {
                    list.Release();
                }
            }
        });

        ServiceFactory.getInstance().getProgressControl().addProgressListener(new IProgressListener() {
            @Override
            public void progressChanged(ProgressChangeEvent e) {
                switch (e.getState()) {
                    case Idle:
                        list.SetProgressState(hwnd, ITaskbarList3.TbpFlag.TBPF_NOPROGRESS);
                        break;
                    case Start:
                        break;
                    case Exception:
                        list.SetProgressState(hwnd, ITaskbarList3.TbpFlag.TBPF_ERROR);
                        list.SetProgressValue(hwnd, 1, 1);
                        break;
                    case Work:
                        if (e.getPercents() != null) {
                            list.SetProgressState(hwnd, ITaskbarList3.TbpFlag.TBPF_NORMAL);
                            list.SetProgressValue(hwnd, e.getPercents(), 100);
                        } else {
                            list.SetProgressState(hwnd, ITaskbarList3.TbpFlag.TBPF_INDETERMINATE);
                        }
                        break;
                    case Stop:
                        list.SetProgressValue(hwnd, 1, 1);

                        mainFrame.addWindowFocusListener(new WindowAdapter() {
                            @Override
                            public void windowGainedFocus(WindowEvent e) {
                                list.SetProgressState(hwnd, ITaskbarList3.TbpFlag.TBPF_NOPROGRESS);
                                mainFrame.removeWindowFocusListener(this);
                            }
                        });
                        break;
                }
            }
        });

    }

}
