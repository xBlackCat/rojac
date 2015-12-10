package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ChangeResponse complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="ChangeResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="newMessages" type="{http://rsdn.ru/Janus/}ArrayOfJanusMessageInfo" minOccurs="0"/>
 *         &lt;element name="newRating" type="{http://rsdn.ru/Janus/}ArrayOfJanusRatingInfo" minOccurs="0"/>
 *         &lt;element name="newModerate" type="{http://rsdn.ru/Janus/}ArrayOfJanusModerateInfo" minOccurs="0"/>
 *         &lt;element name="lastRatingRowVersion" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="lastForumRowVersion" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="lastModerateRowVersion" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="userId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChangeResponse", propOrder = {
        "newMessages",
        "newRating",
        "newModerate",
        "lastRatingRowVersion",
        "lastForumRowVersion",
        "lastModerateRowVersion",
        "userId"
})
public class ChangeResponse {

    protected ArrayOfJanusMessageInfo newMessages;
    protected ArrayOfJanusRatingInfo newRating;
    protected ArrayOfJanusModerateInfo newModerate;
    protected byte[] lastRatingRowVersion;
    protected byte[] lastForumRowVersion;
    protected byte[] lastModerateRowVersion;
    protected int userId;

    /**
     * Gets the value of the newMessages property.
     *
     * @return possible object is
     * {@link ArrayOfJanusMessageInfo }
     */
    public ArrayOfJanusMessageInfo getNewMessages() {
        return newMessages;
    }

    /**
     * Sets the value of the newMessages property.
     *
     * @param value allowed object is
     *              {@link ArrayOfJanusMessageInfo }
     */
    public void setNewMessages(ArrayOfJanusMessageInfo value) {
        this.newMessages = value;
    }

    /**
     * Gets the value of the newRating property.
     *
     * @return possible object is
     * {@link ArrayOfJanusRatingInfo }
     */
    public ArrayOfJanusRatingInfo getNewRating() {
        return newRating;
    }

    /**
     * Sets the value of the newRating property.
     *
     * @param value allowed object is
     *              {@link ArrayOfJanusRatingInfo }
     */
    public void setNewRating(ArrayOfJanusRatingInfo value) {
        this.newRating = value;
    }

    /**
     * Gets the value of the newModerate property.
     *
     * @return possible object is
     * {@link ArrayOfJanusModerateInfo }
     */
    public ArrayOfJanusModerateInfo getNewModerate() {
        return newModerate;
    }

    /**
     * Sets the value of the newModerate property.
     *
     * @param value allowed object is
     *              {@link ArrayOfJanusModerateInfo }
     */
    public void setNewModerate(ArrayOfJanusModerateInfo value) {
        this.newModerate = value;
    }

    /**
     * Gets the value of the lastRatingRowVersion property.
     *
     * @return possible object is
     * byte[]
     */
    public byte[] getLastRatingRowVersion() {
        return lastRatingRowVersion;
    }

    /**
     * Sets the value of the lastRatingRowVersion property.
     *
     * @param value allowed object is
     *              byte[]
     */
    public void setLastRatingRowVersion(byte[] value) {
        this.lastRatingRowVersion = value;
    }

    /**
     * Gets the value of the lastForumRowVersion property.
     *
     * @return possible object is
     * byte[]
     */
    public byte[] getLastForumRowVersion() {
        return lastForumRowVersion;
    }

    /**
     * Sets the value of the lastForumRowVersion property.
     *
     * @param value allowed object is
     *              byte[]
     */
    public void setLastForumRowVersion(byte[] value) {
        this.lastForumRowVersion = value;
    }

    /**
     * Gets the value of the lastModerateRowVersion property.
     *
     * @return possible object is
     * byte[]
     */
    public byte[] getLastModerateRowVersion() {
        return lastModerateRowVersion;
    }

    /**
     * Sets the value of the lastModerateRowVersion property.
     *
     * @param value allowed object is
     *              byte[]
     */
    public void setLastModerateRowVersion(byte[] value) {
        this.lastModerateRowVersion = value;
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

}
