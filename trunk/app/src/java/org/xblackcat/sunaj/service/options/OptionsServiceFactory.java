package org.xblackcat.sunaj.service.options;

/**
 * Date: 12 трав 2007
 *
 * @author ASUS
 */

public class OptionsServiceFactory {
    private static IOptionsService OPTIONS_SERVICE = new MultiUserOptionsService();

    private OptionsServiceFactory() {
    }

    public static IOptionsService getOptionsService() {
        return OPTIONS_SERVICE;
    }
}
