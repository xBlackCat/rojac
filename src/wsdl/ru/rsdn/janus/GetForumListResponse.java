package ru.rsdn.janus;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetForumListResult" type="{http://rsdn.ru/Janus/}ForumResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "getForumListResult"
})
@XmlRootElement(name = "GetForumListResponse")
public class GetForumListResponse {

    @XmlElement(name = "GetForumListResult")
    protected ForumResponse getForumListResult;

    /**
     * Gets the value of the getForumListResult property.
     *
     * @return possible object is
     * {@link ForumResponse }
     */
    public ForumResponse getGetForumListResult() {
        return getForumListResult;
    }

    /**
     * Sets the value of the getForumListResult property.
     *
     * @param value allowed object is
     *              {@link ForumResponse }
     */
    public void setGetForumListResult(ForumResponse value) {
        this.getForumListResult = value;
    }

}
