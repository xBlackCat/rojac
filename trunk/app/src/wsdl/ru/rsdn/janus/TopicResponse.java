
package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TopicResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TopicResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Messages" type="{http://rsdn.ru/Janus/}ArrayOfJanusMessageInfo" minOccurs="0"/>
 *         &lt;element name="Rating" type="{http://rsdn.ru/Janus/}ArrayOfJanusRatingInfo" minOccurs="0"/>
 *         &lt;element name="Moderate" type="{http://rsdn.ru/Janus/}ArrayOfJanusModerateInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TopicResponse", propOrder = {
    "messages",
    "rating",
    "moderate"
})
public class TopicResponse {

    @XmlElement(name = "Messages")
    protected ArrayOfJanusMessageInfo messages;
    @XmlElement(name = "Rating")
    protected ArrayOfJanusRatingInfo rating;
    @XmlElement(name = "Moderate")
    protected ArrayOfJanusModerateInfo moderate;

    /**
     * Gets the value of the messages property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfJanusMessageInfo }
     *     
     */
    public ArrayOfJanusMessageInfo getMessages() {
        return messages;
    }

    /**
     * Sets the value of the messages property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfJanusMessageInfo }
     *     
     */
    public void setMessages(ArrayOfJanusMessageInfo value) {
        this.messages = value;
    }

    /**
     * Gets the value of the rating property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfJanusRatingInfo }
     *     
     */
    public ArrayOfJanusRatingInfo getRating() {
        return rating;
    }

    /**
     * Sets the value of the rating property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfJanusRatingInfo }
     *     
     */
    public void setRating(ArrayOfJanusRatingInfo value) {
        this.rating = value;
    }

    /**
     * Gets the value of the moderate property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfJanusModerateInfo }
     *     
     */
    public ArrayOfJanusModerateInfo getModerate() {
        return moderate;
    }

    /**
     * Sets the value of the moderate property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfJanusModerateInfo }
     *     
     */
    public void setModerate(ArrayOfJanusModerateInfo value) {
        this.moderate = value;
    }

}
