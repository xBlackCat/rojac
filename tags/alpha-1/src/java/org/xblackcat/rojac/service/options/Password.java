package org.xblackcat.rojac.service.options;

/**
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
