package org.xblackcat.rojac.service.commands;

import org.xblackcat.rojac.RojacException;

/**
 * Date: 12 трав 2007
 *
 * @author ASUS
 */

public class RsdnProcessorException extends RojacException {
    public RsdnProcessorException() {
    }

    public RsdnProcessorException(String message) {
        super(message);
    }

    public RsdnProcessorException(String message, Throwable cause) {
        super(message, cause);
    }

    public RsdnProcessorException(Throwable cause) {
        super(cause);
    }
}
