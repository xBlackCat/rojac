package org.xblackcat.rojac.service.janus;

import org.xblackcat.rojac.data.NewMessage;
import org.xblackcat.rojac.data.NewModerate;
import org.xblackcat.rojac.data.NewRating;
import org.xblackcat.rojac.data.Version;
import org.xblackcat.rojac.service.janus.data.ForumsList;
import org.xblackcat.rojac.service.janus.data.NewData;
import org.xblackcat.rojac.service.janus.data.PostInfo;
import org.xblackcat.rojac.service.janus.data.TopicMessages;
import org.xblackcat.rojac.service.janus.data.UsersList;

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
     * @param verRow
     *
     * @return RSDN forums list.
     *
     * @throws JanusServiceException throws if any errors occurs.
     */
    ForumsList getForumsList(Version verRow) throws JanusServiceException;

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
     * @param messages  new messages to post.
     * @param ratings   new ratings to post.
     * @param moderates new moderate actions to post.
     * @param moderates
     *
     * @throws JanusServiceException throws if any errors occurs.
     */
    void postChanges(NewMessage[] messages, NewRating[] ratings, NewModerate[] moderates) throws JanusServiceException;

    /**
     * Gets new messages from Janus WS.
     *
     * @param subscribedForums  list of subscribed forums.
     * @param firstForumRequest
     * @param ratingVer         last rating row version.
     * @param messageVer        last messages row version.
     * @param moderateVer       last moderate info row version.
     * @param breakMsgIds       ???
     * @param breakTopicIds     ???
     * @param maxOutput         amount of messages in response.
     *
     * @return new messages and other information.
     *
     * @throws JanusServiceException throws if any errors occurs.
     */
    NewData getNewData(int[] subscribedForums, boolean firstForumRequest, Version ratingVer, Version messageVer, Version moderateVer, int[] breakMsgIds, int[] breakTopicIds, int maxOutput) throws JanusServiceException;

    void init(boolean useCompression) throws JanusServiceException;
}
