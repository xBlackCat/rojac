package org.xblackcat.rojac.service.executor;

import org.xblackcat.rojac.service.progress.IProgressController;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Service for executing tasks in separate thread.
 *
 * @author xBlackCat
 */

public final class TaskExecutor implements IExecutor {
    private final Map<TaskType, Executor> pools;
    private final IProgressController progressController;

    private final ScheduledExecutorService scheduler;

    private final Map<String, ScheduledFuture<?>> scheduledTasks = new HashMap<String, ScheduledFuture<?>>();

    public TaskExecutor() {
        this(null);
    }

    public TaskExecutor(IProgressController progressController) {
        this.progressController = progressController;

        Map<TaskType, Executor> pools = new EnumMap<TaskType, Executor>(TaskType.class);
        pools.put(TaskType.Common, setupCommonExecutor());
        pools.put(TaskType.MessageLoading, setupMessageLoadingExecutor());
        pools.put(TaskType.Synchronization, setupServerSynchronizationExecutor());

        this.pools = Collections.unmodifiableMap(pools);

        scheduler = new ScheduledThreadPoolExecutor(1);
    }

    private Executor setupServerSynchronizationExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    private Executor setupMessageLoadingExecutor() {
        return new ThreadPoolExecutor(10, 30, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
    }

    private Executor setupCommonExecutor() {
        return Executors.newFixedThreadPool(5);
    }

    @Override
    public void execute(Runnable target, TaskType type) {
        pools.get(type).execute(target);
    }

    @Override
    public void execute(Runnable target) {
        execute(target, TaskType.Common);
    }

    @Override
    public void setupTimer(String id, Runnable target, long delay) {
        ScheduledFuture<?> taskId = scheduler.schedule(new ScheduleTaskCleaner(id, target), delay, TimeUnit.MILLISECONDS);

        synchronized (scheduledTasks) {
            ScheduledFuture<?> oldTask = scheduledTasks.get(id);
            if (oldTask != null) {
                oldTask.cancel(false);
            }

            scheduledTasks.put(id, taskId);
        }
    }

    @Override
    public boolean killTimer(String id) {
        synchronized (scheduledTasks) {
            ScheduledFuture<?> oldTask = scheduledTasks.remove(id);
            if (oldTask != null) {
                return oldTask.cancel(false);
            }
        }

        return false;
    }

    private class ScheduleTaskCleaner implements Runnable {
        private final String id;
        private final Runnable task;

        public ScheduleTaskCleaner(String id, Runnable task) {
            this.id = id;
            this.task = task;
        }

        @Override
        public void run() {
            try {
                task.run();
            } finally {
                synchronized (scheduledTasks) {
                    // Remove the task in all cases.
                    scheduledTasks.remove(id);
                }
            }
        }
    }
}
