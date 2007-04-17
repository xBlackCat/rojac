package org.xblackcat.sunaj;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.sunaj.service.storage.IStorage;
import org.xblackcat.sunaj.service.storage.database.DBStorage;

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
        IStorage storage = new DBStorage("dbconfig/smallsql");
        boolean b = storage.checkStructure();
        if (!b) {
            storage.initialize();
        }
    }
}
