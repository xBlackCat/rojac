package org.xblackcat.sunaj.service.soap.data;

/**
 * Date: 14.04.2007
 *
 * @author ASUS
 */

public final class PostInfo {
    private final int[] commited;
    private final PostExceptionInfo[] exceptions;

    public PostInfo(int[] commited, ru.rsdn.Janus.PostExceptionInfo[] exceptions) {
        this.commited = commited;
        
        this.exceptions = new PostExceptionInfo[exceptions.length];
        for (int i = 0; i < exceptions.length; i++) {
            this.exceptions[i] = new PostExceptionInfo(exceptions[i]);
        }
    }

    public int[] getCommited() {
        return commited;
    }

    public PostExceptionInfo[] getExceptions() {
        return exceptions;
    }
}
