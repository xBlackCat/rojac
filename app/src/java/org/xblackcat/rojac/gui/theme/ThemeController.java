package org.xblackcat.rojac.gui.theme;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.utils.ResourceUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * Date: 22 ρεπο 2009
 *
 * @author xBlackCat
 */

public final class ThemeController {
    private static final Log log = LogFactory.getLog(ThemeController.class);
    private static ThemeController INSTANCE = new ThemeController();

    public static ThemeController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ThemeController();
            try {
                INSTANCE.initialize();
            } catch (IOException e) {
                log.error("Can not initialize theme controller");
                throw new RuntimeException(e);
            }
        }

        return INSTANCE;
    }

    private IconPack[] iconPacks;
    private LnF[] looksAndFeels;

    private ThemeController() {
    }

    private void initialize() throws IOException {
        // Load IconPacks from resource
        Properties packs = ResourceUtils.loadProperties("/iconpack.properties");
    }
}
