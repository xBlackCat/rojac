package org.xblackcat.rojac.service.janus.data;

import org.apache.commons.lang3.ArrayUtils;
import ru.rsdn.janus.PostExceptionInfo;

import java.util.List;

/**
 * @author ASUS
 */

public class PostInfo {
    private final int[] commited;
    private final PostException[] exceptions;

    public PostInfo(List<Integer> commited, List<PostExceptionInfo> exceptions) {
        this.commited = ArrayUtils.toPrimitive(commited.toArray(new Integer[commited.size()]));

        this.exceptions = new PostException[exceptions.size()];
        int i = 0;
        for (PostExceptionInfo pe : exceptions) {
            this.exceptions[i++] = new PostException(pe);
        }
    }

    public int[] getCommited() {
        return commited;
    }

    public PostException[] getExceptions() {
        return exceptions;
    }
}
