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
 *         &lt;element name="GetTopicByMessageResult" type="{http://rsdn.ru/Janus/}TopicResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "getTopicByMessageResult"
})
@XmlRootElement(name = "GetTopicByMessageResponse")
public class GetTopicByMessageResponse {

    @XmlElement(name = "GetTopicByMessageResult")
    protected TopicResponse getTopicByMessageResult;

    /**
     * Gets the value of the getTopicByMessageResult property.
     *
     * @return possible object is
     * {@link TopicResponse }
     */
    public TopicResponse getGetTopicByMessageResult() {
        return getTopicByMessageResult;
    }

    /**
     * Sets the value of the getTopicByMessageResult property.
     *
     * @param value allowed object is
     *              {@link TopicResponse }
     */
    public void setGetTopicByMessageResult(TopicResponse value) {
        this.getTopicByMessageResult = value;
    }

}
