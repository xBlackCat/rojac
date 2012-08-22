package org.xblackcat.rojac.data;

import ru.rsdn.janus.ModerateActionType;

/**
 * @author xBlackCat
 */

public enum ModerateAction {
    MoveMessage(0),
    DeleteMessage(1),
    DeleteThread(2),
    DeleteErrorMessage(3),
    SplitThread(4),
    CloseTopic(5),
    OpenTopic(6),;
    private final int code;
    private final ModerateActionType type;

    ModerateAction(int code) {
        this.code = code;
        type = ModerateActionType.fromValue(name());
    }

    public static ModerateAction getAction(int action) {
        for (ModerateAction m : values()) {
            if (m.getCode() == action) {
                return m;
            }
        }
        throw new IllegalArgumentException("Unknown moderator action: " + action);
    }

    public int getCode() {
        return code;
    }

    public ModerateActionType getType() {
        return type;
    }
}
