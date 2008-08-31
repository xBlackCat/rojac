package org.xblackcat.rojac.service.janus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Date: 15 трав 2007
 *
 * @author ASUS
 */

public class TracingInputStream extends InputStream {
    private static final Log log = LogFactory.getLog(TracingInputStream.class);

    private final InputStream stream;

    public TracingInputStream(InputStream strm) {
        stream = strm;
    }

    public int read() throws IOException {
        return stream.read();
    }

    public int read(byte b[]) throws IOException {
        int read = super.read(b);
        if (log.isDebugEnabled()) {
            log.debug(read + " byte(s) read.");
        }
        return read;
    }

    public int read(byte b[], int off, int len) throws IOException {
        int read = super.read(b, off, len);
        if (log.isDebugEnabled()) {
            log.debug(read + " byte(s) read.");
        }
        return read;
    }
}
