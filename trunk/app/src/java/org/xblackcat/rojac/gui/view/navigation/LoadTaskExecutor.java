package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.executor.TaskType;
import org.xblackcat.rojac.service.executor.TaskTypeEnum;

import javax.swing.*;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @author xBlackCat
 */
@TaskType(TaskTypeEnum.DataLoading)
class LoadTaskExecutor implements Runnable {
    private final Collection<ALoadTask> tasks = new LinkedList<>();

    @SafeVarargs
    LoadTaskExecutor(Collection<? extends ALoadTask>... otherTasks) {
        for (Collection<? extends ALoadTask> tasks : otherTasks) {
            this.tasks.addAll(tasks);
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
