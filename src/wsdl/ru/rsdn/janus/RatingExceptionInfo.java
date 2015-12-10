package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RatingExceptionInfo complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="RatingExceptionInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="exception" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="localRatingId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="info" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RatingExceptionInfo", propOrder = {
        "exception",
        "localRatingId",
        "info"
})
public class RatingExceptionInfo {

    protected String exception;
    protected int localRatingId;
    protected String info;

    /**
     * Gets the value of the exception property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getException() {
        return exception;
    }

    /**
     * Sets the value of the exception property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setException(String value) {
        this.exception = value;
    }

    /**
     * Gets the value of the localRatingId property.
     */
    public int getLocalRatingId() {
        return localRatingId;
    }

    /**
     * Sets the value of the localRatingId property.
     */
    public void setLocalRatingId(int value) {
        this.localRatingId = value;
    }

    /**
     * Gets the value of the info property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getInfo() {
        return info;
    }

    /**
     * Sets the value of the info property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setInfo(String value) {
        this.info = value;
    }

}
