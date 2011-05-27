package org.xblackcat.rojac.service.options;

import org.xblackcat.rojac.RojacException;

/**
 * @author xBlackCat
 */

public class OptionsServiceException extends RojacException {
    public OptionsServiceException() {
        super();
    }

    public OptionsServiceException(String message) {
        super(message);
    }

    public OptionsServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public OptionsServiceException(Throwable cause) {
        super(cause);
    }
}
