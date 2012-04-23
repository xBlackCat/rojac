package org.xblackcat.rojac;

import org.bridj.cpp.com.shell.ITaskbarList3;
import org.xblackcat.rojac.data.ReadStatistic;
import org.xblackcat.rojac.gui.MainFrame;
import org.xblackcat.rojac.gui.component.Windows7Bar;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.progress.IProgressListener;
import org.xblackcat.rojac.service.progress.ProgressChangeEvent;
import org.xblackcat.rojac.service.storage.IStatisticAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.RojacWorker;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author xBlackCat
 */
class Windows7BarProgressTracker implements IProgressListener {
    private final MainFrame mainFrame;
    private final Windows7Bar taskBar;

    public Windows7BarProgressTracker(MainFrame mainFrame, Windows7Bar taskBar) {
        this.mainFrame = mainFrame;
        this.taskBar = taskBar;
    }

    @Override
    public void progressChanged(ProgressChangeEvent e) {
        switch (e.getState()) {
            case Idle:
                taskBar.setState(ITaskbarList3.TbpFlag.TBPF_NOPROGRESS);
                break;
            case Start:
                break;
            case Exception:
                taskBar.setState(ITaskbarList3.TbpFlag.TBPF_ERROR);
                taskBar.setProgress(1, 1);
                break;
            case Work:
                if (e.getPercents() != null) {
                    taskBar.setState(ITaskbarList3.TbpFlag.TBPF_NORMAL);
                    taskBar.setProgress(e.getPercents(), 100);
                } else {
                    taskBar.setState(ITaskbarList3.TbpFlag.TBPF_INDETERMINATE);
                }
                break;
            case Stop:
                taskBar.setState(ITaskbarList3.TbpFlag.TBPF_NOPROGRESS);

                // To hide errors and other notifications
                installProgressCleaner();

                new RojacWorker<Void, Integer>() {
                    @Override
                    protected Void perform() throws Exception {
                        int ownId = Property.RSDN_USER_ID.get();

                        ReadStatistic userRepliesStat = Storage.get(IStatisticAH.class).getUserRepliesStat(ownId);
                        publish(userRepliesStat.getUnreadMessages());

                        return null;
                    }
                }.execute();
                break;
        }
    }

    private void installProgressCleaner() {
        mainFrame.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                taskBar.setState(ITaskbarList3.TbpFlag.TBPF_NOPROGRESS);
                mainFrame.removeWindowFocusListener(this);
            }
        });
    }
}
