package org.xblackcat.rojac.gui;

/**
 * Date: 15 лип 2008
 *
 * @author xBlackCat
 */

public interface IConfigurable {
    /**
     * Loads and applies settings for component from IOptionsService.
     */
    void applySettings();

    /**
     * Updates settings in IOptionsService (for future saving).
     */
    void updateSettings();
}
