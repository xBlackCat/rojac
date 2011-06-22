/*
 * Copyright 2001-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xblackcat.rojac.service.janus;

import org.apache.axis.AxisFault;
import org.apache.axis.Constants;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.components.logger.LogFactory;
import org.apache.axis.components.net.CommonsHTTPClientProperties;
import org.apache.axis.components.net.CommonsHTTPClientPropertiesFactory;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.soap.SOAP12Constants;
import org.apache.axis.soap.SOAPConstants;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.axis.utils.JavaUtils;
import org.apache.axis.utils.Messages;
import org.apache.axis.utils.NetworkUtils;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.xblackcat.rojac.service.progress.IProgressController;

import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.xblackcat.rojac.i18n.Message.*;
import static org.xblackcat.rojac.service.options.Property.*;

/**
 * This class uses Jakarta Commons's HttpClient to call a SOAP server. Modification of CommonsHTTPSender to fit Janus WS
 * requirements.
 *
 * @author xBlackCat
 * @author Davanum Srinivas (dims@yahoo.com) History: By Chandra Talluri Modifications done for maintaining sessions.
 *         Cookies needed to be set on HttpState not on MessageContext, since ttpMethodBase overwrites the cookies from
 *         HttpState. Also we need to setCookiePolicy on HttpState to CookiePolicy.COMPATIBILITY else it is defaulting
 *         to RFC2109Spec and adding Version information to it and tomcat server not recognizing it
 */
class JanusHTTPSender extends BasicHandler {
    private final IProgressController progressController;

    /**
     * Field log
     */
    protected static Log log =
            LogFactory.getLog(JanusHTTPSender.class.getName());

    protected HttpConnectionManager connectionManager;
    protected CommonsHTTPClientProperties clientProperties;
    boolean httpChunkStream = true; //Use HTTP chunking or not.

    public JanusHTTPSender(IProgressController progressController) {
        this.progressController = progressController;

        initialize();
    }

    protected void initialize() {
        MultiThreadedHttpConnectionManager cm = new MultiThreadedHttpConnectionManager();
        this.clientProperties = CommonsHTTPClientPropertiesFactory.create();
        cm.getParams().setDefaultMaxConnectionsPerHost(clientProperties.getMaximumConnectionsPerHost());
        cm.getParams().setMaxTotalConnections(clientProperties.getMaximumTotalConnections());
        // If defined, set the default timeouts
        // Can be overridden by the MessageContext
        if (this.clientProperties.getDefaultConnectionTimeout() > 0) {
            cm.getParams().setConnectionTimeout(this.clientProperties.getDefaultConnectionTimeout());
        }
        if (this.clientProperties.getDefaultSoTimeout() > 0) {
            cm.getParams().setSoTimeout(this.clientProperties.getDefaultSoTimeout());
        }
        this.connectionManager = cm;
    }

    /**
     * invoke creates a socket connection, sends the request SOAP message and then reads the response SOAP message back
     * from the SOAP server
     *
     * @param msgContext the messsage context
     *
     * @throws org.apache.axis.AxisFault
     */
    public void invoke(MessageContext msgContext) throws AxisFault {
        HttpMethodBase method;
        if (log.isDebugEnabled()) {
            log.debug(Messages.getMessage("enter00",
                    "JanusHTTPSender::invoke"));
        }
        try {
            URL targetURL =
                    new URL(msgContext.getStrProp(MessageContext.TRANS_URL));

            // no need to retain these, as the cookies/credentials are
            // stored in the message context across multiple requests.
            // the underlying connection manager, however, is retained
            // so sockets get recycled when possible.
            HttpClient httpClient = new HttpClient(this.connectionManager);
            // the timeout value for allocation of connections from the pool
            httpClient.getParams().setConnectionManagerTimeout(this.clientProperties.getConnectionPoolTimeout());

            HostConfiguration hostConfiguration =
                    getHostConfiguration(httpClient, targetURL);

            boolean posting = true;

            // If we're SOAP 1.2, allow the web method to be set from the
            // MessageContext.
            if (msgContext.getSOAPConstants() == SOAPConstants.SOAP12_CONSTANTS) {
                String webMethod = msgContext.getStrProp(SOAP12Constants.PROP_WEBMETHOD);
                if (webMethod != null) {
                    posting = webMethod.equals(HTTPConstants.HEADER_POST);
                }
            }

            if (posting) {
                Message reqMessage = msgContext.getRequestMessage();
                method = new PostMethod(targetURL.toString());

                // set false as default, addContetInfo can overwrite
                method.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, false);

                addContextInfo(method, httpClient, msgContext, targetURL);

                MessageRequestEntity requestEntity;
                if (msgContext.isPropertyTrue(HTTPConstants.MC_GZIP_REQUEST)) {
                    requestEntity = new GzipMessageRequestEntity(method, reqMessage, httpChunkStream);
                } else {
                    requestEntity = new MessageRequestEntity(method, reqMessage, httpChunkStream);
                }
                ((PostMethod) method).setRequestEntity(requestEntity);
            } else {
                method = new GetMethod(targetURL.toString());
                addContextInfo(method, httpClient, msgContext, targetURL);
            }

            String httpVersion =
                    msgContext.getStrProp(MessageContext.HTTP_TRANSPORT_VERSION);
            if (httpVersion != null) {
                if (httpVersion.equals(HTTPConstants.HEADER_PROTOCOL_V10)) {
                    method.getParams().setVersion(HttpVersion.HTTP_1_0);
                }
                // assume 1.1
            }

            // don't forget the cookies!
            // Cookies need to be set on HttpState, since HttpMethodBase
            // overwrites the cookies from HttpState
            if (msgContext.getMaintainSession()) {
                HttpState state = httpClient.getState();
                method.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
                method.getParams().setBooleanParameter(HttpMethodParams.SINGLE_COOKIE_HEADER, true);
                String host = hostConfiguration.getHost();
                String path = targetURL.getPath();
                boolean secure = hostConfiguration.getProtocol().isSecure();
                fillHeaders(msgContext, state, HTTPConstants.HEADER_COOKIE, host, path, secure);
                fillHeaders(msgContext, state, HTTPConstants.HEADER_COOKIE2, host, path, secure);
                httpClient.setState(state);
            }

            int returnCode = httpClient.executeMethod(hostConfiguration, method, null);

            String contentType =
                    getHeader(method, HTTPConstants.HEADER_CONTENT_TYPE);
            String contentLocation =
                    getHeader(method, HTTPConstants.HEADER_CONTENT_LOCATION);
            String contentLength =
                    getHeader(method, HTTPConstants.HEADER_CONTENT_LENGTH);

            if ((returnCode > 199) && (returnCode < 300)) {

                // SOAP return is OK - so fall through
            } else if (msgContext.getSOAPConstants() ==
                    SOAPConstants.SOAP12_CONSTANTS) {
                // For now, if we're SOAP 1.2, fall through, since the range of
                // valid result codes is much greater
            } else if ((contentType != null) && !contentType.equals("text/html")
                    && ((returnCode > 499) && (returnCode < 600))) {

                // SOAP Fault should be in here - so fall through
            } else {
                String statusMessage = method.getStatusText();
                AxisFault fault = new AxisFault("HTTP",
                        "(" + returnCode + ")"
                                + statusMessage, null,
                        null);

                try {
                    fault.setFaultDetailString(
                            Messages.getMessage("return01",
                                    "" + returnCode,
                                    method.getResponseBodyAsString()));
                    fault.addFaultDetail(Constants.QNAME_FAULTDETAIL_HTTPERRORCODE,
                            Integer.toString(returnCode));
                    throw fault;
                } finally {
                    method.releaseConnection(); // release connection back to pool.
                }
            }

            if (method.getResponseContentLength() < 0) {
                progressController.fireJobProgressChanged(0f, Synchronize_Message_ReadUnknown);
            } else {
                progressController.fireJobProgressChanged(0f, Synchronize_Message_Read, method.getResponseContentLength());
            }
            // wrap the response body stream so that close() also releases
            // the connection back to the pool.
            InputStream releaseConnectionOnCloseStream =
                    createConnectionReleasingInputStream(method);

            Header contentEncoding =
                    method.getResponseHeader(HTTPConstants.HEADER_CONTENT_ENCODING);
            if (contentEncoding != null) {
                if (contentEncoding.getValue().
                        equalsIgnoreCase(HTTPConstants.COMPRESSION_GZIP)) {
                    releaseConnectionOnCloseStream =
                            new GZIPInputStream(releaseConnectionOnCloseStream);
                } else {
                    throw new AxisFault("HTTP",
                            "unsupported content-encoding of '"
                                    + contentEncoding.getValue()
                                    + "' found", null, null);
                }

            }
            Message outMsg = new Message(releaseConnectionOnCloseStream,
                    false, contentType, contentLocation);
            // Transfer HTTP headers of HTTP message to MIME headers of SOAP message
            Header[] responseHeaders = method.getResponseHeaders();
            MimeHeaders responseMimeHeaders = outMsg.getMimeHeaders();
            for (Header responseHeader : responseHeaders) {
                responseMimeHeaders.addHeader(responseHeader.getName(),
                        responseHeader.getValue());
            }
            outMsg.setMessageType(Message.RESPONSE);
            msgContext.setResponseMessage(outMsg);
//            if (log.isDebugEnabled()) {
//                if (null == contentLength) {
//                    log.debug("\n"
//                            + Messages.getMessage("no00", "Content-Length"));
//                }
//                log.debug("\n" + Messages.getMessage("xmlRecd00"));
//                log.debug("-----------------------------------------------");
//                log.debug(outMsg.getSOAPPartAsString());
//            }

            // if we are maintaining session state,
            // handle cookies (if any)
            if (msgContext.getMaintainSession()) {
                Header[] headers = method.getResponseHeaders();

                for (Header header : headers) {
                    if (header.getName().equalsIgnoreCase(HTTPConstants.HEADER_SET_COOKIE)) {
                        handleCookie(HTTPConstants.HEADER_COOKIE, header.getValue(), msgContext);
                    } else if (header.getName().equalsIgnoreCase(HTTPConstants.HEADER_SET_COOKIE2)) {
                        handleCookie(HTTPConstants.HEADER_COOKIE2, header.getValue(), msgContext);
                    }
                }
            }

            // always release the connection back to the pool if
            // it was one way invocation
            if (msgContext.isPropertyTrue("axis.one.way")) {
                method.releaseConnection();
            }

        } catch (Exception e) {
            log.debug(e);
            throw AxisFault.makeFault(e);
        }

        if (log.isDebugEnabled()) {
            log.debug(Messages.getMessage("exit00",
                    "JanusHTTPSender::invoke"));
        }
    }

    /**
     * little helper function for cookies. fills up the message context with a string or an array of strings (if there
     * are more than one Set-Cookie)
     *
     * @param cookieName
     * @param cookie
     * @param msgContext
     */
    public void handleCookie(String cookieName, String cookie,
                             MessageContext msgContext) {

        cookie = cleanupCookie(cookie);
        int keyIndex = cookie.indexOf("=");
        String key = (keyIndex != -1) ? cookie.substring(0, keyIndex) : cookie;

        List<String> cookies = new ArrayList<String>();
        Object oldCookies = msgContext.getProperty(cookieName);
        boolean alreadyExist = false;
        if (oldCookies != null) {
            if (oldCookies instanceof String[]) {
                String[] oldCookiesArray = (String[]) oldCookies;
                for (String anOldCookie : oldCookiesArray) {
                    if (key != null && anOldCookie.indexOf(key) == 0) { // same cookie key
                        anOldCookie = cookie;             // update to new one
                        alreadyExist = true;
                    }
                    cookies.add(anOldCookie);
                }
            } else {
                String oldCookie = (String) oldCookies;
                if (key != null && oldCookie.indexOf(key) == 0) { // same cookie key
                    oldCookie = cookie;             // update to new one
                    alreadyExist = true;
                }
                cookies.add(oldCookie);
            }
        }

        if (!alreadyExist) {
            cookies.add(cookie);
        }

        if (cookies.size() == 1) {
            msgContext.setProperty(cookieName, cookies.get(0));
        } else if (cookies.size() > 1) {
            msgContext.setProperty(cookieName, cookies.toArray(new String[cookies.size()]));
        }
    }

    /**
     * Add cookies from message context
     *
     * @param msgContext
     * @param state
     * @param header
     * @param host
     * @param path
     * @param secure
     */
    private void fillHeaders(MessageContext msgContext, HttpState state, String header, String host, String path, boolean secure) {
        Object ck1 = msgContext.getProperty(header);
        if (ck1 != null) {
            if (ck1 instanceof String[]) {
                String[] cookies = (String[]) ck1;
                for (String cooky : cookies) {
                    addCookie(state, cooky, host, path, secure);
                }
            } else {
                addCookie(state, (String) ck1, host, path, secure);
            }
        }
    }

    /**
     * add cookie to state
     *
     * @param state
     * @param cookie
     */
    private void addCookie(HttpState state, String cookie, String host, String path, boolean secure) {
        int index = cookie.indexOf('=');
        state.addCookie(new Cookie(host, cookie.substring(0, index),
                cookie.substring(index + 1), path,
                null, secure));
    }

    /**
     * cleanup the cookie value.
     *
     * @param cookie initial cookie value
     *
     * @return a cleaned up cookie value.
     */
    private String cleanupCookie(String cookie) {
        cookie = cookie.trim();
        // chop after first ; a la Apache SOAP (see HTTPUtils.java there)
        int index = cookie.indexOf(';');
        if (index != -1) {
            cookie = cookie.substring(0, index);
        }
        return cookie;
    }

    protected HostConfiguration getHostConfiguration(HttpClient client,
                                                     URL targetURL) {
        int port = targetURL.getPort();
        boolean useProxy = SYNCHRONIZER_PROXY_ENABLED.get(false);

        HostConfiguration config = new HostConfiguration();

        if (port == -1) {
            if (targetURL.getProtocol().equalsIgnoreCase("https")) {
                port = 443;        // default port for https being 443
            } else { // it must be http
                port = 80;        // default port for http being 80
            }
        }

        config.setHost(targetURL.getHost(), port, targetURL.getProtocol());
        if (!useProxy) {
        } else {
            String pHost = SYNCHRONIZER_PROXY_HOST.get("");
            Integer pPort = SYNCHRONIZER_PROXY_PORT.get(0);
            if (pHost.length() == 0 || pPort == 0) {
                config.setHost(targetURL.getHost(), port, targetURL.getProtocol());
            } else {
                progressController.fireJobProgressChanged(0f, Synchronize_Message_ProxyUsed, pHost, pPort);
                String pUser = SYNCHRONIZER_PROXY_USER.get("");
                if (pUser.length() != 0) {
                    String pPass = SYNCHRONIZER_PROXY_PASSWORD.get().toString();
                    Credentials proxyCred =
                            new UsernamePasswordCredentials(pUser, pPass);
                    // if the username is in the form "user\domain"
                    // then use NTCredentials instead.
                    int domainIndex = pUser.indexOf("\\");
                    if (domainIndex > 0) {
                        String domain = pUser.substring(0, domainIndex);
                        if (pUser.length() > domainIndex + 1) {
                            String user = pUser.substring(domainIndex + 1);
                            proxyCred = new NTCredentials(user, pPass, pHost, domain);
                        }
                    }
                    client.getState().setProxyCredentials(AuthScope.ANY, proxyCred);
                }
                config.setProxy(pHost, pPort);
            }
        }
        return config;
    }

    /**
     * Extracts info from message context.
     *
     * @param method     Post method
     * @param httpClient The client used for posting
     * @param msgContext the message context
     * @param tmpURL     the url to post to.
     *
     * @throws Exception
     */
    private void addContextInfo(HttpMethodBase method,
                                HttpClient httpClient,
                                MessageContext msgContext,
                                URL tmpURL)
            throws Exception {

        // optionally set a timeout for the request
        if (msgContext.getTimeout() != 0) {
            /* ISSUE: these are not the same, but MessageContext has only one
                      definition of timeout */
            // SO_TIMEOUT -- timeout for blocking reads
            httpClient.getHttpConnectionManager().getParams().setSoTimeout(msgContext.getTimeout());
            // timeout for initial connection
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(msgContext.getTimeout());
        }

        // Get SOAPAction, default to ""
        String action = msgContext.useSOAPAction()
                ? msgContext.getSOAPActionURI()
                : "";

        if (action == null) {
            action = "";
        }

        Message msg = msgContext.getRequestMessage();
        if (msg != null) {
            method.setRequestHeader(new Header(HTTPConstants.HEADER_CONTENT_TYPE,
                    msg.getContentType(msgContext.getSOAPConstants())));
        }
        method.setRequestHeader(new Header(HTTPConstants.HEADER_SOAP_ACTION,
                "\"" + action + "\""));
        String userId = msgContext.getUsername();
        String password = msgContext.getPassword();

        // if UserID is not part of the context, but is in the URL, use
        // the one in the URL.
        if ((userId == null) && (tmpURL.getUserInfo() != null)) {
            String info = tmpURL.getUserInfo();
            int sep = info.indexOf(':');

            if ((sep >= 0) && (sep + 1 < info.length())) {
                userId = info.substring(0, sep);
                password = info.substring(sep + 1);
            } else {
                userId = info;
            }
        }
        if (userId != null) {
            Credentials proxyCred =
                    new UsernamePasswordCredentials(userId,
                            password);
            // if the username is in the form "user\domain"
            // then use NTCredentials instead.
            int domainIndex = userId.indexOf("\\");
            if (domainIndex > 0) {
                String domain = userId.substring(0, domainIndex);
                if (userId.length() > domainIndex + 1) {
                    String user = userId.substring(domainIndex + 1);
                    proxyCred = new NTCredentials(user,
                            password,
                            NetworkUtils.getLocalHostname(), domain);
                }
            }
            httpClient.getState().setCredentials(AuthScope.ANY, proxyCred);
        }

        // add compression headers if needed
        if (msgContext.isPropertyTrue(HTTPConstants.MC_ACCEPT_GZIP)) {
            method.addRequestHeader(HTTPConstants.HEADER_ACCEPT_ENCODING,
                    HTTPConstants.COMPRESSION_GZIP);
        }
        if (msgContext.isPropertyTrue(HTTPConstants.MC_GZIP_REQUEST)) {
            method.addRequestHeader(HTTPConstants.HEADER_CONTENT_ENCODING,
                    HTTPConstants.COMPRESSION_GZIP);
        }

        if (msg != null) {
            // Transfer MIME headers of SOAPMessage to HTTP headers.
            MimeHeaders mimeHeaders = msg.getMimeHeaders();
            if (mimeHeaders != null) {
                for (Iterator i = mimeHeaders.getAllHeaders(); i.hasNext(); ) {
                    MimeHeader mimeHeader = (MimeHeader) i.next();
                    //HEADER_CONTENT_TYPE and HEADER_SOAP_ACTION are already set.
                    //Let's not duplicate them.
                    String headerName = mimeHeader.getName();
                    if (headerName.equals(HTTPConstants.HEADER_CONTENT_TYPE)
                            || headerName.equals(HTTPConstants.HEADER_SOAP_ACTION)) {
                        continue;
                    }
                    method.addRequestHeader(mimeHeader.getName(),
                            mimeHeader.getValue());
                }
            }
        }

        // process user defined headers for information.
        Hashtable<?, ?> userHeaderTable =
                (Hashtable) msgContext.getProperty(HTTPConstants.REQUEST_HEADERS);

        if (userHeaderTable != null) {
            for (Map.Entry me : userHeaderTable.entrySet()) {
                Object keyObj = me.getKey();

                if (null == keyObj) {
                    continue;
                }
                String key = keyObj.toString().trim();
                String value = me.getValue().toString().trim();

                if (key.equalsIgnoreCase(HTTPConstants.HEADER_EXPECT) &&
                        value.equalsIgnoreCase(HTTPConstants.HEADER_EXPECT_100_Continue)) {
                    method.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, true);
                } else if (key.equalsIgnoreCase(HTTPConstants.HEADER_TRANSFER_ENCODING_CHUNKED)) {
                    String val = me.getValue().toString();
                    if (null != val) {
                        httpChunkStream = JavaUtils.isTrue(val);
                    }
                } else {
                    method.addRequestHeader(key, value);
                }
            }
        }
    }

    /**
     * Check if the specified host is in the list of non proxy hosts.
     *
     * @param host          host name
     * @param nonProxyHosts string containing the list of non proxy hosts
     *
     * @return true/false
     */
    protected boolean isHostInNonProxyList(String host, String nonProxyHosts) {

        if ((nonProxyHosts == null) || (host == null)) {
            return false;
        }

        /*
         * The http.nonProxyHosts system property is a list enclosed in
         * double quotes with items separated by a vertical bar.
         */
        StringTokenizer tokenizer = new StringTokenizer(nonProxyHosts, "|\"");

        while (tokenizer.hasMoreTokens()) {
            String pattern = tokenizer.nextToken();

            if (log.isDebugEnabled()) {
                log.debug(Messages.getMessage("match00",
                        new String[]{"HTTPSender",
                                host,
                                pattern}));
            }
            if (match(pattern, host)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Matches a string against a pattern. The pattern contains two special characters: '*' which means zero or more
     * characters,
     *
     * @param pattern the (non-null) pattern to match against
     * @param str     the (non-null) string that must be matched against the pattern
     *
     * @return <code>true</code> when the string matches against the pattern, <code>false</code> otherwise.
     */
    protected static boolean match(String pattern, String str) {

        char[] patArr = pattern.toCharArray();
        char[] strArr = str.toCharArray();
        int patIdxStart = 0;
        int patIdxEnd = patArr.length - 1;
        int strIdxStart = 0;
        int strIdxEnd = strArr.length - 1;
        char ch;
        boolean containsStar = false;

        for (char aPatArr : patArr) {
            if (aPatArr == '*') {
                containsStar = true;
                break;
            }
        }
        if (!containsStar) {

            // No '*'s, so we make a shortcut
            if (patIdxEnd != strIdxEnd) {
                return false;        // Pattern and string do not have the same size
            }
            for (int i = 0; i <= patIdxEnd; i++) {
                ch = patArr[i];
                if (false && (ch != strArr[i])) {
                    return false;    // Character mismatch
                }
                if (Character.toUpperCase(ch)
                        != Character.toUpperCase(strArr[i])) {
                    return false;    // Character mismatch
                }
            }
            return true;             // String matches against pattern
        }
        if (patIdxEnd == 0) {
            return true;    // Pattern contains only '*', which matches anything
        }

        // Process characters before first star
        while ((ch = patArr[patIdxStart]) != '*'
                && (strIdxStart <= strIdxEnd)) {
            if (false && (ch != strArr[strIdxStart])) {
                return false;    // Character mismatch
            }
            if (Character.toUpperCase(ch)
                    != Character.toUpperCase(strArr[strIdxStart])) {
                return false;    // Character mismatch
            }
            patIdxStart++;
            strIdxStart++;
        }
        if (strIdxStart > strIdxEnd) {

            // All characters in the string are used. Check if only '*'s are
            // left in the pattern. If so, we succeeded. Otherwise failure.
            for (int i = patIdxStart; i <= patIdxEnd; i++) {
                if (patArr[i] != '*') {
                    return false;
                }
            }
            return true;
        }

        // Process characters after last star
        while ((ch = patArr[patIdxEnd]) != '*' && (strIdxStart <= strIdxEnd)) {
            if (false && (ch != strArr[strIdxEnd])) {
                return false;    // Character mismatch
            }
            if (Character.toUpperCase(ch)
                    != Character.toUpperCase(strArr[strIdxEnd])) {
                return false;    // Character mismatch
            }
            patIdxEnd--;
            strIdxEnd--;
        }
        if (strIdxStart > strIdxEnd) {

            // All characters in the string are used. Check if only '*'s are
            // left in the pattern. If so, we succeeded. Otherwise failure.
            for (int i = patIdxStart; i <= patIdxEnd; i++) {
                if (patArr[i] != '*') {
                    return false;
                }
            }
            return true;
        }

        // process pattern between stars. padIdxStart and patIdxEnd point
        // always to a '*'.
        while ((patIdxStart != patIdxEnd) && (strIdxStart <= strIdxEnd)) {
            int patIdxTmp = -1;

            for (int i = patIdxStart + 1; i <= patIdxEnd; i++) {
                if (patArr[i] == '*') {
                    patIdxTmp = i;
                    break;
                }
            }
            if (patIdxTmp == patIdxStart + 1) {

                // Two stars next to each other, skip the first one.
                patIdxStart++;
                continue;
            }

            // Find the pattern between padIdxStart & padIdxTmp in str between
            // strIdxStart & strIdxEnd
            int patLength = (patIdxTmp - patIdxStart - 1);
            int strLength = (strIdxEnd - strIdxStart + 1);
            int foundIdx = -1;

            strLoop:
            for (int i = 0; i <= strLength - patLength; i++) {
                for (int j = 0; j < patLength; j++) {
                    ch = patArr[patIdxStart + j + 1];
                    if (false
                            && (ch != strArr[strIdxStart + i + j])) {
                        continue strLoop;
                    }
                    if ((Character
                            .toUpperCase(ch) != Character
                            .toUpperCase(strArr[strIdxStart + i + j]))) {
                        continue strLoop;
                    }
                }
                foundIdx = strIdxStart + i;
                break;
            }
            if (foundIdx == -1) {
                return false;
            }
            patIdxStart = patIdxTmp;
            strIdxStart = foundIdx + patLength;
        }

        // All characters in the string are used. Check if only '*'s are left
        // in the pattern. If so, we succeeded. Otherwise failure.
        for (int i = patIdxStart; i <= patIdxEnd; i++) {
            if (patArr[i] != '*') {
                return false;
            }
        }
        return true;
    }

    private static String getHeader(HttpMethodBase method, String headerName) {
        Header header = method.getResponseHeader(headerName);
        return (header == null) ? null : header.getValue().trim();
    }

    private InputStream createConnectionReleasingInputStream(final HttpMethodBase method) throws IOException {
        return new AutoCloseInputStream(method);
    }

    private class MessageRequestEntity implements RequestEntity {

        private HttpMethodBase method;
        private Message message;
        boolean httpChunkStream = true; //Use HTTP chunking or not.

        public MessageRequestEntity(HttpMethodBase method, Message message, boolean httpChunkStream) {
            this.message = message;
            this.method = method;
            this.httpChunkStream = httpChunkStream;
        }

        public boolean isRepeatable() {
            return true;
        }

        public void writeRequest(OutputStream out) throws IOException {
            progressController.fireJobProgressChanged(0f, Synchronize_Message_Write, message.getContentLength());
            try {
                this.message.writeTo(new LogOutputStream(out, message.getContentLength()));
            } catch (SOAPException e) {
                throw new IOException(e.getMessage());
            }
        }

        protected boolean isContentLengthNeeded() {
            return this.method.getParams().getVersion() == HttpVersion.HTTP_1_0 || !httpChunkStream;
        }

        public long getContentLength() {
            if (isContentLengthNeeded()) {
                try {
                    return message.getContentLength();
                } catch (Exception e) {
                    // Ignore
                }
            }
            return -1; /* -1 for chunked */
        }

        public String getContentType() {
            return null; // a separate header is added
        }

    }

    private class GzipMessageRequestEntity extends MessageRequestEntity {
        public GzipMessageRequestEntity(HttpMethodBase method, Message message, boolean httpChunkStream) {
            super(method, message, httpChunkStream);
        }

        public void writeRequest(OutputStream out) throws IOException {
            if (cachedStream != null) {
                cachedStream.writeTo(out);
            } else {
                GZIPOutputStream gzStream = new GZIPOutputStream(out);
                super.writeRequest(gzStream);
                gzStream.finish();
            }
        }

        public long getContentLength() {
            if (isContentLengthNeeded()) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try {
                    writeRequest(outputStream);
                    cachedStream = outputStream;
                    return outputStream.size();
                } catch (IOException e) {
                    // fall through to doing chunked.
                }
            }
            return -1; // do chunked
        }

        private ByteArrayOutputStream cachedStream;
    }

    private class LogOutputStream extends FilterOutputStream {
        private long amount = 0;
        private final long total;
        private final long chunkSize;

        private long chunkRemain = 0;

        public LogOutputStream(OutputStream out, long contentLength) {
            super(out);
            total = contentLength;

            chunkSize = Math.max(contentLength / 100, 1);
        }

        @Override
        public void write(int b) throws IOException {
            super.write(b);
            logByte();
        }

        private void logByte() {
            amount++;
            if (chunkSize == 1 || chunkRemain == 0) {
                progressController.fireJobProgressChanged(amount, total);
                chunkRemain = chunkSize;
            }
            chunkRemain--;
        }
    }

    private class AutoCloseInputStream extends FilterInputStream {
        private final HttpMethodBase method;
        protected final long total;
        private long amount = 0;

        public AutoCloseInputStream(HttpMethodBase method) throws IOException {
            super(method.getResponseBodyAsStream());
            this.method = method;
            total = method.getResponseContentLength();
            if (total < 0) {
                progressController.fireJobProgressChanged(0, 1);
            }
        }

        public void close() throws IOException {
            try {
                super.close();
            } finally {
                method.releaseConnection();
                progressController.fireJobProgressChanged(1f, Synchronize_Message_WasRead, amount);
            }
        }

        @Override
        public int read() throws IOException {
            logRead(1);
            return super.read();
        }

        private void logRead(long a) {
            if (a > 0) {
                amount += a;
                if (total > 0) {
                    progressController.fireJobProgressChanged(amount, total);
                } else {
                    progressController.fireJobProgressChanged(amount);
                }
            }
        }

        @Override
        public int read(byte[] b) throws IOException {
            int i = super.read(b);
            logRead(i);
            return i;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int i = super.read(b, off, len);
            logRead(i);
            return i;
        }

        @Override
        public long skip(long n) throws IOException {
            long l = super.skip(n);
            logRead(l);
            return l;
        }
    }
}
