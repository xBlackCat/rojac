package ru.rsdn.janus;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetUserByIdsResult" type="{http://rsdn.ru/Janus/}UserResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "getUserByIdsResult"
})
@XmlRootElement(name = "GetUserByIdsResponse")
public class GetUserByIdsResponse {

    @XmlElement(name = "GetUserByIdsResult")
    protected UserResponse getUserByIdsResult;

    /**
     * Gets the value of the getUserByIdsResult property.
     *
     * @return possible object is
     * {@link UserResponse }
     */
    public UserResponse getGetUserByIdsResult() {
        return getUserByIdsResult;
    }

    /**
     * Sets the value of the getUserByIdsResult property.
     *
     * @param value allowed object is
     *              {@link UserResponse }
     */
    public void setGetUserByIdsResult(UserResponse value) {
        this.getUserByIdsResult = value;
    }

}
