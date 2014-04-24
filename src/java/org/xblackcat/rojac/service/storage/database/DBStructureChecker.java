package org.xblackcat.rojac.service.storage.database;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.progress.IProgressListener;
import org.xblackcat.rojac.service.progress.ProgressChangeEvent;
import org.xblackcat.rojac.service.progress.ProgressState;
import org.xblackcat.rojac.service.storage.IStructureChecker;
import org.xblackcat.rojac.service.storage.StorageCheckException;
import org.xblackcat.rojac.service.storage.StorageInitializationException;
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
    private static final Class<? extends IAH>[] QUERIES = (Class<? extends IAH>[]) new Class[]{
            IInitAH.class
    };
    private final IStorage storage;

    public DBStructureChecker(IStorage storage) throws StorageInitializationException {
        this.storage = storage;
    }

    @Override
    public void check(IProgressListener progressListener) throws StorageCheckException {
        if (progressListener == null) {
            //  NPE and null checks avoiding
            progressListener = new IProgressListener() {
                @Override
                public void progressChanged(ProgressChangeEvent e) {
                    // Nothing
                }
            };
        }

        if (log.isInfoEnabled()) {
            log.info("Check database storage structure started.");
        }

        int amountChecks = QUERIES.length;
        int checkIdx = 0;

        for (Class<? extends IAH> entry : QUERIES) {
            progressListener.progressChanged(new ProgressChangeEvent(this, ProgressState.Start, checkIdx, amountChecks));

            processInitialization(entry);

            checkIdx++;
        }
    }

    private void processInitialization(Class<? extends IAH> entry) throws StorageCheckException {
        final Method[] methods = entry.getMethods();

        final IAH ah = storage.get(entry);

        for (Method m : methods) {
            try {
                m.invoke(ah);
            } catch (ReflectiveOperationException e) {
                throw new StorageCheckException("Can't initialize database", e);
            }
        }
    }
}
