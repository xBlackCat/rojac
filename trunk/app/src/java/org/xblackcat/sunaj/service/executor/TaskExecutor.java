package org.xblackcat.sunaj.service.executor;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Date: 14 груд 2007
 *
 * @author xBlackCat
 */

public final class TaskExecutor {
    private static final TaskExecutor INSTANCE = new TaskExecutor();

    public static TaskExecutor getInstance() {
        return INSTANCE;
    }

    private final Map<TaskType, Executor> pools;

    private TaskExecutor() {
        EnumMap<TaskType, Executor> pools = new EnumMap<TaskType, Executor>(TaskType.class);
        pools.put(TaskType.Common, setupCommonExecutor());
        pools.put(TaskType.MessageLoading, setupMessageLoadingExecutor());
        pools.put(TaskType.ServerSynchronization, setupServerSynchronizationExecutor());

        this.pools = Collections.unmodifiableMap(pools);
    }

    private Executor setupServerSynchronizationExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    private Executor setupMessageLoadingExecutor() {
        return Executors.newFixedThreadPool(5);
    }

    private Executor setupCommonExecutor() {
        return new ThreadPoolExecutor(10, 30, 1, null, new LinkedBlockingQueue<Runnable>());
    }

    public void execute(Runnable target, TaskType type) {
        pools.get(type).execute(target);
    }

    public void execute(Runnable target) {
        execute(target, TaskType.Common);
    }
}
