package org.xblackcat.rojac.service.janus;

import org.apache.commons.httpclient.ConnectionPoolTimeoutException;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

/**
 * Date: 15 трав 2007
 *
 * @author ASUS
 */

public class TracingMultiThreadedHttpConnectionManager extends MultiThreadedHttpConnectionManager {
    public HttpConnection getConnectionWithTimeout(HostConfiguration hostConfiguration, long timeout) throws ConnectionPoolTimeoutException {
        return new TracingHTTPConnection(super.getConnectionWithTimeout(hostConfiguration, timeout));
    }
}
