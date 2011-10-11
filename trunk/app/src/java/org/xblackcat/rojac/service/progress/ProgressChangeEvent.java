package org.xblackcat.rojac.service.progress;

import java.util.EventObject;

/**
 * @author xBlackCat
 */

public class ProgressChangeEvent extends EventObject {
    private final boolean percents;
    private final ProgressState state;
    private final Integer value;
    private final Integer bound;
    private final String text;

    public ProgressChangeEvent(Object source, ProgressState state) {
        this(source, state, null, null, null, true);
    }

    public ProgressChangeEvent(Object source, ProgressState state, int value) {
        this(source, state, value, null, null, false);
    }

    public ProgressChangeEvent(Object source, ProgressState state, String text) {
        this(source, state, null, null, text, true);
    }

    public ProgressChangeEvent(Object source, ProgressState state, int value, String text) {
        this(source, state, value, null, text, true);
    }

    public ProgressChangeEvent(Object source, ProgressState state, int value, int bound) {
        this(source, state, value, bound, null);
    }

    public ProgressChangeEvent(Object source, ProgressState state, int value, int bound, String text) {
        this(source, state, value, bound, text, false);
    }

    private ProgressChangeEvent(Object source, ProgressState state, Integer value, Integer bound, String text, boolean percents) {
        super(source);
        this.state = state;
        this.value = value;
        this.text = text;
        this.percents = percents;
        this.bound = bound;
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
    public Integer getValue() {
        return value;
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
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProgressChangeEvent)) {
            return false;
        }

        ProgressChangeEvent that = (ProgressChangeEvent) o;

        if (value != null ? !value.equals(that.value) : that.value != null) {
            return false;
        }
        if (state != that.state) {
            return false;
        }
        if (text != null ? !text.equals(that.text) : that.text != null) {
            return false;
        }

        return true;
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

    public boolean isPercents() {
        return percents;
    }

    public Integer getBound() {
        return bound;
    }
}
