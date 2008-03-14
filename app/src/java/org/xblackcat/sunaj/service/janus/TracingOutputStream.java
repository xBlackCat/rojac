package org.xblackcat.sunaj.service.janus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Date: 15 трав 2007
 *
 * @author ASUS
 */

public class TracingOutputStream extends OutputStream {
    private static final Log log = LogFactory.getLog(TracingOutputStream.class);

    private final OutputStream stream;

    public TracingOutputStream(OutputStream stream) {
        this.stream = stream;
    }

    public void write(int b) throws IOException {
        stream.write(b);
    }

    public void write(byte b[]) throws IOException {
        super.write(b);
        if (log.isDebugEnabled()) {
            log.debug(b.length + " byte(s) written.");
        }
    }

    public void write(byte b[], int off, int len) throws IOException {
        super.write(b, off, len);
        if (log.isDebugEnabled()) {
            log.debug(len + " byte(s) written.");
        }
    }
}
