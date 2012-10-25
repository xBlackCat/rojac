package org.xblackcat.rojac.service.janus;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.NewMessage;
import org.xblackcat.rojac.data.NewModerate;
import org.xblackcat.rojac.data.NewRating;
import org.xblackcat.rojac.data.Version;
import org.xblackcat.rojac.service.janus.data.*;
import org.xblackcat.rojac.util.RojacUtils;
import ru.rsdn.janus.*;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import java.net.HttpCookie;
import java.util.*;

/**
 * @author Alexey
 */

public class JanusService implements IJanusService {
    private static final Log log = LogFactory.getLog(JanusService.class);

    private final String userName;
    private final String password;

    private JanusATSoap soap;

    public JanusService(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public void testConnection() throws JanusServiceException {
        log.debug("Perform connection test.");

        soap.check();
        if (log.isDebugEnabled()) {
            log.debug("Connection has been established.");
        }
    }

    @Override
    public ForumsList getForumsList(Version verRow) throws JanusServiceException {
        log.info("Retrieve the forums list from the Janus WS.");

        ForumResponse list;
        ForumRequest fr = new ForumRequest();
        fr.setForumsRowVersion(verRow.getBytes());
        fr.setUserName(userName);
        fr.setPassword(password);
        list = soap.getForumList(fr);

        List<JanusForumInfo> forumInfos = list.getForumList().getJanusForumInfo();
        List<JanusForumGroupInfo> groupInfos = list.getGroupList().getJanusForumGroupInfo();
        Version version = new Version(list.getForumsRowVersion());
        if (log.isDebugEnabled()) {
            log.debug("Got " + forumInfos.size() + " forum(s) in " + groupInfos.size() + " group(s). Version: " + version);
        }
        return new ForumsList(forumInfos, groupInfos, version);
    }

    @Override
    public UsersList getNewUsers(Version verRow, int maxOutput) throws JanusServiceException {
        log.info("Retrieve the users list from the Janus WS. Limit = " + maxOutput);

        UserResponse list;
        UserRequest userRequest = new UserRequest();
        userRequest.setUserName(userName);
        userRequest.setPassword(password);
        userRequest.setLastRowVersion(verRow.getBytes());
        userRequest.setMaxOutput(maxOutput);
        list = soap.getNewUsers(userRequest);
        if (log.isDebugEnabled()) {
            log.debug("Got " + list.getUsers().getJanusUserInfo().size() + " new user(s)");
        }
        return new UsersList(list);
    }

    @Override
    public TopicMessages getTopicByMessage(int[] messageIds) throws JanusServiceException {
        log.info("Retrieve the topics by messages from the Janus WS.");
        if (log.isDebugEnabled()) {
            log.debug("Messages ids are: " + ArrayUtils.toString(messageIds));
        }

        TopicResponse list;
        TopicRequest r = new TopicRequest();
        r.setUserName(userName);
        r.setPassword(password);
        r.setMessageIds(RojacUtils.toIntArray(messageIds));
        list = soap.getTopicByMessage(r);
        List<JanusMessageInfo> messages = list.getMessages().getJanusMessageInfo();
        List<JanusModerateInfo> moderate = list.getModerate().getJanusModerateInfo();
        List<JanusRatingInfo> rating = list.getRating().getJanusRatingInfo();
        if (log.isDebugEnabled()) {
            log.debug("Got " + messages.size() + " message(s), " + moderate.size() + " modarators note(s) and " + rating.size() + " user raiting(s).");
        }
        return new TopicMessages(messages, moderate, rating);
    }

    @Override
    public PostInfo commitChanges() throws JanusServiceException {
        log.info("Commit the local changes to the Janus WS.");

        PostResponse post;
        post = soap.postChangeCommit();
        List<PostExceptionInfo> exceptions = post.getExceptions().getPostExceptionInfo();
        List<Integer> ids = post.getCommitedIds().getInt();
        if (log.isDebugEnabled()) {
            log.debug(ids.size() + " message(s) commited and " + exceptions.size() + " exception(s) occurs.");
        }
        return new PostInfo(ids, exceptions);
    }

    @Override
    public void postChanges(
            Collection<NewMessage> messages,
            Collection<NewRating> ratings,
            Collection<NewModerate> moderates
    ) throws JanusServiceException {
        log.info("Post the changes to the Janus WS.");

        PostRequest postRequest = new PostRequest();
        postRequest.setUserName(userName);
        postRequest.setPassword(password);
        ArrayOfPostMessageInfo writtenMessages = new ArrayOfPostMessageInfo();
        writtenMessages.getPostMessageInfo().addAll(RojacUtils.getRSDNObject(messages, PostMessageInfo.class));
        postRequest.setWritedMessages(writtenMessages);
        ArrayOfPostRatingInfo postRatingInfo = new ArrayOfPostRatingInfo();
        postRatingInfo.getPostRatingInfo().addAll(RojacUtils.getRSDNObject(ratings, PostRatingInfo.class));
        postRequest.setRates(postRatingInfo);
        ArrayOfPostModerateInfo postModerateInfo = new ArrayOfPostModerateInfo();
        postModerateInfo.getPostModerateInfo().addAll(RojacUtils.getRSDNObject(moderates, PostModerateInfo.class));
        postRequest.setModerates(postModerateInfo);

        soap.postChange(postRequest);
    }

    @Override
    public NewData getNewData(
            Collection<RequestForumInfo> requestForumInfos,
            Version ratingVer,
            Version messageVer,
            Version moderateVer,
            int[] breakMsgIds,
            int[] breakTopicIds,
            int maxOutput
    ) throws JanusServiceException {
        if (log.isInfoEnabled()) {
            log.info("Retrieve the messages from the Janus WS. Portion limit = " + maxOutput);
        }

        ChangeResponse list;
        ChangeRequest r = new ChangeRequest();
        r.setUserName(userName);
        r.setPassword(password);
        ArrayOfRequestForumInfo subscribedForums = new ArrayOfRequestForumInfo();
        subscribedForums.getRequestForumInfo().addAll(requestForumInfos);
        r.setSubscribedForums(subscribedForums);
        r.setRatingRowVersion(ratingVer.getBytes());
        r.setMessageRowVersion(messageVer.getBytes());
        r.setModerateRowVersion(moderateVer.getBytes());
        r.setBreakMsgIds(RojacUtils.toIntArray(breakMsgIds));
        r.setBreakTopicIds(RojacUtils.toIntArray(breakTopicIds));
        r.setMaxOutput(maxOutput);
        list = soap.getNewData(r);
        Version forumRowVersion = new Version(list.getLastForumRowVersion());
        Version ratingRowVersion = new Version(list.getLastRatingRowVersion());
        Version moderateRowVersion = new Version(list.getLastModerateRowVersion());

        List<JanusMessageInfo> newMessages = list.getNewMessages().getJanusMessageInfo();
        List<JanusModerateInfo> newModerate = list.getNewModerate().getJanusModerateInfo();
        List<JanusRatingInfo> newRating = list.getNewRating().getJanusRatingInfo();

        int ownId = list.getUserId();

        if (log.isDebugEnabled()) {
            log.debug(
                    "New data statistics: " +
                            "own user id is " + ownId + ", " +
                            newMessages.size() + " new message(s), " +
                            newRating.size() + " new rating(s), " +
                            newModerate.size() + " new moderate info(s)" +
                            ", messages version is " + forumRowVersion +
                            ", ratings version is " + ratingRowVersion +
                            ", moderates version is " + moderateRowVersion
            );
        }
        return new NewData(
                ownId,
                forumRowVersion,
                ratingRowVersion,
                moderateRowVersion,
                newMessages,
                newModerate,
                newRating
        );
    }

    @Override
    public UsersList getUsersByIds(int[] ids) throws JanusServiceException {
        log.info("Retrieve users by ids");
        if (log.isDebugEnabled()) {
            log.debug("Requested user ids: " + ArrayUtils.toString(ids));
        }

        UserByIdsRequest r = new UserByIdsRequest();
        r.setUserName(userName);
        r.setPassword(password);
        r.setUserIds(RojacUtils.toIntArray(ids));
        UserResponse userByIds = soap.getUserByIds(r);

        if (log.isDebugEnabled()) {
            log.debug("Got " + userByIds.getUsers().getJanusUserInfo().size() + " user(s).");
        }


        return new UsersList(userByIds);
    }

    @SuppressWarnings({"unchecked"})
    public void init(boolean useCompression) throws JanusServiceException {
        log.info("Janus SOAP service initialization has been started.");

//        SimpleProvider provider = new SimpleProvider();
//
//        provider.deployTransport("http", new JanusHTTPSender(ServiceFactory.getInstance().getProgressControl()));
        try {
            JanusAT jl = new JanusAT();

            soap = jl.getJanusATSoap();

            Map<String, Object> requestContext = ((BindingProvider) soap).getRequestContext();
            Map<String, List<String>> httpHeaders = new Hashtable();
            // Compress the request
            if (useCompression) {
                log.info("Data compression is enabled.");
                // Tell the server it can compress the response
                httpHeaders.put(
                        HTTPConstants.HEADER_ACCEPT_ENCODING,
                        Collections.singletonList(HTTPConstants.COMPRESSION_GZIP)
                );
    //                soap._setProperty(HTTPConstants.MC_GZIP_REQUEST, Boolean.TRUE);
            }
            httpHeaders.put(
                    HTTPConstants.HEADER_TRANSFER_ENCODING_CHUNKED,
                    Collections.singletonList(Boolean.FALSE.toString())
            );
            httpHeaders.put(
                    HTTPConstants.HEADER_CONNECTION,
                    Collections.singletonList(HTTPConstants.HEADER_CONNECTION_CLOSE)
            );
            httpHeaders.put(HTTPConstants.HEADER_USER_AGENT, Collections.singletonList(RojacUtils.VERSION_STRING));
            requestContext.put(MessageContext.HTTP_REQUEST_HEADERS, httpHeaders);

            log.info("Initialization has done.");
        } catch (RuntimeException e) {
            throw new JanusServiceException("Failed to initialize service", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void fixCookies() {
        Map<String, Object> responseContext = ((BindingProvider) soap).getResponseContext();

        List<HttpCookie> cookies = new ArrayList<>();
        if (responseContext != null) {
            Map<String, List<String>> headers = (Map<String, List<String>>) responseContext.get(MessageContext.HTTP_RESPONSE_HEADERS);
            if (headers.containsKey(HTTPConstants.HEADER_SET_COOKIE)) {
                for (String cookie : headers.get(HTTPConstants.HEADER_SET_COOKIE)) {
                    cookies.addAll(HttpCookie.parse(HTTPConstants.HEADER_SET_COOKIE + ": " + cookie));
                }
            }
            if (headers.containsKey(HTTPConstants.HEADER_SET_COOKIE2)) {
                for (String cookie : headers.get(HTTPConstants.HEADER_SET_COOKIE2)) {
                    cookies.addAll(HttpCookie.parse(HTTPConstants.HEADER_SET_COOKIE2 + ": " + cookie));
                }
            }
        }

        StringBuilder cookiesStr = new StringBuilder();
        for (HttpCookie c : cookies) {
            c.setVersion(0);
            cookiesStr.append(c);
            cookiesStr.append("; ");
        }

        Map<String, Object> requestContext = ((BindingProvider) soap).getRequestContext();
        Map<String, List<String>> headers = (Map<String, List<String>>) requestContext.get(MessageContext.HTTP_REQUEST_HEADERS);
        headers.put(HTTPConstants.HEADER_COOKIE, Collections.singletonList(cookiesStr.toString()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clearCookies() {
        Map<String, Object> requestContext = ((BindingProvider) soap).getRequestContext();
        Map<String, List<String>> headers = (Map<String, List<String>>) requestContext.get(MessageContext.HTTP_REQUEST_HEADERS);
        headers.remove(HTTPConstants.HEADER_COOKIE);
    }
}
