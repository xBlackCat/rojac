package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UserRequest complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="UserRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="userName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lastRowVersion" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="maxOutput" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserRequest", propOrder = {
        "userName",
        "password",
        "lastRowVersion",
        "maxOutput"
})
public class UserRequest {

    protected String userName;
    protected String password;
    protected byte[] lastRowVersion;
    protected int maxOutput;

    /**
     * Gets the value of the userName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * Gets the value of the password property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPassword(String value) {
        this.password = value;
    }

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
     * Gets the value of the maxOutput property.
     */
    public int getMaxOutput() {
        return maxOutput;
    }

    /**
     * Sets the value of the maxOutput property.
     */
    public void setMaxOutput(int value) {
        this.maxOutput = value;
    }

}
