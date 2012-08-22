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
     * Tasks to be performed in background but not related to GUI.
     */
    Background,
    /**
     * Initialization tasks. Should be performed sequentially. Probably in forked threads
     */
    Initialization,
    /**
     * Heavy initialization tasks. Should be performed sequentially. Probably in forked threads
     */
    InitializationHeavy,
}
