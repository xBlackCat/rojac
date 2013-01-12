
package ru.rsdn.janus;

import javax.xml.bind.annotation.*;


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
 *         &lt;element name="GetNewViolationsResult" type="{http://rsdn.ru/Janus/}ViolationResponse" minOccurs="0"/>
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
    "getNewViolationsResult"
})
@XmlRootElement(name = "GetNewViolationsResponse")
public class GetNewViolationsResponse {

    @XmlElement(name = "GetNewViolationsResult")
    protected ViolationResponse getNewViolationsResult;

    /**
     * Gets the value of the getNewViolationsResult property.
     * 
     * @return
     *     possible object is
     *     {@link ViolationResponse }
     *     
     */
    public ViolationResponse getGetNewViolationsResult() {
        return getNewViolationsResult;
    }

    /**
     * Sets the value of the getNewViolationsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ViolationResponse }
     *     
     */
    public void setGetNewViolationsResult(ViolationResponse value) {
        this.getNewViolationsResult = value;
    }

}
