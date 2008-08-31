package org.xblackcat.rojac.service.options;

/**
 * Date: 12 лип 2008
 *
 * @author xBlackCat
 */

public class Password {
    private final char[] password;

    public Password(char[] password) {
        this.password = password;
    }

    public char[] getPassword() {
        return password;
    }

    public String toString() {
        return new String(password);
    }
}
