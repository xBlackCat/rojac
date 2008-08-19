package org.xblackcat.sunaj;

/**
 * Date: 8 лип 2008
 *
 * @author xBlackCat
 */

public class SunajException extends Exception {
    public SunajException() {
    }

    public SunajException(String message) {
        super(message);
    }

    public SunajException(String message, Throwable cause) {
        super(message, cause);
    }

    public SunajException(Throwable cause) {
        super(cause);
    }
}
