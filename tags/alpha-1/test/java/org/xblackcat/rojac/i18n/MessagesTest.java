package org.xblackcat.rojac.i18n;
/**
 *
 * @author xBlackCat
 */


import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.util.RojacUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import static ch.lambdaj.Lambda.*;

public class MessagesTest extends TestCase {
    private static final Log log = LogFactory.getLog(MessagesTest.class);
    private static final Object[] EMPTY = new Object[100];

    public void testForExistence() throws IOException {
        // Test default locale first.
        if (log.isInfoEnabled()) {
            log.info("Test resources for default locale.");
        }
        localeTest(null);

        // List of locales
        Collection<Locale> locales = Arrays.asList(RojacUtils.localesForBundle(Messages.LOCALIZATION_BUNDLE_NAME, false));

        if (log.isInfoEnabled()) {
            log.info("Found " + locales.size() + " locales to test: " + join(locales));
        }

        for (Locale l : locales) {
            if (log.isInfoEnabled()) {
                log.info("Test resources for locale " + l);
            }
            localeTest(l);
        }
    }

    private static void localeTest(Locale l) {
        Messages.setLocale(l, true);
        for (Messages m : Messages.values()) {
            if (log.isDebugEnabled()) {
                log.debug("Check resource " + m);
            }
            assertNotNull(m.get(EMPTY));
        }
    }
}