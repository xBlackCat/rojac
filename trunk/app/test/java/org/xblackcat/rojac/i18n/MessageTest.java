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
import java.util.MissingResourceException;


public class MessageTest extends TestCase {
    private static final Log log = LogFactory.getLog(MessageTest.class);
    private static final Object[] EMPTY = new Object[100];

    public void testForExistence() throws IOException {
        // Test default locale first.
        if (log.isInfoEnabled()) {
            log.info("Test resources for default locale.");
        }
        assertTrue("Some translations is missed.", localeTest(null));

        // List of locales
        Collection<Locale> locales = Arrays.asList(RojacUtils.localesForBundle("i18n/" + Message.LOCALIZATION_BUNDLE_NAME, false));

        if (log.isInfoEnabled()) {
            log.info("Found " + locales.size() + " locales to test: " + locales);
        }

        boolean noErrors = true;
        for (Locale l : locales) {
            if (log.isInfoEnabled()) {
                log.info("Test resources for locale " + l);
            }
            try {
                noErrors &= localeTest(l);
            } catch (IllegalArgumentException e) {
                log.error("Can not set locale " + l, e);
                noErrors = false;
            }
        }

        assertTrue("Some translations is missed.", noErrors);
    }

    private static boolean localeTest(Locale l) {
        boolean noErrors = true;
        LocaleControl.getInstance().setLocale(l, true);

        for (Message m : Message.values()) {
            if (log.isDebugEnabled()) {
                log.debug("Check resource " + m);
            }
            try {
                if (m.get(EMPTY) == null) {
                    log.error("There is no translation for " + m.key());
                    noErrors = false;
                }
            } catch (MissingResourceException e) {
                log.error("There is no translation for " + m.key());
                noErrors = false;
            }
        }

        return noErrors;
    }
}
