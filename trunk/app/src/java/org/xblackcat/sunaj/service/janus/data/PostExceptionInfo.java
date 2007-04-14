package org.xblackcat.sunaj.service.janus.data;

/**
 * Date: 14.04.2007
 *
 * @author ASUS
 */

public final class PostExceptionInfo {
    private final String exception;
    private final int localMessageId;
    private final String info;

    public PostExceptionInfo(String exception, int localMessageId, String info) {
        this.exception = exception;
        this.localMessageId = localMessageId;
        this.info = info;
    }

    public PostExceptionInfo(ru.rsdn.Janus.PostExceptionInfo i) {
        this(i.getException(), i.getLocalMessageId(), i.getInfo());
    }

    public String getException() {
        return exception;
    }

    public int getLocalMessageId() {
        return localMessageId;
    }

    public String getInfo() {
        return info;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PostExceptionInfo that = (PostExceptionInfo) o;

        if (localMessageId != that.localMessageId) return false;
        if (!exception.equals(that.exception)) return false;
        if (!info.equals(that.info)) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = exception.hashCode();
        result = 31 * result + localMessageId;
        result = 31 * result + info.hashCode();
        return result;
    }

    public String toString() {
        StringBuilder str = new StringBuilder("PostException[");
        str.append("exception=").append(exception).append(", ");
        str.append("localMessageId=").append(localMessageId).append(", ");
        str.append("info=").append(info).append(']');
        return str.toString();
    }
}
