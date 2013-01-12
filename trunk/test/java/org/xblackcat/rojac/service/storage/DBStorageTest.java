package org.xblackcat.rojac.service.storage;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.xblackcat.rojac.service.storage.database.DataQuery;
import org.xblackcat.rojac.service.storage.database.SQL;
import org.xblackcat.rojac.util.DatabaseUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xBlackCat
 */


public class DBStorageTest extends TestCase {
    private static final Log log = LogFactory.getLog(DBStorageTest.class);

    public void testQueriesForExistence() throws IOException, StorageInitializationException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        Resource[] resources = resolver.getResources("/dbconfig/*/database.properties");
        Pattern configNamePattern = Pattern.compile(".[\\\\/]dbconfig[\\\\/](.+?)[\\\\/]database\\.properties");

        for (Resource resource : resources) {
            Matcher m = configNamePattern.matcher(resource.getDescription());
            if (!m.find()) {
                log.error("Can not resolve resource " + resource);
                log.error("Maia is here! Dying....");
                assertTrue(false);
                continue;
            }
            String name = m.group(1);

            log.info("Check queries for database configuration " + name);

            Map<SQL,List<SQL>> map = DatabaseUtils.loadInitializeSQLs(name);
            if (log.isInfoEnabled()) {
                log.info(map.size() + " initialization steps was loaded.");
            }

            Map<DataQuery, String> queries = DatabaseUtils.loadSQLs(name, DataQuery.class);
            if (log.isInfoEnabled()) {
                log.info(queries.size() + " data queries were loaded.");
            }
        }
    }
}
