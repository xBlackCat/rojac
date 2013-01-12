
package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="userRequest" type="{http://rsdn.ru/Janus/}UserRequest" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "userRequest"
})
@XmlRootElement(name = "GetNewUsers")
public class GetNewUsers {

    protected UserRequest userRequest;

    /**
     * Gets the value of the userRequest property.
     * 
     * @return
     *     possible object is
     *     {@link UserRequest }
     *     
     */
    public UserRequest getUserRequest() {
        return userRequest;
    }

    /**
     * Sets the value of the userRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserRequest }
     *     
     */
    public void setUserRequest(UserRequest value) {
        this.userRequest = value;
    }

}
