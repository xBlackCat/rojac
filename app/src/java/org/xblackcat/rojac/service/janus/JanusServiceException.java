package org.xblackcat.rojac.service.janus;

import org.xblackcat.rojac.RojacException;

/**
 * Date: 10 квіт 2007
 *
 * @author Alexey
 */

public class JanusServiceException extends RojacException {
    public JanusServiceException() {
    }

    public JanusServiceException(Throwable cause) {
        super(cause);
    }

    public JanusServiceException(String message) {
        super(message);
    }

    public JanusServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
