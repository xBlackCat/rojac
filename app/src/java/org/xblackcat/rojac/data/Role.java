package org.xblackcat.rojac.data;

import org.apache.commons.logging.LogFactory;
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
    Anonym,
    Group;

    public static Role getUserType(UserRole ur) {
        try {
            return valueOf(ur.getValue());
        } catch (IllegalArgumentException e) {
            LogFactory.getLog(Role.class).warn("Unknown user role " + ur.getValue());

            return User;
        }
    }

    public String toString() {
        return "UserRole[" + name() + ']';
    }
}
