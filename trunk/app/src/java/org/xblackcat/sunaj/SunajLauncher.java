package org.xblackcat.sunaj;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.sunaj.service.options.IOptionsService;
import org.xblackcat.sunaj.service.options.MultiUserOptionsService;
import org.xblackcat.sunaj.service.options.Property;

/**
 * Date: 26 бер 2007
 *
 * @author Alexey
 */

public class SunajLauncher {
    private static final Log log = LogFactory.getLog(SunajLauncher.class);

    private SunajLauncher() {
    }

    public static void main(String[] args) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("Application started.");
        }
        IOptionsService os = MultiUserOptionsService.getInstance();

        Property<Boolean> p = Property.SERVICE_JANUS_USE_GZIP;
        if (log.isDebugEnabled()) {
            log.debug(p + " = " + os.getProperty(p));
        }
    }
}
