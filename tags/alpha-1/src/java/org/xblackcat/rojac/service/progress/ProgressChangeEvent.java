package org.xblackcat.rojac.service.progress;

import java.util.EventObject;

/**
 * @author xBlackCat
 */

public class ProgressChangeEvent extends EventObject {
    private ProgressState state;
    private Float progress;
    private String text;

    public ProgressChangeEvent(Object source, ProgressState state, Float progress, String text) {
        super(source);
        this.state = state;
        this.progress = progress;
        this.text = text;
    }

    /**
     * Returns a state of the task or <code>null</code> if it is not applicable to the event.
     *
     * @return progress state.
     */
    public ProgressState getState() {
        return state;
    }

    /**
     * Returns a progress value in percentage  or <code>null</code> if it is not applicable to the event.
     *
     * @return progress value.
     */
    public Float getProgress() {
        return progress;
    }

    /**
     * Returns a message to log  or <code>null</code> if it is not applicable to the event.
     *
     * @return log message.
     */
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ProgressChangeEvent");
        sb.append("{state=").append(state);
        sb.append(", progress=").append(progress);
        sb.append(", text='").append(text).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
