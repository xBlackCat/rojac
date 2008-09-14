package org.xblackcat.rojac.service.executor;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Date: 14 груд 2007
 *
 * @author xBlackCat
 */

public final class TaskExecutor implements IExecutor {
    private final Map<TaskType, Executor> pools;

    public TaskExecutor() {
        Map<TaskType, Executor> pools = new EnumMap<TaskType, Executor>(TaskType.class);
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
        return new ThreadPoolExecutor(10, 30, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
    }

    public void execute(Runnable target, TaskType type) {
        pools.get(type).execute(target);
    }

    public void execute(Runnable target) {
        execute(target, TaskType.Common);
    }
}
