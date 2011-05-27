package org.xblackcat.rojac.service.options;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
* @author xBlackCat
*/
public final class TestOptionsService extends AnOptionsService {
    private static final Log log = LogFactory.getLog(TestOptionsService.class);

    public TestOptionsService() throws OptionsServiceException {
    }

    private Map<String, String> cache = new HashMap<String, String>();

    protected String getProperty(String key) {
        String s = cache.get(key);
        if (log.isDebugEnabled()) {
            log.debug("Value for '" + key + "' is '" + s + '\'');
        }
        return s;
    }

    protected String setProperty(String key, String value) {
        if (log.isDebugEnabled()) {
            log.debug("Set value for '" + key + "': '" + value + '\'');
        }
        String s = cache.put(key, value);
        if (log.isDebugEnabled()) {
            log.debug("Old value for '" + key + "' is '" + s + '\'');
        }
        return s;
    }

    public boolean storeSettings() {
        return false;
    }
}
