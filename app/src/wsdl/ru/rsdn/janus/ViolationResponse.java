
package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ViolationResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ViolationResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Violations" type="{http://rsdn.ru/Janus/}ArrayOfJanusViolationInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ViolationResponse", propOrder = {
    "violations"
})
public class ViolationResponse {

    @XmlElement(name = "Violations")
    protected ArrayOfJanusViolationInfo violations;

    /**
     * Gets the value of the violations property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfJanusViolationInfo }
     *     
     */
    public ArrayOfJanusViolationInfo getViolations() {
        return violations;
    }

    /**
     * Sets the value of the violations property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfJanusViolationInfo }
     *     
     */
    public void setViolations(ArrayOfJanusViolationInfo value) {
        this.violations = value;
    }

}
