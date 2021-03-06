package org.xblackcat.rojac.service.janus.data;

import org.xblackcat.rojac.data.User;
import org.xblackcat.rojac.data.Version;
import ru.rsdn.janus.JanusUserInfo;
import ru.rsdn.janus.UserResponse;

import java.util.List;

/**
 * @author Alexey
 */

public class UsersList {
    private final User[] users;
    private final Version version;

    public UsersList(UserResponse r) {
        this.version = new Version(r.getLastRowVersion());

        List<JanusUserInfo> janusUserInfo = r.getUsers().getJanusUserInfo();
        JanusUserInfo[] users = janusUserInfo.toArray(new JanusUserInfo[janusUserInfo.size()]);
        this.users = new User[users.length];
        for (int i = 0; i < users.length; i++) {
            this.users[i] = new User(
                    users[i].getUserId(),
                    users[i].getUserName(),
                    users[i].getUserNick(),
                    users[i].getRealName(),
                    users[i].getPublicEmail(),
                    users[i].getHomePage(),
                    users[i].getSpecialization(),
                    users[i].getWhereFrom(),
                    users[i].getOrigin()
            );
        }
    }

    public User[] getUsers() {
        return users;
    }

    public Version getVersion() {
        return version;
    }
}
