package org.xblackcat.rojac.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

/**
 * @author xBlackCat
 */

public final class ClipboardUtils {
    private static final Log log = LogFactory.getLog(ClipboardUtils.class);

    private ClipboardUtils() {
    }

    public static void copyToClipboard(String url) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        final StringSelection contents = new StringSelection(url);
        clipboard.setContents(contents, contents);
    }

    public static String getStringFromClipboard() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText = (contents != null) &&
                contents.isDataFlavorSupported(DataFlavor.stringFlavor);

        if (hasTransferableText) {
            try {
                return (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException ex) {
                log.fatal("Standard flavor is unsupported. Check environment in your window - probably armageddon is close by.", ex);
            } catch (IOException ex) {
                log.warn("Can not get a content from the system clipboard.", ex);
            }
        }

        return null;
    }
}
