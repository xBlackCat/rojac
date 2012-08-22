
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
 *         &lt;element name="GetNewDataResult" type="{http://rsdn.ru/Janus/}ChangeResponse" minOccurs="0"/>
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
    "getNewDataResult"
})
@XmlRootElement(name = "GetNewDataResponse")
public class GetNewDataResponse {

    @XmlElement(name = "GetNewDataResult")
    protected ChangeResponse getNewDataResult;

    /**
     * Gets the value of the getNewDataResult property.
     * 
     * @return
     *     possible object is
     *     {@link ChangeResponse }
     *     
     */
    public ChangeResponse getGetNewDataResult() {
        return getNewDataResult;
    }

    /**
     * Sets the value of the getNewDataResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ChangeResponse }
     *     
     */
    public void setGetNewDataResult(ChangeResponse value) {
        this.getNewDataResult = value;
    }

}
