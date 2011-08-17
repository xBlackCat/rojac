package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.executor.TaskType;
import org.xblackcat.rojac.service.executor.TaskTypeEnum;

import javax.swing.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @author xBlackCat
 */
@TaskType(TaskTypeEnum.Background)
class LoadTaskExecutor implements Runnable {
    private final Collection<ALoadTask> tasks = new LinkedList<>();

    LoadTaskExecutor(ALoadTask[]... otherTasks) {
        for (ALoadTask[] tasks : otherTasks) {
            this.tasks.addAll(Arrays.asList(tasks));
        }
    }

    @SuppressWarnings({"unchecked"})
    public void run() {
        for (final ALoadTask task : tasks) {
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
