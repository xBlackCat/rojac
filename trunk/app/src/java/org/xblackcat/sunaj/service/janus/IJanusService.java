package org.xblackcat.sunaj.service.janus;

import org.xblackcat.sunaj.service.janus.data.*;

/**
 * Date: 10 квіт 2007
 *
 * @author Alexey
 */

public interface IJanusService {
    /**
     * Tests a connection with Janus WS.
     *
     * @throws JanusServiceException throws if connection can not be established.
     */
    void testConnection() throws JanusServiceException;

    /**
     * Retrieves a forums list from Janus WS.
     *
     * @return RSDN forums list.
     *
     * @throws JanusServiceException throws if any errors occurs.
     */
    ForumsList getForumsList() throws JanusServiceException;

    /**
     * Gets new users from the Janus WS.
     *
     * @param verRow    local version of the users list.
     * @param maxOutput amount of users in the server users list.
     *
     * @return users list with the version of the list.
     *
     * @throws JanusServiceException throws if any errors occurs.
     */
    UsersList getNewUsers(Version verRow, int maxOutput) throws JanusServiceException;

    /**
     * Loads whole topics for given messages.
     *
     * @param messageIds array of messages ids to loads the whole topics with the messages.
     *
     * @return list of topics with the messages.
     *
     * @throws JanusServiceException throws if any errors occurs.
     */
    TopicMessages getTopicByMessage(int[] messageIds) throws JanusServiceException;

    /**
     * Commits the changes (new messages and etc.)
     *
     * @return the status of the commit.
     *
     * @throws JanusServiceException throws if any errors occurs.
     */
    PostInfo commitChanges() throws JanusServiceException;

    /**
     * Posts the changes to the RSDN forum.
     *
     * @param messages new messages to post.
     * @param ratings  new ratings to post.
     *
     * @throws JanusServiceException throws if any errors occurs.
     */
    void postChanges(NewMessage[] messages, NewRating[] ratings) throws JanusServiceException;

    /**
     * Gets new messages from Janus WS.
     *
     * @param subscribedForums list of subscribed forums.
     * @param firstForumRequest
     * @param ratingVer last rating row version.
     * @param messageVer last messages row version.
     * @param moderateVer last moderate info row version.
     * @param breakMsgIds ???
     * @param breakTopicIds ???
     * @param maxOutput amount of messages in response.
     *
     * @return new messages and other information.
     *
     * @throws JanusServiceException throws if any errors occurs.
     */
    NewData getNewData(int[] subscribedForums, boolean[] firstForumRequest, Version ratingVer, Version messageVer, Version moderateVer, int[] breakMsgIds, int[] breakTopicIds, int maxOutput) throws JanusServiceException;
}
