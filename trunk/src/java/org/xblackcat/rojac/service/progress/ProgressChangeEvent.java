package org.xblackcat.rojac.service.progress;

import java.util.EventObject;

/**
 * @author xBlackCat
 */

public class ProgressChangeEvent extends EventObject {
    private final ProgressState state;
    private final Long value;
    private final Long bound;
    private final String text;

    private final Integer percents;

    public ProgressChangeEvent(Object source, ProgressState state) {
        this(source, state, null, null, null);
    }

    public ProgressChangeEvent(Object source, ProgressState state, long value) {
        this(source, state, value, null, null);
    }

    public ProgressChangeEvent(Object source, ProgressState state, String text) {
        this(source, state, null, null, text);
    }

    public ProgressChangeEvent(Object source, ProgressState state, String text, long value) {
        this(source, state, value, null, text);
    }

    public ProgressChangeEvent(Object source, ProgressState state, long value, long bound) {
        this(source, state, null, value, bound);
    }

    public ProgressChangeEvent(Object source, ProgressState state, String text, long value, long bound) {
        this(source, state, value, bound, text);
    }

    private ProgressChangeEvent(Object source, ProgressState state, Long value, Long bound, String text) {
        super(source);
        this.state = state;
        this.value = value;
        this.text = text;
        this.bound = bound;

        if (bound != null && value != null) {
            percents = (int) (value * 100. / bound);
        } else {
            percents = null;
        }
    }

    /**
     * Returns a state of the task or {@code null} if it is not applicable to the event.
     *
     * @return progress state.
     */
    public ProgressState getState() {
        return state;
    }

    /**
     * Returns a progress value in percentage  or {@code null} if it is not applicable to the event.
     *
     * @return progress value.
     */
    public Long getValue() {
        return value;
    }

    public Integer getPercents() {
        return percents;
    }

    /**
     * Returns a message to log  or {@code null} if it is not applicable to the event.
     *
     * @return log message.
     */
    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ProgressChangeEvent)) {
            return false;
        }

        ProgressChangeEvent that = (ProgressChangeEvent) o;

        return state == that.state &&
                (value == null ? that.value == null : value.equals(that.value)) &&
                (text == null ? that.text == null : text.equals(that.text));
    }

    @Override
    public int hashCode() {
        int result = state != null ? state.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ProgressChangeEvent");
        sb.append("{state=").append(state);
        sb.append(", progress=").append(value);
        sb.append(", text='").append(text).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public Long getBound() {
        return bound;
    }
}
