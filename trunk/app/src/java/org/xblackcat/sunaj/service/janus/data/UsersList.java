package org.xblackcat.sunaj.service.janus.data;

import org.xblackcat.sunaj.service.data.UserInfo;
import org.xblackcat.sunaj.service.data.Version;
import ru.rsdn.Janus.JanusUserInfo;
import ru.rsdn.Janus.UserResponse;

/**
 * Date: 13 квіт 2007
 *
 * @author Alexey
 */

public final class UsersList {
    private final UserInfo[] users;
    private final Version version;

    public UsersList(UserInfo[] users, Version version) {
        this.users = users;
        this.version = version;
    }

    public UsersList(UserResponse r) {
        this.version = new Version(r.getLastRowVersion());

        JanusUserInfo[] users = r.getUsers();
        this.users = new UserInfo[users.length];
        for (int i = 0; i < users.length; i++) {
            this.users[i] = new UserInfo(users[i]);
        }
    }

    public UserInfo[] getUsers() {
        return users;
    }

    public Version getVersion() {
        return version;
    }
}
