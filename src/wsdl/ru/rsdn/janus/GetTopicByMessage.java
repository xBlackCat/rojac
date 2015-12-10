package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="topicRequest" type="{http://rsdn.ru/Janus/}TopicRequest" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "topicRequest"
})
@XmlRootElement(name = "GetTopicByMessage")
public class GetTopicByMessage {

    protected TopicRequest topicRequest;

    /**
     * Gets the value of the topicRequest property.
     *
     * @return possible object is
     * {@link TopicRequest }
     */
    public TopicRequest getTopicRequest() {
        return topicRequest;
    }

    /**
     * Sets the value of the topicRequest property.
     *
     * @param value allowed object is
     *              {@link TopicRequest }
     */
    public void setTopicRequest(TopicRequest value) {
        this.topicRequest = value;
    }

}
