package org.xblackcat.rojac.service.commands;

/**
 * Utility class to help with constructing request to Janus service in one line.
 *
 * @author xBlackCat
 */

public final class Request {
    public static final IRequest EXTRA_MESSAGES = new LoadExtraMessagesRequest();
    public static final IRequest GET_NEW_POSTS = new GetNewPostsRequest();
    public static final IRequest POST_CHANGES = new PostChangesRequest();
    public static final IRequest GET_USERS = new GetUsersRequest();
    public static final IRequest GET_FORUMS_LIST = new GetForumListRequest();

    /**
     * Set of requests to synchronize local DB with RSDN. Contains following requests: post changes, get new posts, load
     * broken topics and extra messages.
     */
    public static final IRequest[] SYNCHRONIZE = new IRequest[]{
            POST_CHANGES,
            GET_NEW_POSTS,
            EXTRA_MESSAGES
    };

    /**
     * Set of requests to complete synchronize local DB with RSDN. Contains following requests: post changes, get new
     * users, get new posts, load broken topics and extra messages.
     */
    public static final IRequest[] SYNCHRONIZE_WITH_USERS = new IRequest[]{
            POST_CHANGES,
            GET_USERS,
            GET_NEW_POSTS,
            EXTRA_MESSAGES
    };


    private Request() {
    }
}
