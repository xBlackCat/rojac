
package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UserRole.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="UserRole">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Admin"/>
 *     &lt;enumeration value="Moderator"/>
 *     &lt;enumeration value="TeamMember"/>
 *     &lt;enumeration value="User"/>
 *     &lt;enumeration value="Expert"/>
 *     &lt;enumeration value="Anonym"/>
 *     &lt;enumeration value="Group"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "UserRole")
@XmlEnum
public enum UserRole {

    @XmlEnumValue("Admin")
    ADMIN("Admin"),
    @XmlEnumValue("Moderator")
    MODERATOR("Moderator"),
    @XmlEnumValue("TeamMember")
    TEAM_MEMBER("TeamMember"),
    @XmlEnumValue("User")
    USER("User"),
    @XmlEnumValue("Expert")
    EXPERT("Expert"),
    @XmlEnumValue("Anonym")
    ANONYM("Anonym"),
    @XmlEnumValue("Group")
    GROUP("Group");
    private final String value;

    UserRole(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static UserRole fromValue(String v) {
        for (UserRole c: UserRole.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
