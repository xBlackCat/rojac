
package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for JanusForumGroupInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="JanusForumGroupInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="forumGroupId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="forumGroupName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sortOrder" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "JanusForumGroupInfo", propOrder = {
    "forumGroupId",
    "forumGroupName",
    "sortOrder"
})
public class JanusForumGroupInfo {

    protected int forumGroupId;
    protected String forumGroupName;
    protected int sortOrder;

    /**
     * Gets the value of the forumGroupId property.
     * 
     */
    public int getForumGroupId() {
        return forumGroupId;
    }

    /**
     * Sets the value of the forumGroupId property.
     * 
     */
    public void setForumGroupId(int value) {
        this.forumGroupId = value;
    }

    /**
     * Gets the value of the forumGroupName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForumGroupName() {
        return forumGroupName;
    }

    /**
     * Sets the value of the forumGroupName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForumGroupName(String value) {
        this.forumGroupName = value;
    }

    /**
     * Gets the value of the sortOrder property.
     * 
     */
    public int getSortOrder() {
        return sortOrder;
    }

    /**
     * Sets the value of the sortOrder property.
     * 
     */
    public void setSortOrder(int value) {
        this.sortOrder = value;
    }

}
