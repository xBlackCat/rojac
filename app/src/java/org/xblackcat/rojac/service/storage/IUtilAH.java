package org.xblackcat.rojac.service.storage;

/**
 * 04.04.12 17:10
 *
 * @author xBlackCat
 */
public interface IUtilAH extends AH {
    void updateForumsStat() throws StorageException;

    void updateThreadsStat() throws StorageException;

    void updateLastPostId() throws StorageException;

    void updateLastPostDate() throws StorageException;
}
