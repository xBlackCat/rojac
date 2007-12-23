package org.xblackcat.sunaj.i18n;
/**
 * Date: 23 груд 2007
 * @author xBlackCat
 */


import junit.framework.TestCase;

import java.util.Locale;

public class MessageTest extends TestCase {
    public void testForExistence() {
        // List of locales
        // TODO: add aditional locales to the list
        Locale[] locales = new Locale[]{
                null, //Default locale
                new Locale("en", "EN")
        };

        for (Locale l : locales) {
            Message.setLocale(l);
            for (Message m : Message.values()) {
                assertNotNull(m);
            }
        }
    }
}