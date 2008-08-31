package org.xblackcat.rojac.service.storage;

/**
 * Date: 15.04.2007
 *
 * @author ASUS
 */

public interface IStorage {

    void initialize() throws StorageException;

    IForumAH getForumAH();

    IForumGroupAH getForumGroupAH();

    IMessageAH getMessageAH();

    IModerateAH getModerateAH();

    INewMessageAH getNewMessageAH();

    INewModerateAH getNewModerateAH();

    INewRatingAH getNewRatingAH();

    IRatingAH getRatingAH();

    IUserAH getUserAH();

    IVersionAH getVersionAH();

    IMiscAH getMiscAH();
}
