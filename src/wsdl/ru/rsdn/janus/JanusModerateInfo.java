package ru.rsdn.janus;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for JanusModerateInfo complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="JanusModerateInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="messageId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="topicId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="userId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="forumId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="create" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "JanusModerateInfo", propOrder = {
        "messageId",
        "topicId",
        "userId",
        "forumId",
        "create"
})
public class JanusModerateInfo {

    protected int messageId;
    protected int topicId;
    protected int userId;
    protected int forumId;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar create;

    /**
     * Gets the value of the messageId property.
     */
    public int getMessageId() {
        return messageId;
    }

    /**
     * Sets the value of the messageId property.
     */
    public void setMessageId(int value) {
        this.messageId = value;
    }

    /**
     * Gets the value of the topicId property.
     */
    public int getTopicId() {
        return topicId;
    }

    /**
     * Sets the value of the topicId property.
     */
    public void setTopicId(int value) {
        this.topicId = value;
    }

    /**
     * Gets the value of the userId property.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the value of the userId property.
     */
    public void setUserId(int value) {
        this.userId = value;
    }

    /**
     * Gets the value of the forumId property.
     */
    public int getForumId() {
        return forumId;
    }

    /**
     * Sets the value of the forumId property.
     */
    public void setForumId(int value) {
        this.forumId = value;
    }

    /**
     * Gets the value of the create property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getCreate() {
        return create;
    }

    /**
     * Sets the value of the create property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setCreate(XMLGregorianCalendar value) {
        this.create = value;
    }

}
