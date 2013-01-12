package org.xblackcat.rojac.service.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.NotImplementedException;

import java.util.*;
import java.util.concurrent.*;

/**
 * Service for executing tasks in separate thread.
 *
 * @author xBlackCat
 */

public final class TaskExecutor implements IExecutor, ExecutorService {
    private static final Log log = LogFactory.getLog(TaskExecutor.class);

    private final Map<TaskTypeEnum, ExecutorService> pools;

    private final ScheduledExecutorService scheduler;

    private final Map<String, ScheduledFuture<?>> scheduledTasks = new HashMap<>();

    public TaskExecutor() {
        Map<TaskTypeEnum, ExecutorService> pools = new EnumMap<>(TaskTypeEnum.class);
        pools.put(TaskTypeEnum.Background, setupCommonExecutor());
        pools.put(TaskTypeEnum.DataLoading, setupMessageLoadingExecutor());
        pools.put(TaskTypeEnum.Synchronization, setupServerSynchronizationExecutor());
        pools.put(TaskTypeEnum.Initialization, setupInitializatorsExecutor());
        pools.put(TaskTypeEnum.InitializationHeavy, setupHeavyInitializatorsExecutor());

        this.pools = Collections.unmodifiableMap(pools);

        scheduler = new ScheduledThreadPoolExecutor(1);
    }

    private ExecutorService setupInitializatorsExecutor() {
        return new ForkJoinPool();
    }

    private ExecutorService setupHeavyInitializatorsExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    private ExecutorService setupServerSynchronizationExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    private ExecutorService setupMessageLoadingExecutor() {
        return new ThreadPoolExecutor(10, 30, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
    }

    private ExecutorService setupCommonExecutor() {
        return Executors.newFixedThreadPool(5);
    }

    @Override
    public void execute(Runnable target) {
        getAnnotatedPool(target).execute(target);
    }

    @Override
    public void setupTimer(String id, Runnable target, long delay) {
        ScheduledFuture<?> taskId = scheduler.schedule(new ScheduleTaskCleaner(id, target), delay, TimeUnit.MILLISECONDS);

        replaceTask(id, taskId);
    }

    @Override
    public void setupPeriodicTask(String id, Runnable target, int period) {
        ScheduledFuture<?> taskId = scheduler.scheduleWithFixedDelay(target, period, period, TimeUnit.SECONDS);

        replaceTask(id, taskId);
    }

    private void replaceTask(String id, ScheduledFuture<?> taskId) {
        ScheduledFuture<?> oldTask;
        synchronized (scheduledTasks) {
            oldTask = scheduledTasks.put(id, taskId);
        }

        if (oldTask != null) {
            oldTask.cancel(false);
        }
    }

    @Override
    public boolean killTimer(String id) {
        ScheduledFuture<?> oldTask;
        synchronized (scheduledTasks) {
            oldTask = scheduledTasks.remove(id);
        }

        if (oldTask != null) {
            return oldTask.cancel(false);
        }

        return false;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        throw new NotImplementedException();
    }

    @Override
    public void shutdown() {
        for (ExecutorService p : pools.values()) {
            p.shutdownNow();
        }
    }

    @Override
    public List<Runnable> shutdownNow() {
        List<Runnable> list = new ArrayList<>();
        for (ExecutorService p : pools.values()) {
            list.addAll(p.shutdownNow());
        }
        return list;
    }

    @Override
    public boolean isShutdown() {
        for (ExecutorService e : pools.values()) {
            if (!e.isShutdown()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isTerminated() {
        for (ExecutorService e : pools.values()) {
            if (!e.isTerminated()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return getAnnotatedPool(task).submit(task);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return getAnnotatedPool(task).submit(task, result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return getAnnotatedPool(task).submit(task);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        throw new NotImplementedException();
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        throw new NotImplementedException();
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        throw new NotImplementedException();
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        throw new NotImplementedException();
    }

    /**
     * Searches a TaskType annotation in class declaration in the following order: <ul> <li>Class</li>
     * <li>SuperClass</li> <li>Superclass of the superclass</li> <li>...</li> </ul> Note that implemented interfaces
     * will not be checked for annotations.
     *
     * @param clazz class to check.
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

    private <T> ExecutorService getAnnotatedPool(T target) {
        if (target == null) {
            throw new NullPointerException("Can not execute empty target.");
        }

        // Obtain target task type from annotation.
        TaskTypeEnum type = getTargetType(target.getClass());

        if (type == null) {
            // By default use Background type.
            type = TaskTypeEnum.Background;
            if (log.isTraceEnabled()) {
                log.trace("Target " + target.getClass() + " will be executed as type " + TaskTypeEnum.Background);
            }
        }

        return pools.get(type);
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
