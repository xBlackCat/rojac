
package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ChangeRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ChangeRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="userName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subscribedForums" type="{http://rsdn.ru/Janus/}ArrayOfRequestForumInfo" minOccurs="0"/>
 *         &lt;element name="ratingRowVersion" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="messageRowVersion" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="moderateRowVersion" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="breakMsgIds" type="{http://rsdn.ru/Janus/}ArrayOfInt" minOccurs="0"/>
 *         &lt;element name="breakTopicIds" type="{http://rsdn.ru/Janus/}ArrayOfInt" minOccurs="0"/>
 *         &lt;element name="maxOutput" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChangeRequest", propOrder = {
    "userName",
    "password",
    "subscribedForums",
    "ratingRowVersion",
    "messageRowVersion",
    "moderateRowVersion",
    "breakMsgIds",
    "breakTopicIds",
    "maxOutput"
})
public class ChangeRequest {

    protected String userName;
    protected String password;
    protected ArrayOfRequestForumInfo subscribedForums;
    protected byte[] ratingRowVersion;
    protected byte[] messageRowVersion;
    protected byte[] moderateRowVersion;
    protected ArrayOfInt breakMsgIds;
    protected ArrayOfInt breakTopicIds;
    protected int maxOutput;

    /**
     * Gets the value of the userName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the subscribedForums property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfRequestForumInfo }
     *     
     */
    public ArrayOfRequestForumInfo getSubscribedForums() {
        return subscribedForums;
    }

    /**
     * Sets the value of the subscribedForums property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfRequestForumInfo }
     *     
     */
    public void setSubscribedForums(ArrayOfRequestForumInfo value) {
        this.subscribedForums = value;
    }

    /**
     * Gets the value of the ratingRowVersion property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getRatingRowVersion() {
        return ratingRowVersion;
    }

    /**
     * Sets the value of the ratingRowVersion property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setRatingRowVersion(byte[] value) {
        this.ratingRowVersion = value;
    }

    /**
     * Gets the value of the messageRowVersion property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getMessageRowVersion() {
        return messageRowVersion;
    }

    /**
     * Sets the value of the messageRowVersion property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setMessageRowVersion(byte[] value) {
        this.messageRowVersion = value;
    }

    /**
     * Gets the value of the moderateRowVersion property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getModerateRowVersion() {
        return moderateRowVersion;
    }

    /**
     * Sets the value of the moderateRowVersion property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setModerateRowVersion(byte[] value) {
        this.moderateRowVersion = value;
    }

    /**
     * Gets the value of the breakMsgIds property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfInt }
     *     
     */
    public ArrayOfInt getBreakMsgIds() {
        return breakMsgIds;
    }

    /**
     * Sets the value of the breakMsgIds property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfInt }
     *     
     */
    public void setBreakMsgIds(ArrayOfInt value) {
        this.breakMsgIds = value;
    }

    /**
     * Gets the value of the breakTopicIds property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfInt }
     *     
     */
    public ArrayOfInt getBreakTopicIds() {
        return breakTopicIds;
    }

    /**
     * Sets the value of the breakTopicIds property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfInt }
     *     
     */
    public void setBreakTopicIds(ArrayOfInt value) {
        this.breakTopicIds = value;
    }

    /**
     * Gets the value of the maxOutput property.
     * 
     */
    public int getMaxOutput() {
        return maxOutput;
    }

    /**
     * Sets the value of the maxOutput property.
     * 
     */
    public void setMaxOutput(int value) {
        this.maxOutput = value;
    }

}
