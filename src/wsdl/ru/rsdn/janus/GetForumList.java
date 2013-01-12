
package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="forumRequest" type="{http://rsdn.ru/Janus/}ForumRequest" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "forumRequest"
})
@XmlRootElement(name = "GetForumList")
public class GetForumList {

    protected ForumRequest forumRequest;

    /**
     * Gets the value of the forumRequest property.
     * 
     * @return
     *     possible object is
     *     {@link ForumRequest }
     *     
     */
    public ForumRequest getForumRequest() {
        return forumRequest;
    }

    /**
     * Sets the value of the forumRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link ForumRequest }
     *     
     */
    public void setForumRequest(ForumRequest value) {
        this.forumRequest = value;
    }

}
