package org.xblackcat.rojac.service.janus;

import org.apache.axis.client.Call;
import org.apache.axis.configuration.SimpleProvider;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.NewMessage;
import org.xblackcat.rojac.data.NewModerate;
import org.xblackcat.rojac.data.NewRating;
import org.xblackcat.rojac.data.Version;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.janus.data.ForumsList;
import org.xblackcat.rojac.service.janus.data.NewData;
import org.xblackcat.rojac.service.janus.data.PostInfo;
import org.xblackcat.rojac.service.janus.data.TopicMessages;
import org.xblackcat.rojac.service.janus.data.UsersList;
import org.xblackcat.rojac.util.RojacUtils;
import ru.rsdn.Janus.*;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.util.Hashtable;

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

        try {
            soap.check();
        } catch (RemoteException e) {
            throw new JanusServiceException(e);
        }
        if (log.isDebugEnabled()) {
            log.debug("Connection has been established.");
        }
    }

    @Override
    public ForumsList getForumsList(Version verRow) throws JanusServiceException {
        log.info("Retrieve the forums list from the Janus WS.");

        ForumResponse list;
        try {
            list = soap.getForumList(new ForumRequest(userName, password, verRow.getBytes()));
        } catch (RemoteException e) {
            throw new JanusServiceException("Can not obtain the forum list.", e);
        }

        JanusForumInfo[] forumInfos = list.getForumList();
        JanusForumGroupInfo[] groupInfos = list.getGroupList();
        Version version = new Version(list.getForumsRowVersion());
        if (log.isDebugEnabled()) {
            log.debug("Got " + forumInfos.length + " forum(s) in " + groupInfos.length + " group(s). Version: " + version);
        }
        return new ForumsList(forumInfos, groupInfos, version);
    }

    @Override
    public UsersList getNewUsers(Version verRow, int maxOutput) throws JanusServiceException {
        log.info("Retrieve the users list from the Janus WS. Limit = " + maxOutput);

        UserResponse list;
        try {
            list = soap.getNewUsers(new UserRequest(userName, password, verRow.getBytes(), maxOutput));
        } catch (RemoteException e) {
            throw new JanusServiceException("Can not obtain the new users list.", e);
        }
        if (log.isDebugEnabled()) {
            log.debug("Got " + list.getUsers().length + " new user(s)");
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
        try {
            list = soap.getTopicByMessage(new TopicRequest(userName, password, messageIds));
        } catch (RemoteException e) {
            throw new JanusServiceException("Can not obtain extra messages.", e);
        }
        JanusMessageInfo[] messages = list.getMessages();
        JanusModerateInfo[] moderate = list.getModerate();
        JanusRatingInfo[] rating = list.getRating();
        if (log.isDebugEnabled()) {
            log.debug("Got " + messages.length + " message(s), " + moderate.length + " modarators note(s) and " + rating.length + " user raiting(s).");
        }
        return new TopicMessages(messages, moderate, rating);
    }

    @Override
    public PostInfo commitChanges() throws JanusServiceException {
        log.info("Commit the local changes to the Janus WS.");

        PostResponse post;
        try {
            post = soap.postChangeCommit();
        } catch (RemoteException e) {
            throw new JanusServiceException("Can not commit changes.", e);
        }
        PostExceptionInfo[] exceptions = post.getExceptions();
        int[] ids = post.getCommitedIds();
        if (log.isDebugEnabled()) {
            log.debug(ids.length + " message(s) commited and " + exceptions.length + " exception(s) occurs.");
        }
        return new PostInfo(ids, exceptions);
    }

    @Override
    public void postChanges(NewMessage[] messages, NewRating[] ratings, NewModerate[] moderates) throws JanusServiceException {
        log.info("Post the changes to the Janus WS.");

        try {
            PostRequest postRequest = new PostRequest(
                    userName,
                    password,
                    RojacUtils.getRSDNObject(messages),
                    RojacUtils.getRSDNObject(ratings),
                    RojacUtils.getRSDNObject(moderates));
            soap.postChange(postRequest);
        } catch (RemoteException e) {
            throw new JanusServiceException("Can not post changes to RSDN.", e);
        }
    }

    @Override
    public NewData getNewData(RequestForumInfo[] requestForumInfos, Version ratingVer, Version messageVer, Version moderateVer, int[] breakMsgIds, int[] breakTopicIds, int maxOutput) throws JanusServiceException {
        if (log.isInfoEnabled()) {
            log.info("Retrieve the messages from the Janus WS. Portion limit = " + maxOutput);
        }

        ChangeResponse list;
        try {
            list = soap.getNewData(
                    new ChangeRequest(
                            userName,
                            password,
                            requestForumInfos,
                            ratingVer.getBytes(),
                            messageVer.getBytes(),
                            moderateVer.getBytes(),
                            breakMsgIds,
                            breakTopicIds,
                            maxOutput
                    )
            );
        } catch (RemoteException e) {
            throw new JanusServiceException("Can not obtain the new data.", e);
        }
        Version forumRowVersion = new Version(list.getLastForumRowVersion());
        Version ratingRowVersion = new Version(list.getLastRatingRowVersion());
        Version moderateRowVersion = new Version(list.getLastModerateRowVersion());

        JanusMessageInfo[] newMessages = list.getNewMessages();
        JanusModerateInfo[] newModerate = list.getNewModerate();
        JanusRatingInfo[] newRating = list.getNewRating();

        int ownId = list.getUserId();

        if (log.isDebugEnabled()) {
            log.debug("New data statistics: " +
                    "own user id is " + ownId + ", " +
                    newMessages.length + " new message(s), " +
                    newRating.length + " new rating(s), " +
                    newModerate.length + " new moderate info(s)" +
                    ", messages version is " + forumRowVersion +
                    ", ratings version is " + ratingRowVersion +
                    ", moderates version is " + moderateRowVersion
            );
        }
        return new NewData(ownId, forumRowVersion, ratingRowVersion, moderateRowVersion, newMessages, newModerate, newRating);
    }

    @SuppressWarnings({"unchecked"})
    public void init(boolean useCompression) throws JanusServiceException {
        try {
            log.info("Janus SOAP service initialization has been started.");

            SimpleProvider provider = new SimpleProvider();

            provider.deployTransport("http", new JanusHTTPSender(ServiceFactory.getInstance().getProgressControl()));
            JanusATLocator jl = new JanusATLocator(provider);

            JanusATSoapStub soap = (JanusATSoapStub) jl.getJanusATSoap();

            // Compress the request
            if (useCompression) {
                log.info("Data compression is enabled.");
                // Tell the server it can compress the response
                soap._setProperty(HTTPConstants.MC_ACCEPT_GZIP, Boolean.TRUE);
//                soap._setProperty(HTTPConstants.MC_GZIP_REQUEST, Boolean.TRUE);
            }
            soap._setProperty(Call.SESSION_MAINTAIN_PROPERTY, Boolean.TRUE);
            Hashtable rh = new Hashtable();
            rh.put(HTTPConstants.HEADER_TRANSFER_ENCODING_CHUNKED, Boolean.FALSE);
            rh.put(HTTPConstants.HEADER_CONNECTION, HTTPConstants.HEADER_CONNECTION_CLOSE);
            rh.put(HTTPConstants.HEADER_USER_AGENT, RojacUtils.VERSION_STRING);
            soap._setProperty(HTTPConstants.REQUEST_HEADERS, rh);

            this.soap = soap;
            log.info("Initialization has done.");
        } catch (ServiceException e) {
            throw new JanusServiceException(e);
        }
    }
}
