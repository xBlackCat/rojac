package org.xblackcat.rojac.service.storage;

import org.bridj.cpp.com.shell.ITaskbarList3;
import org.xblackcat.rojac.gui.component.Windows7Bar;
import org.xblackcat.rojac.gui.dialog.db.CheckProcessDialog;
import org.xblackcat.rojac.service.executor.TaskType;
import org.xblackcat.rojac.service.executor.TaskTypeEnum;
import org.xblackcat.rojac.service.progress.ProgressChangeEvent;
import org.xblackcat.rojac.service.progress.ProgressState;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.RojacWorker;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 04.04.12 16:53
 *
 * @author xBlackCat
 */
@TaskType(TaskTypeEnum.Background)
public abstract class DatabaseWorker extends RojacWorker<Void, ProgressChangeEvent> {
    protected final CheckProcessDialog processDialog;
    protected final Window owner;
    protected final Windows7Bar win7bar;

    protected DatabaseWorker(Runnable postProcessor, Window owner, CheckProcessDialog processDialog) {
        super(postProcessor);
        assert RojacUtils.checkThread(true) : "Database worker should be created in EventDispatcher thread";

        this.processDialog = processDialog;
        this.win7bar = Windows7Bar.getWindowBar(owner);
        this.owner = owner;
    }

    @Override
    protected final Void perform() throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                WindowsUtils.center(processDialog);
                processDialog.setVisible(true);
            }
        });

        if (doWork()) {
            publish(new ProgressChangeEvent(this, ProgressState.Stop));
        }

        return null;
    }

    @Override
    protected final void process(List<ProgressChangeEvent> chunks) {
        for (ProgressChangeEvent e : chunks) {
            if (e.getState() == ProgressState.Stop) {
                processDialog.dispose();
                if (win7bar != null) {
                    win7bar.setState(ITaskbarList3.TbpFlag.TBPF_NOPROGRESS);
                }

                onSuccess();
            } else if (e.getValue() != null) {
                if (e.getBound() != null) {
                    processDialog.setProgress(e.getValue(), e.getBound());
                    if (win7bar != null) {
                        win7bar.setState(ITaskbarList3.TbpFlag.TBPF_PAUSED);
                        win7bar.setProgress(e.getValue(), e.getBound());
                    }
                } else {
                    processDialog.setIndeterminate();
                    if (win7bar != null) {
                        win7bar.setState(ITaskbarList3.TbpFlag.TBPF_INDETERMINATE);
                    }
                }
            }
        }
    }

    protected abstract boolean doWork() throws StorageException;

    protected abstract void onSuccess();
}
