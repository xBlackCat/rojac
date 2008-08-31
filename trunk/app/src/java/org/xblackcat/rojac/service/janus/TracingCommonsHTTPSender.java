package org.xblackcat.rojac.service.janus;

import org.apache.axis.components.net.CommonsHTTPClientPropertiesFactory;
import org.apache.axis.transport.http.CommonsHTTPSender;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

/**
 * Date: 15 трав 2007
 *
 * @author ASUS
 */

public class TracingCommonsHTTPSender extends CommonsHTTPSender {
    protected void initialize() {
        MultiThreadedHttpConnectionManager cm = new TracingMultiThreadedHttpConnectionManager();
        this.clientProperties = CommonsHTTPClientPropertiesFactory.create();
        cm.getParams().setDefaultMaxConnectionsPerHost(clientProperties.getMaximumConnectionsPerHost());
        cm.getParams().setMaxTotalConnections(clientProperties.getMaximumTotalConnections());
        // If defined, set the default timeouts
        // Can be overridden by the MessageContext
        if(this.clientProperties.getDefaultConnectionTimeout()>0) {
           cm.getParams().setConnectionTimeout(this.clientProperties.getDefaultConnectionTimeout());
        }
        if(this.clientProperties.getDefaultSoTimeout()>0) {
           cm.getParams().setSoTimeout(this.clientProperties.getDefaultSoTimeout());
        }
        this.connectionManager = cm;

    }
}
