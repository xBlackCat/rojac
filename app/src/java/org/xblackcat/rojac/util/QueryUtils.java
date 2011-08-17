package org.xblackcat.rojac.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.storage.StorageInitializationException;
import org.xblackcat.rojac.service.storage.database.IPropertiable;
import org.xblackcat.rojac.service.storage.database.SQL;
import org.xblackcat.utils.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author xBlackCat
 */

public final class QueryUtils {
    private static final Log log = LogFactory.getLog(QueryUtils.class);
    public static final String DBCONFIG_PACKAGE = "dbconfig/";

    private QueryUtils() {
    }

    public static <T extends Enum<T> & IPropertiable> Map<T, String> loadSQLs(String propRoot, Class<T> type) throws IOException, StorageInitializationException {
        String name = '/' + DBCONFIG_PACKAGE + propRoot + "/sql.data.properties";
        Properties queries = ResourceUtils.loadProperties(name);

        Map<T, String> qs = new EnumMap<>(type);
        for (T q : type.getEnumConstants()) {
            String sql = (String) queries.remove(q.getPropertyName());
            if (sql != null) {
                if (log.isTraceEnabled()) {
                    log.trace("Property '" + q.getPropertyName() + "' initialized with SQL: " + sql);
                }
                qs.put(q, sql);
            } else {
                throw new StorageInitializationException(q + " is not initialized.");
            }
        }

        if (!queries.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("There are unused properties in " + name);
                for (Map.Entry<Object, Object> entry : queries.entrySet()) {
                    log.warn("Property: " + entry.getKey() + " = " + entry.getValue());
                }
            }
            throw new StorageInitializationException("There are some excess properties in " + name);
        }

        return Collections.unmodifiableMap(qs);
    }

    /**
     * Loads and returns a new Properties object for given resource or path name. The properties in result map stored in
     * the same order as they defined in properties file.
     *
     * @param propertiesFile target properties resource for loading.
     * @return
     * @throws IOException
     */
    public static Map<String, String> loadProperties(String propertiesFile) throws IOException {
        InputStream is;
        try {
            is = ResourceUtils.getResourceAsStream(propertiesFile);
        } catch (MissingResourceException e) {
            if (propertiesFile.toLowerCase().endsWith(".properties")) {
                throw e;
            } else {
                is = ResourceUtils.getResourceAsStream(propertiesFile + ".properties");
            }
        }

        final Map<String, String> map = new LinkedHashMap<>();
        // Workaround to load properties in natural order.
        Properties p = new Properties() {
            @Override
            public Object put(Object key, Object value) {
                map.put(key.toString(), value.toString());
                return super.put(key, value);
            }
        };
        p.load(is);

        return map;
    }

    public static Map<SQL, List<SQL>> loadInitializeSQLs(String propRoot) throws IOException {
        Map<String, String> check = loadProperties('/' + DBCONFIG_PACKAGE + propRoot + "/sql.check.properties");
        Properties init = ResourceUtils.loadProperties('/' + DBCONFIG_PACKAGE + propRoot + "/sql.initialize.properties");
        Properties clue = ResourceUtils.loadProperties('/' + DBCONFIG_PACKAGE + propRoot + "/sql.depends.properties");

        Map<SQL, List<SQL>> map = new LinkedHashMap<>();

        for (Map.Entry<String, String> ce : check.entrySet()) {
            String name = ce.getKey();
            String sql = ce.getValue();

            String inits = clue.getProperty(name, "");
            List<SQL> sqls = new ArrayList<>();
            String[] initNames = inits.trim().split(",");
            if (!ArrayUtils.isEmpty(initNames)) {
                for (String initName : initNames) {
                    String initSql = init.getProperty(initName.trim());
                    if (StringUtils.isBlank(initSql)) {
                        throw new IOException(initName + " SQL not defined (Used in " + name + ").");
                    } else {
                        sqls.add(new SQL(initName, initSql));
                    }
                }
            }

            map.put(new SQL(name, sql), sqls);
        }

        return Collections.unmodifiableMap(map);
    }
}
