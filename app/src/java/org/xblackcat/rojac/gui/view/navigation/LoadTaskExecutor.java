package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.util.RojacWorker;

/**
 * @author xBlackCat
 */
class LoadTaskExecutor extends RojacWorker<Void, Void> {
    private final ILoadTask[] tasks;
    private final Object[] results;

    LoadTaskExecutor(ILoadTask... tasks) {
        this.tasks = tasks;
        results = new Object[tasks.length];
    }

    @Override
    protected Void perform() throws Exception {
        for (int i = 0, tasksLength = tasks.length; i < tasksLength; i++) {
            try {
                Object result = tasks[i].doBackground();

                synchronized (this) {
                    results[i] = result;
                }
            } catch (Exception e) {
                // TODO: do something
            }
        }
        return null;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    protected void done() {
        for (int i = 0, tasksLength = tasks.length; i < tasksLength; i++) {
            Object result;
            synchronized (this) {
                result = results[i];
            }
            tasks[i].doSwing(result);
        }
    }
}
