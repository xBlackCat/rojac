
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
 *         &lt;element name="postRequest" type="{http://rsdn.ru/Janus/}PostRequest" minOccurs="0"/>
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
    "postRequest"
})
@XmlRootElement(name = "PostChange")
public class PostChange {

    protected PostRequest postRequest;

    /**
     * Gets the value of the postRequest property.
     * 
     * @return
     *     possible object is
     *     {@link PostRequest }
     *     
     */
    public PostRequest getPostRequest() {
        return postRequest;
    }

    /**
     * Sets the value of the postRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link PostRequest }
     *     
     */
    public void setPostRequest(PostRequest value) {
        this.postRequest = value;
    }

}
