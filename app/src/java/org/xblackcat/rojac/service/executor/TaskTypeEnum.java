package org.xblackcat.rojac.service.executor;

/**
 * @author xBlackCat
 */

public enum TaskTypeEnum {
    /**
     * Tasks for loading GUI data.
     */
    DataLoading,
    /**
     * Janus data retrieving tasks.
     */
    Synchronization,
    /**
     * Tasks not fit to previous types.
     */
    Common,
}
