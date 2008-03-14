package org.xblackcat.sunaj.service.converter;

/**
 * Date: 19 лют 2008
 *
 * @author xBlackCat
 */

public class TagNotProcessedException extends RuntimeException {
    public TagNotProcessedException() {
    }

    public TagNotProcessedException(String message) {
        super(message);
    }

    public TagNotProcessedException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagNotProcessedException(Throwable cause) {
        super(cause);
    }
}
