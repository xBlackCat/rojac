package org.xblackcat.sunaj.service.soap;

import org.apache.axis.Handler;
import org.apache.axis.SimpleChain;
import org.apache.axis.SimpleTargetedChain;
import org.apache.axis.client.AxisClient;
import org.apache.axis.configuration.SimpleProvider;
import org.apache.axis.transport.http.CommonsHTTPSender;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.axis.transport.http.HTTPTransport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.sunaj.service.soap.data.ForumsList;
import org.xblackcat.sunaj.service.soap.data.UsersList;
import ru.rsdn.Janus.*;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;

/**
 * Date: 10 квіт 2007
 *
 * @author Alexey
 */

public class JanusService implements IJanusService {
    private static final Log log = LogFactory.getLog(JanusService.class);

    private final String userName;
    private final String password;

    private JanusATSoap soap;

    public static IJanusService getInstance(String userName, String password) throws JanusServiceException {
        try {
            JanusService c = new JanusService(userName, password);
            c.init();
            c.testConnection();
            return c;
        } catch (ServiceException e) {
            throw new JanusServiceException(e);
        }
    }

    public JanusService(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public void testConnection() throws JanusServiceException {
        if (log.isDebugEnabled()) {
            log.debug("Perform check connection.");
        }
        try {
            soap.check();
        } catch (RemoteException e) {
            throw new JanusServiceException(e);
        }
        if (log.isDebugEnabled()) {
            log.debug("Check have done successfuly.");
        }
    }

    public ForumsList getForumsList() throws JanusServiceException {
        if (log.isDebugEnabled()) {
            log.debug("Retrieve the forums list from the Janus WS.");
        }
        ForumResponse list;
        try {
            list = soap.getForumList(new ForumRequest(userName, password));
        } catch (RemoteException e) {
            throw new JanusServiceException("Can not obtain the forum list.", e);
        }
        JanusForumInfo[] forumInfos = list.getForumList();
        JanusForumGroupInfo[] groupInfos = list.getGroupList();
        if (log.isDebugEnabled()) {
            log.debug("Have got " + forumInfos.length + " forum(s) in " + groupInfos.length + " group(s).");
        }
        return new ForumsList(forumInfos, groupInfos);
    }

    public UsersList getNewUsers(byte[] verRow) throws JanusServiceException {
        if (log.isDebugEnabled()) {
            log.debug("Retrieve the users list from the Janus WS.");
        }
        UserResponse list;
        try {
            list = soap.getNewUsers(new UserRequest(userName, password, verRow, 100));
        } catch (RemoteException e) {
            throw new JanusServiceException("Can not obtain the new users list.", e);
        }
        return new UsersList(list);
    }

    private void init() throws ServiceException {
        if (log.isDebugEnabled()) {
            log.debug("Start Janus SAOP service initialization for user " + userName);
        }
        JanusATLocator jl = new JanusATLocator();
        SimpleProvider clientConfig = new SimpleProvider();
        SimpleChain reqHandler = new SimpleChain();
        SimpleChain respHandler = new SimpleChain();

        Handler pivot = new CommonsHTTPSender();
        Handler transport = new SimpleTargetedChain(reqHandler, pivot, respHandler);
        clientConfig.deployTransport(HTTPTransport.DEFAULT_TRANSPORT_NAME, transport);
        jl.setEngineConfiguration(clientConfig);
        jl.setEngine(new AxisClient(clientConfig));
        jl.setMaintainSession(true);

        JanusATSoapStub soap = (JanusATSoapStub) jl.getJanusATSoap();

        // Compress the request
//		soap._setProperty(HTTPConstants.MC_GZIP_REQUEST, Boolean.TRUE);
        // Tell the server it can compress the response
        soap._setProperty(HTTPConstants.MC_ACCEPT_GZIP, Boolean.TRUE);

        this.soap = soap;
        // set true to instruct axis client to send cookies
        if (log.isDebugEnabled()) {
            log.debug("Initialization has done successfuly.");
        }
    }

}
