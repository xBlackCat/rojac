package org.xblackcat.rojac.util;

import chrriis.dj.nativeswing.swtimpl.NSPanelComponent;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 19.03.12 16:00
 *
 * @author xBlackCat
 */
public class SWTUtils {
    private static final Log log = LogFactory.getLog(SWTUtils.class);

    public static final boolean isSwtEnabled;
    private static JWebBrowser webBrowser;

    static {
        try {
            Class.forName("org.eclipse.swt.SWT");
        } catch (ClassNotFoundException e) {
            // Try to load SWT library

            prepareSWT();
        }
        boolean swtFound = false;
        try {
            Class.forName("org.eclipse.swt.SWT");
            swtFound = true;
        } catch (ClassNotFoundException e) {
            // No SWT
        }

        isSwtEnabled = swtFound;
    }

    public static JWebBrowser getBrowser() {
        assert RojacUtils.checkThread(true);

        if (webBrowser == null) {
            // Check and load correspond SWT library

            webBrowser = new JWebBrowser(NSPanelComponent.destroyOnFinalization());
            webBrowser.setStatusBarVisible(false);
            webBrowser.setMenuBarVisible(false);
            webBrowser.setJavascriptEnabled(true);
            webBrowser.setBarsVisible(false);
            webBrowser.setDefaultPopupMenuRegistered(false);
            webBrowser.setLocationBarVisible(false);
            webBrowser.setButtonBarVisible(false);
            webBrowser.setFocusable(false);
            webBrowser.setRequestFocusEnabled(false);
            webBrowser.getNativeComponent().setFocusable(false);

        }
        return webBrowser;
    }

    private static void prepareSWT() {
        try {
            URLClassLoader classLoader = (URLClassLoader) SWTUtils.class.getClassLoader();

            // Search for rojac.jar
            for (URL url : classLoader.getURLs()) {
                if (url.getFile().contains("DJNativeSwing-SWT.jar")) {
                    Method addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                    addUrlMethod.setAccessible(true);

                    String os;
                    if (SystemUtils.IS_OS_LINUX) {
                        os = "linux";
                    } else if (SystemUtils.IS_OS_WINDOWS) {
                        os = "win32";
                    } else if (SystemUtils.IS_OS_MAC_OSX) {
                        os = "macosx";
                    } else {
                        os = "";
                    }

                    String bit = SystemUtils.OS_ARCH.contains("64") ? "x64" : "x86";

                    String swtLibrary = "swt/swt-" + os + "-" + bit + ".jar";
                    URL swtLibUrl = new URL(url.toExternalForm().replaceFirst("axis\\.jar", swtLibrary));
                    addUrlMethod.invoke(classLoader, swtLibUrl);
                    break;
                }
            }
        } catch (MalformedURLException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            log.error("Can not initialize SWT", e);
        }
    }
}
