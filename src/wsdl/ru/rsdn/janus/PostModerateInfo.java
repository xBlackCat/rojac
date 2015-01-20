
package ru.rsdn.janus;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for PostModerateInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PostModerateInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LocalModerateId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="MessageId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ModerateAction" type="{http://rsdn.ru/Janus/}ModerateActionType"/>
 *         &lt;element name="ModerateToForumId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AsModerator" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PostModerateInfo", propOrder = {
    "localModerateId",
    "messageId",
    "moderateAction",
    "moderateToForumId",
    "description",
    "asModerator"
})
public class PostModerateInfo {

    @XmlElement(name = "LocalModerateId")
    protected int localModerateId;
    @XmlElement(name = "MessageId")
    protected int messageId;
    @XmlElement(name = "ModerateAction", required = true)
    @XmlSchemaType(name = "string")
    protected ModerateActionType moderateAction;
    @XmlElement(name = "ModerateToForumId")
    protected int moderateToForumId;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "AsModerator")
    protected boolean asModerator;

    /**
     * Gets the value of the localModerateId property.
     * 
     */
    public int getLocalModerateId() {
        return localModerateId;
    }

    /**
     * Sets the value of the localModerateId property.
     * 
     */
    public void setLocalModerateId(int value) {
        this.localModerateId = value;
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
     * Gets the value of the moderateAction property.
     * 
     * @return
     *     possible object is
     *     {@link ModerateActionType }
     *     
     */
    public ModerateActionType getModerateAction() {
        return moderateAction;
    }

    /**
     * Sets the value of the moderateAction property.
     * 
     * @param value
     *     allowed object is
     *     {@link ModerateActionType }
     *     
     */
    public void setModerateAction(ModerateActionType value) {
        this.moderateAction = value;
    }

    /**
     * Gets the value of the moderateToForumId property.
     * 
     */
    public int getModerateToForumId() {
        return moderateToForumId;
    }

    /**
     * Sets the value of the moderateToForumId property.
     * 
     */
    public void setModerateToForumId(int value) {
        this.moderateToForumId = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the asModerator property.
     * 
     */
    public boolean isAsModerator() {
        return asModerator;
    }

    /**
     * Sets the value of the asModerator property.
     * 
     */
    public void setAsModerator(boolean value) {
        this.asModerator = value;
    }

}
