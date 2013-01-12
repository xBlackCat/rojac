
package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RequestForumInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RequestForumInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="forumId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="isFirstRequest" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequestForumInfo", propOrder = {
    "forumId",
    "isFirstRequest"
})
public class RequestForumInfo {

    protected int forumId;
    protected boolean isFirstRequest;

    /**
     * Gets the value of the forumId property.
     * 
     */
    public int getForumId() {
        return forumId;
    }

    /**
     * Sets the value of the forumId property.
     * 
     */
    public void setForumId(int value) {
        this.forumId = value;
    }

    /**
     * Gets the value of the isFirstRequest property.
     * 
     */
    public boolean isIsFirstRequest() {
        return isFirstRequest;
    }

    /**
     * Sets the value of the isFirstRequest property.
     * 
     */
    public void setIsFirstRequest(boolean value) {
        this.isFirstRequest = value;
    }

}
