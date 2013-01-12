
package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for JanusUserInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="JanusUserInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="userId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="userName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userNick" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="realName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="publicEmail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="homePage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="specialization" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="whereFrom" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="origin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userClass" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "JanusUserInfo", propOrder = {
    "userId",
    "userName",
    "userNick",
    "realName",
    "publicEmail",
    "homePage",
    "specialization",
    "whereFrom",
    "origin",
    "userClass"
})
public class JanusUserInfo {

    protected int userId;
    protected String userName;
    protected String userNick;
    protected String realName;
    protected String publicEmail;
    protected String homePage;
    protected String specialization;
    protected String whereFrom;
    protected String origin;
    protected int userClass;

    /**
     * Gets the value of the userId property.
     * 
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the value of the userId property.
     * 
     */
    public void setUserId(int value) {
        this.userId = value;
    }

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
     * Gets the value of the userNick property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserNick() {
        return userNick;
    }

    /**
     * Sets the value of the userNick property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserNick(String value) {
        this.userNick = value;
    }

    /**
     * Gets the value of the realName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRealName() {
        return realName;
    }

    /**
     * Sets the value of the realName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRealName(String value) {
        this.realName = value;
    }

    /**
     * Gets the value of the publicEmail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPublicEmail() {
        return publicEmail;
    }

    /**
     * Sets the value of the publicEmail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPublicEmail(String value) {
        this.publicEmail = value;
    }

    /**
     * Gets the value of the homePage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHomePage() {
        return homePage;
    }

    /**
     * Sets the value of the homePage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHomePage(String value) {
        this.homePage = value;
    }

    /**
     * Gets the value of the specialization property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpecialization() {
        return specialization;
    }

    /**
     * Sets the value of the specialization property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecialization(String value) {
        this.specialization = value;
    }

    /**
     * Gets the value of the whereFrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWhereFrom() {
        return whereFrom;
    }

    /**
     * Sets the value of the whereFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWhereFrom(String value) {
        this.whereFrom = value;
    }

    /**
     * Gets the value of the origin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * Sets the value of the origin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigin(String value) {
        this.origin = value;
    }

    /**
     * Gets the value of the userClass property.
     * 
     */
    public int getUserClass() {
        return userClass;
    }

    /**
     * Sets the value of the userClass property.
     * 
     */
    public void setUserClass(int value) {
        this.userClass = value;
    }

}
