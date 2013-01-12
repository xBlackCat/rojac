
package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PostRatingInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PostRatingInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="localRatingId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="messageId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="rate" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PostRatingInfo", propOrder = {
    "localRatingId",
    "messageId",
    "rate"
})
public class PostRatingInfo {

    protected int localRatingId;
    protected int messageId;
    protected int rate;

    /**
     * Gets the value of the localRatingId property.
     * 
     */
    public int getLocalRatingId() {
        return localRatingId;
    }

    /**
     * Sets the value of the localRatingId property.
     * 
     */
    public void setLocalRatingId(int value) {
        this.localRatingId = value;
    }

    /**
     * Gets the value of the messageId property.
     * 
     */
    public int getMessageId() {
        return messageId;
    }

    /**
     * Sets the value of the messageId property.
     * 
     */
    public void setMessageId(int value) {
        this.messageId = value;
    }

    /**
     * Gets the value of the rate property.
     * 
     */
    public int getRate() {
        return rate;
    }

    /**
     * Sets the value of the rate property.
     * 
     */
    public void setRate(int value) {
        this.rate = value;
    }

}
