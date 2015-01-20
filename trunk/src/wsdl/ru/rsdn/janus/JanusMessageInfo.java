package ru.rsdn.janus;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for JanusMessageInfo complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="JanusMessageInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="messageId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="topicId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="parentId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="userId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="forumId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="subject" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="messageName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userNick" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="articleId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="messageDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="updateDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="userRole" type="{http://rsdn.ru/Janus/}UserRole"/>
 *         &lt;element name="userTitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userTitleColor" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="lastModerated" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="closed" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "JanusMessageInfo", propOrder = {
        "messageId",
        "topicId",
        "parentId",
        "userId",
        "forumId",
        "subject",
        "messageName",
        "userNick",
        "message",
        "articleId",
        "messageDate",
        "updateDate",
        "userRole",
        "userTitle",
        "userTitleColor",
        "lastModerated",
        "closed"
})
public class JanusMessageInfo {

    protected int messageId;
    protected int topicId;
    protected int parentId;
    protected int userId;
    protected int forumId;
    protected String subject;
    protected String messageName;
    protected String userNick;
    protected String message;
    protected int articleId;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar messageDate;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar updateDate;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected UserRole userRole;
    protected String userTitle;
    protected int userTitleColor;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastModerated;
    protected boolean closed;

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
     * Gets the value of the parentId property.
     */
    public int getParentId() {
        return parentId;
    }

    /**
     * Sets the value of the parentId property.
     */
    public void setParentId(int value) {
        this.parentId = value;
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
     * Gets the value of the subject property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the value of the subject property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSubject(String value) {
        this.subject = value;
    }

    /**
     * Gets the value of the messageName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMessageName() {
        return messageName;
    }

    /**
     * Sets the value of the messageName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMessageName(String value) {
        this.messageName = value;
    }

    /**
     * Gets the value of the userNick property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getUserNick() {
        return userNick;
    }

    /**
     * Sets the value of the userNick property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUserNick(String value) {
        this.userNick = value;
    }

    /**
     * Gets the value of the message property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Gets the value of the articleId property.
     */
    public int getArticleId() {
        return articleId;
    }

    /**
     * Sets the value of the articleId property.
     */
    public void setArticleId(int value) {
        this.articleId = value;
    }

    /**
     * Gets the value of the messageDate property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getMessageDate() {
        return messageDate;
    }

    /**
     * Sets the value of the messageDate property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setMessageDate(XMLGregorianCalendar value) {
        this.messageDate = value;
    }

    /**
     * Gets the value of the updateDate property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getUpdateDate() {
        return updateDate;
    }

    /**
     * Sets the value of the updateDate property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setUpdateDate(XMLGregorianCalendar value) {
        this.updateDate = value;
    }

    /**
     * Gets the value of the userRole property.
     *
     * @return possible object is
     * {@link UserRole }
     */
    public UserRole getUserRole() {
        return userRole;
    }

    /**
     * Sets the value of the userRole property.
     *
     * @param value allowed object is
     *              {@link UserRole }
     */
    public void setUserRole(UserRole value) {
        this.userRole = value;
    }

    /**
     * Gets the value of the userTitle property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getUserTitle() {
        return userTitle;
    }

    /**
     * Sets the value of the userTitle property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUserTitle(String value) {
        this.userTitle = value;
    }

    /**
     * Gets the value of the userTitleColor property.
     */
    public int getUserTitleColor() {
        return userTitleColor;
    }

    /**
     * Sets the value of the userTitleColor property.
     */
    public void setUserTitleColor(int value) {
        this.userTitleColor = value;
    }

    /**
     * Gets the value of the lastModerated property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getLastModerated() {
        return lastModerated;
    }

    /**
     * Sets the value of the lastModerated property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setLastModerated(XMLGregorianCalendar value) {
        this.lastModerated = value;
    }

    /**
     * Gets the value of the closed property.
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * Sets the value of the closed property.
     */
    public void setClosed(boolean value) {
        this.closed = value;
    }

}
