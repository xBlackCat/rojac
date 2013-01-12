
package ru.rsdn.janus;

import javax.xml.bind.annotation.*;


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
 *         &lt;element name="PostChangeCommitResult" type="{http://rsdn.ru/Janus/}PostResponse" minOccurs="0"/>
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
    "postChangeCommitResult"
})
@XmlRootElement(name = "PostChangeCommitResponse")
public class PostChangeCommitResponse {

    @XmlElement(name = "PostChangeCommitResult")
    protected PostResponse postChangeCommitResult;

    /**
     * Gets the value of the postChangeCommitResult property.
     * 
     * @return
     *     possible object is
     *     {@link PostResponse }
     *     
     */
    public PostResponse getPostChangeCommitResult() {
        return postChangeCommitResult;
    }

    /**
     * Sets the value of the postChangeCommitResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link PostResponse }
     *     
     */
    public void setPostChangeCommitResult(PostResponse value) {
        this.postChangeCommitResult = value;
    }

}
