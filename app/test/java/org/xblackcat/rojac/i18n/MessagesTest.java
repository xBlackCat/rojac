package org.xblackcat.rojac.i18n;
/**
 *
 * @author xBlackCat
 */


import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Locale;

public class MessagesTest extends TestCase {
    private static final Log log = LogFactory.getLog(MessagesTest.class);
    private static final Object[] EMPTY = new Object[100];

    public void testForExistence() {
        // List of locales
        // TODO: add aditional locales to the list
        Locale[] locales = new Locale[]{
                null, //Default locale
                new Locale("en", "EN"),
                new Locale("ru", "RU")
        };

        for (Locale l : locales) {
            if (log.isInfoEnabled()) {
                log.info("Test resources for locale " + l);
            }
            Messages.setLocale(l);
            for (Messages m : Messages.values()) {
                if (log.isDebugEnabled()) {
                    log.debug("Check resource " + m);
                }
                assertNotNull(m.get(EMPTY));
            }
        }
    }
}