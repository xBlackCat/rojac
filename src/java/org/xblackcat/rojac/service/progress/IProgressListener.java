package org.xblackcat.rojac.service.progress;

import java.util.EventListener;

/**
 * Implement the interface to listen any progress changes to show them.
 *
 * @author xBlackCat
 */

public interface IProgressListener extends EventListener {
    void progressChanged(ProgressChangeEvent e);
}
