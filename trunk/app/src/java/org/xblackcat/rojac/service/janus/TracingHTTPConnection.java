package org.xblackcat.rojac.service.janus;

import org.apache.commons.httpclient.HttpConnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Date: 15 трав 2007
 *
 * @author ASUS
 */

public class TracingHTTPConnection extends HttpConnection {
    public TracingHTTPConnection(HttpConnection c) {
        super(c.getHost(), c.getPort(), c.getProtocol());
    }

    public OutputStream getRequestOutputStream() throws IOException, IllegalStateException {
        return new TracingOutputStream(super.getRequestOutputStream());
    }

    public InputStream getResponseInputStream() throws IOException, IllegalStateException {
        return new TracingInputStream(super.getResponseInputStream());
    }
}
