package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.executor.TaskType;
import org.xblackcat.rojac.service.executor.TaskTypeEnum;

import javax.swing.*;

/**
 * @author xBlackCat
 */
@TaskType(TaskTypeEnum.Background)
class LoadTaskExecutor implements Runnable {
    private final ILoadTask[] tasks;

    LoadTaskExecutor(ILoadTask... tasks) {
        this.tasks = tasks;
    }

    @SuppressWarnings({"unchecked"})
    public void run() {
        for (final ILoadTask task : tasks) {
            try {
                final Object result = task.doBackground();

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        task.doSwing(result);
                    }
                });
            } catch (Exception e) {
                // TODO: do something
            }
        }
    }

    public final void execute() {
        ServiceFactory.getInstance().getExecutor().execute(this);
    }
}
