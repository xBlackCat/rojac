package org.xblackcat.rojac.service.progress;

import java.util.EventObject;

/**
 * @author xBlackCat
 */

public class ProgressChangeEvent extends EventObject {
    private ProgressState state;
    private Integer progress;
    private String text;

    public ProgressChangeEvent(Object source, ProgressState state, Integer progress, String text) {
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
    public Integer getProgress() {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProgressChangeEvent)) return false;

        ProgressChangeEvent that = (ProgressChangeEvent) o;

        if (progress != null ? !progress.equals(that.progress) : that.progress != null) return false;
        if (state != that.state) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = state != null ? state.hashCode() : 0;
        result = 31 * result + (progress != null ? progress.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
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
