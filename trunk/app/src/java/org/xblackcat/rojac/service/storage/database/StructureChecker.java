package org.xblackcat.rojac.service.storage.database;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.storage.StorageCheckException;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.StorageInitializationException;
import org.xblackcat.rojac.service.storage.database.convert.Converters;
import org.xblackcat.rojac.service.storage.database.helper.IQueryHelper;
import org.xblackcat.rojac.util.DatabaseUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 27.09.11 17:16
 *
 * @author xBlackCat
 */
class StructureChecker implements IStructureChecker {
    private static final Log log = LogFactory.getLog(StructureChecker.class);

    private final Map<SQL, List<SQL>> initializationQueries;
    private final IQueryHelper helper;

    StructureChecker(IQueryHelper helper) throws StorageInitializationException {
        this.helper = helper;
        try {
            initializationQueries = DatabaseUtils.loadInitializeSQLs(helper.getEngine());
        } catch (IOException e) {
            throw new StorageInitializationException("Can not load initialization routines", e);
        }
    }

    @Override
    public void check() throws StorageCheckException {
        check(false);
    }

    @Override
    public void check(boolean onlyTest) throws StorageCheckException {
        if (log.isInfoEnabled()) {
            log.info("Check database storage structure started.");
        }

        for (Map.Entry<SQL, List<SQL>> entry : initializationQueries.entrySet()) {
            boolean success = false;
            SQL check = entry.getKey();
            if (log.isTraceEnabled()) {
                log.trace("Perform check " + check);
            }
            try {
                Boolean c = helper.executeSingle(Converters.TO_BOOLEAN, check.getSql());
                success = Boolean.TRUE.equals(c);
            } catch (StorageException e) {
                if (log.isTraceEnabled()) {
                    log.trace(check + " check failed. Reason: " + e.getLocalizedMessage());
                }
            }

            if (!success) {
                if (onlyTest) {
                    throw new StorageCheckException("Database has invalid structure: " + check + " check failed");
                }

                // If c is null or FALSE - abort.
                if (log.isTraceEnabled()) {
                    log.trace(check + " check failed. Perform initialization.");
                }

                for (SQL sql : entry.getValue()) {
                    try {
                        if (log.isTraceEnabled()) {
                            log.trace("Perform initialization command " + sql);
                        }
                        helper.update(sql.getSql());
                    } catch (StorageException e) {
                        log.error("Can not perform initialization procedure " + sql);
                        throw new StorageCheckException("Can not execute " + sql, e);
                    }
                }

                // Perform post-check
                try {
                    if (log.isTraceEnabled()) {
                        log.trace("Perform post-initialization check " + check);
                    }
                    Boolean c = helper.executeSingle(Converters.TO_BOOLEAN, check.getSql());
                    success = Boolean.TRUE.equals(c);
                } catch (StorageException e) {
                    throw new StorageCheckException("Post check failed for " + check, e);
                }

                if (!success) {
                    throw new StorageCheckException("Post check failed for " + check);
                }
            }
        }
    }
}
