package org.xblackcat.sunaj.service.storage;

/**
 * Date: 15.04.2007
 *
 * @author ASUS
 */

public interface IStorage {
    /* Initialization routines */
    boolean checkStructure();

    void initialize() throws StorageException;

    IForumAH getForumAH();

    IForumGroupAH getForumGroupAH();

    IMessageAH getMessageAH();

    IModerateAH getModerateAH();

    INewMessageAH getNewMessageAH();

    INewRatingAH getNewRatingAH();

    IRatingAH getRatingAH();

    IUserAH getUserAH();

    IVersionAH getVersionAH();

    IMiscAH getMiscAH();
}
