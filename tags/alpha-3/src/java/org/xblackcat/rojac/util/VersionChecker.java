package org.xblackcat.rojac.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.executor.TaskType;
import org.xblackcat.rojac.service.executor.TaskTypeEnum;

import javax.swing.*;
import java.awt.*;

/**
 * @author xBlackCat
 */
@TaskType(TaskTypeEnum.Background)
public class VersionChecker extends RojacWorker<Void, Integer> {
    private static final Log log = LogFactory.getLog(VersionChecker.class);

    private final Window mainFrame;
    private final boolean manual;

    public VersionChecker(Window mainFrame) {
        this(mainFrame, false);
    }

    public VersionChecker(Window mainFrame, boolean manual) {
        this.mainFrame = mainFrame;
        this.manual = manual;
    }

    @Override
    protected Void perform() throws Exception {
        try {
            Integer lastBuild = RojacUtils.getLastBuild();

            if (lastBuild != null) {
                if (log.isDebugEnabled()) {
                    log.debug("Revision on site: " + lastBuild + ". Current revision: " + RojacUtils.REVISION_NUMBER);
                }

                publish(lastBuild);
            }
        } catch (RojacException e) {
            if (log.isWarnEnabled()) {
                log.warn("Can not read available last revision from site.", e);
            }
            publish();
        }

        return null;
    }

    @Override
    protected void process(java.util.List<Integer> chunks) {
        if (chunks.isEmpty()) {
            // Error is occurs
        } else {
            int lastBuild = chunks.iterator().next();

            if (RojacUtils.REVISION_NUMBER != null && RojacUtils.REVISION_NUMBER < lastBuild) {
                if (log.isDebugEnabled()) {
                    log.debug("User should update Rojac dist up to latest version.");
                }

                JLOptionPane.showMessageDialog(mainFrame, Messages.Dialog_Updater_UpdateExists.get(lastBuild), Messages.Dialog_Updater_UpdateExists_Title.get(), JOptionPane.INFORMATION_MESSAGE);
            } else if (manual) {
                JLOptionPane.showMessageDialog(mainFrame, Messages.Dialog_Updater_NoUpdate.get(), Messages.Dialog_Updater_NoUpdate_Title.get(), JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
