package org.xblackcat.rojac.data;

import org.apache.commons.logging.LogFactory;
import ru.rsdn.janus.UserRole;

/**
 * @author ASUS
 */

public enum Role implements IDictionary {
    Admin,
    Moderator,
    TeamMember,
    User,
    Expert,
    Anonym,
    Group;

    public static Role getUserType(UserRole ur) {
        try {
            return valueOf(ur.value());
        } catch (IllegalArgumentException e) {
            LogFactory.getLog(Role.class).warn("Unknown user role " + ur.value());

            return User;
        }
    }

    public String toString() {
        return "UserRole[" + name() + ']';
    }

    @Override
    public int getId() {
        return ordinal();
    }
}
