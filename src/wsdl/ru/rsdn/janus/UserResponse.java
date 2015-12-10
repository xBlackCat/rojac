package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UserResponse complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="UserResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lastRowVersion" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="users" type="{http://rsdn.ru/Janus/}ArrayOfJanusUserInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserResponse", propOrder = {
        "lastRowVersion",
        "users"
})
public class UserResponse {

    protected byte[] lastRowVersion;
    protected ArrayOfJanusUserInfo users;

    /**
     * Gets the value of the lastRowVersion property.
     *
     * @return possible object is
     * byte[]
     */
    public byte[] getLastRowVersion() {
        return lastRowVersion;
    }

    /**
     * Sets the value of the lastRowVersion property.
     *
     * @param value allowed object is
     *              byte[]
     */
    public void setLastRowVersion(byte[] value) {
        this.lastRowVersion = value;
    }

    /**
     * Gets the value of the users property.
     *
     * @return possible object is
     * {@link ArrayOfJanusUserInfo }
     */
    public ArrayOfJanusUserInfo getUsers() {
        return users;
    }

    /**
     * Sets the value of the users property.
     *
     * @param value allowed object is
     *              {@link ArrayOfJanusUserInfo }
     */
    public void setUsers(ArrayOfJanusUserInfo value) {
        this.users = value;
    }

}
