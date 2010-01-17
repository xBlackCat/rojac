package org.xblackcat.rojac.data;

import ru.rsdn.Janus.JanusUserInfo;

/**
 * @author Alexey
 */

public final class User {
    private final int id;
    private final String userName;
    private final String userNick;
    private final String realName;
    private final String publicEmail;
    private final String homePage;
    private final String specialization;
    private final String whereFrom;
    private final String origin;

    public User(int id, String userName, String userNick, String realName, String publicEmail, String homePage, String specialization, String whereFrom, String origin) {
        this.id = id;
        this.userName = userName;
        this.userNick = userNick;
        this.realName = realName;
        this.publicEmail = publicEmail;
        this.homePage = homePage;
        this.specialization = specialization;
        this.whereFrom = whereFrom;
        this.origin = origin;
    }

    public User(JanusUserInfo i) {
        this(i.getUserId(), i.getUserName(), i.getUserNick(), i.getRealName(), i.getPublicEmail(),
                i.getHomePage(), i.getSpecialization(), i.getWhereFrom(), i.getOrigin());
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserNick() {
        return userNick;
    }

    public String getRealName() {
        return realName;
    }

    public String getPublicEmail() {
        return publicEmail;
    }

    public String getHomePage() {
        return homePage;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getWhereFrom() {
        return whereFrom;
    }

    public String getOrigin() {
        return origin;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id == user.id;

    }

    public int hashCode() {
        return id;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("User[id=");
        str.append(id);
        str.append(", nick=");
        str.append(userNick);
        str.append(", name=");
        str.append(userName);
        str.append(", realName=");
        str.append(realName);
        str.append(", publicEmail=");
        str.append(publicEmail);
        str.append(']');
        return str.toString();
    }
}
