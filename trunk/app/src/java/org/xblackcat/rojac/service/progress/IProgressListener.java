package org.xblackcat.rojac.service.progress;

import java.util.EventListener;

/**
 * Implement the interface to listen any progress changes to show them.
 *
 * @author xBlackCat
 */

public interface IProgressListener extends EventListener {
    void stateChanged(ProgressState state, String description);

    void progressChanged(float percentage, String description);
}
