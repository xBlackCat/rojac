package org.xblackcat.rojac.data;

import ru.rsdn.Janus.UserRole;

/**
 * @author ASUS
 */

public enum Role {
    Admin,
    Moderator,
    TeamMember,
    User,
    Expert,
    Anonym;

    public static Role getUserType(UserRole ur) {
        return valueOf(ur.getValue());
    }

    public String toString() {
        return "UserRole[" + name() + ']';
    }
}
