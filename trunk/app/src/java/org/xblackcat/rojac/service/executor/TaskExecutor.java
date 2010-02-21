package org.xblackcat.rojac.service.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
    private static final Log log = LogFactory.getLog(TaskExecutor.class);

    private final Map<TaskTypeEnum, Executor> pools;

    private final ScheduledExecutorService scheduler;

    private final Map<String, ScheduledFuture<?>> scheduledTasks = new HashMap<String, ScheduledFuture<?>>();

    public TaskExecutor() {
        Map<TaskTypeEnum, Executor> pools = new EnumMap<TaskTypeEnum, Executor>(TaskTypeEnum.class);
        pools.put(TaskTypeEnum.Background, setupCommonExecutor());
        pools.put(TaskTypeEnum.DataLoading, setupMessageLoadingExecutor());
        pools.put(TaskTypeEnum.Synchronization, setupServerSynchronizationExecutor());

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
    public void execute(Runnable target) {
        if (target == null) throw new NullPointerException("Can not execute empty target.");

        // Obtain target task type from annotation.
        TaskTypeEnum type = getTargetType(target.getClass());

        if (type == null) {
            // By default use Background type.
            type = TaskTypeEnum.Background;
            if (log.isTraceEnabled()) {
                log.trace("Target " + target.getClass() + " will be executed as type " + TaskTypeEnum.Background);
            }
        }

        pools.get(type).execute(target);
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
    public void setupPeriodicTask(String id, Runnable target, int period) {
        ScheduledFuture<?> taskId = scheduler.scheduleWithFixedDelay(target, period, period, TimeUnit.SECONDS);

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

    /**
     * Searches a TaskType annotation in class declaration in the following order: <ul> <li>Class</li>
     * <li>SuperClass</li> <li>Superclass of the superclass</li> <li>...</li> </ul> Note that implemented interfaces
     * will not be checked for annotations.
     *
     * @param clazz class to check.
     *
     * @return TaskTypeEnum value or <code>null</code> if the annotation is not present.
     */
    private static TaskTypeEnum getTargetType(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        TaskType annotation = clazz.getAnnotation(TaskType.class);

        if (annotation != null) {
            if (log.isTraceEnabled()) {
                log.trace("Target " + clazz + " has " + annotation.value() + " type");
            }
            return annotation.value();
        }

        return getTargetType(clazz.getSuperclass());
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
