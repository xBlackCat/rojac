
package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ForumResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ForumResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="forumList" type="{http://rsdn.ru/Janus/}ArrayOfJanusForumInfo" minOccurs="0"/>
 *         &lt;element name="groupList" type="{http://rsdn.ru/Janus/}ArrayOfJanusForumGroupInfo" minOccurs="0"/>
 *         &lt;element name="forumsRowVersion" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ForumResponse", propOrder = {
    "forumList",
    "groupList",
    "forumsRowVersion"
})
public class ForumResponse {

    protected ArrayOfJanusForumInfo forumList;
    protected ArrayOfJanusForumGroupInfo groupList;
    protected byte[] forumsRowVersion;

    /**
     * Gets the value of the forumList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfJanusForumInfo }
     *     
     */
    public ArrayOfJanusForumInfo getForumList() {
        return forumList;
    }

    /**
     * Sets the value of the forumList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfJanusForumInfo }
     *     
     */
    public void setForumList(ArrayOfJanusForumInfo value) {
        this.forumList = value;
    }

    /**
     * Gets the value of the groupList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfJanusForumGroupInfo }
     *     
     */
    public ArrayOfJanusForumGroupInfo getGroupList() {
        return groupList;
    }

    /**
     * Sets the value of the groupList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfJanusForumGroupInfo }
     *     
     */
    public void setGroupList(ArrayOfJanusForumGroupInfo value) {
        this.groupList = value;
    }

    /**
     * Gets the value of the forumsRowVersion property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getForumsRowVersion() {
        return forumsRowVersion;
    }

    /**
     * Sets the value of the forumsRowVersion property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setForumsRowVersion(byte[] value) {
        this.forumsRowVersion = value;
    }

}
