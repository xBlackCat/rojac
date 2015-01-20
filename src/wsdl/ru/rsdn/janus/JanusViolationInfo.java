
package ru.rsdn.janus;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for JanusViolationInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="JanusViolationInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MessageID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Reason" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CreatedOn" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="PenaltyType" type="{http://rsdn.ru/Janus/}PenaltyType"/>
 *         &lt;element name="RowVersion" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "JanusViolationInfo", propOrder = {
    "messageID",
    "reason",
    "createdOn",
    "penaltyType",
    "rowVersion"
})
public class JanusViolationInfo {

    @XmlElement(name = "MessageID")
    protected int messageID;
    @XmlElement(name = "Reason")
    protected String reason;
    @XmlElement(name = "CreatedOn", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createdOn;
    @XmlElement(name = "PenaltyType", required = true)
    @XmlSchemaType(name = "string")
    protected PenaltyType penaltyType;
    @XmlElement(name = "RowVersion")
    protected byte[] rowVersion;

    /**
     * Gets the value of the messageID property.
     * 
     */
    public int getMessageID() {
        return messageID;
    }

    /**
     * Sets the value of the messageID property.
     * 
     */
    public void setMessageID(int value) {
        this.messageID = value;
    }

    /**
     * Gets the value of the reason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the value of the reason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReason(String value) {
        this.reason = value;
    }

    /**
     * Gets the value of the createdOn property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreatedOn() {
        return createdOn;
    }

    /**
     * Sets the value of the createdOn property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreatedOn(XMLGregorianCalendar value) {
        this.createdOn = value;
    }

    /**
     * Gets the value of the penaltyType property.
     * 
     * @return
     *     possible object is
     *     {@link PenaltyType }
     *     
     */
    public PenaltyType getPenaltyType() {
        return penaltyType;
    }

    /**
     * Sets the value of the penaltyType property.
     * 
     * @param value
     *     allowed object is
     *     {@link PenaltyType }
     *     
     */
    public void setPenaltyType(PenaltyType value) {
        this.penaltyType = value;
    }

    /**
     * Gets the value of the rowVersion property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getRowVersion() {
        return rowVersion;
    }

    /**
     * Sets the value of the rowVersion property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setRowVersion(byte[] value) {
        this.rowVersion = value;
    }

}
