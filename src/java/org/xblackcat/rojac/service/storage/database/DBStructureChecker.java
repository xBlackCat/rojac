package org.xblackcat.rojac.service.storage.database;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.progress.IProgressListener;
import org.xblackcat.rojac.service.progress.ProgressChangeEvent;
import org.xblackcat.rojac.service.progress.ProgressState;
import org.xblackcat.rojac.service.storage.IStructureChecker;
import org.xblackcat.rojac.service.storage.StorageCheckException;
import org.xblackcat.rojac.service.storage.StorageInitializationException;
import org.xblackcat.rojac.service.storage.schema.ICheck;
import org.xblackcat.rojac.service.storage.schema.IInitAH;
import org.xblackcat.sjpu.storage.IAH;
import org.xblackcat.sjpu.storage.IStorage;

import java.lang.reflect.Method;

/**
 * 27.09.11 17:16
 *
 * @author xBlackCat
 */
public class DBStructureChecker implements IStructureChecker {
    private static final Log log = LogFactory.getLog(DBStructureChecker.class);

    @SuppressWarnings("unchecked")
    private static final Check[] QUERIES = new Check[]{
            new Check(ICheck.class, IInitAH.class)
    };
    private final IStorage storage;

    public DBStructureChecker(IStorage storage) throws StorageInitializationException {
        this.storage = storage;
    }

    @Override
    public void check(IProgressListener progressListener) throws StorageCheckException {
        if (progressListener == null) {
            //  NPE and null checks avoiding
            progressListener = e -> {/* Nothing */};
        }

        if (log.isInfoEnabled()) {
            log.info("Check database storage structure started.");
        }

        int amountChecks = QUERIES.length;
        int checkIdx = 0;

        for (Check entry : QUERIES) {
            progressListener.progressChanged(new ProgressChangeEvent(this, ProgressState.Start, checkIdx, amountChecks));

            if (!isStructureValid(entry.checker)) {
                processInitialization(entry.init);
            }

            checkIdx++;
        }
    }

    private boolean isStructureValid(Class<? extends IAH> checker) {
        final Method[] methods = checker.getMethods();

        final IAH ah = storage.get(checker);

        for (Method m : methods) {
            try {
                final Object o = m.invoke(ah);
                if (o instanceof Boolean) {
                    if (!(Boolean) o) {
                        return false;
                    }
                }
            } catch (ReflectiveOperationException e) {
                return false;
            }
        }

        return true;
    }

    private void processInitialization(Class<? extends IAH> init) throws StorageCheckException {
        final Method[] methods = init.getMethods();

        final IAH ah = storage.get(init);

        for (Method m : methods) {
            try {
                m.invoke(ah);
            } catch (ReflectiveOperationException e) {
                throw new StorageCheckException("Can't initialize database", e);
            }
        }
    }

    private static class Check {
        private final Class<? extends IAH> checker;
        private final Class<? extends IAH> init;

        private Check(Class<? extends IAH> checker, Class<? extends IAH> init) {
            this.checker = checker;
            this.init = init;
        }
    }
}
