package org.xblackcat.rojac.gui.view.navigation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * @author xBlackCat
 */
class LoadTaskExecutor extends RojacWorker<Void, LoadTaskExecutor.TaskResult<?>> {
    private static final Log log = LogFactory.getLog(LoadTaskExecutor.class);

    private final Collection<ILoadTask> tasks = new LinkedList<>();

    @SafeVarargs
    LoadTaskExecutor(Collection<? extends ILoadTask>... otherTasks) {
        for (Collection<? extends ILoadTask> tasks : otherTasks) {
            this.tasks.addAll(tasks);
        }
    }

    public Void perform() {
        ForkJoinPool pool = new ForkJoinPool();
        for (final ILoadTask task : tasks) {
            pool.execute(new ForkJoinTask<Object>() {
                @Override
                public Object getRawResult() {
                    return null;
                }

                @Override
                protected void setRawResult(Object value) {
                }

                @Override
                protected boolean exec() {
                    try {
                        final Object result = task.doBackground();

                        @SuppressWarnings({"unchecked"})
                        TaskResult taskResult = new TaskResult(task, result);

                        publish(taskResult);

                        return true;
                    } catch (Exception e) {
                        log.error("Can not perform load task", e);
                        return false;
                    }
                }
            });
        }

        return null;
    }

    @Override
    protected void process(List<TaskResult<?>> chunks) {
        for (TaskResult<?> tr : chunks) {
            tr.doSwing();
        }
    }

    protected static final class TaskResult<T> {
        final ILoadTask<T> task;
        final T result;

        private TaskResult(ILoadTask<T> task, T result) {
            this.result = result;
            this.task = task;
        }

        void doSwing() {
            task.doSwing(result);
        }
    }
}
