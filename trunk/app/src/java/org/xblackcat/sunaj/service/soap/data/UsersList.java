package org.xblackcat.sunaj.service.soap.data;

import ru.rsdn.Janus.UserResponse;
import ru.rsdn.Janus.JanusUserInfo;

/**
 * Date: 13 квіт 2007
 *
 * @author Alexey
 */

public final class UsersList {
	private final UserInfo[] users;
	private final byte[] version;

	public UsersList(UserInfo[] users, byte[] version) {
		this.users = users;
		this.version = version;
	}

	public UsersList(UserResponse r) {
		this.version = r.getLastRowVersion();

		JanusUserInfo[] users = r.getUsers();
		this.users = new UserInfo[users.length];
		for (int i = 0; i < users.length; i++) {
			this.users[i] = new UserInfo(users[i]);
		}
	}

	public UserInfo[] getUsers() {
		return users;
	}

	public byte[] getVersion() {
		return version;
	}
}
