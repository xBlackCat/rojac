package org.xblackcat.rojac.gui.view.navigation;

/**
 * @author xBlackCat
 */
abstract class ALoadTask<V> {
    protected static final ALoadTask[] NO_TASKS = new ALoadTask[0];

    @SuppressWarnings({"unchecked"})
    protected static <T> ALoadTask<T>[] group(ALoadTask<T> task) {
        if (task == null) {
            return NO_TASKS;
        }

        return new ALoadTask[]{task};
    }

    abstract V doBackground() throws Exception;

    abstract void doSwing(V data);
}
