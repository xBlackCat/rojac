package org.xblackcat.sunaj.service.data;

import ru.rsdn.Janus.UserRole;

/**
 * Date: 14.04.2007
 *
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
}
