package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for JanusForumInfo complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="JanusForumInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="forumId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="forumGroupId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="shortForumName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="forumName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rated" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="inTop" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="rateLimit" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "JanusForumInfo", propOrder = {
        "forumId",
        "forumGroupId",
        "shortForumName",
        "forumName",
        "rated",
        "inTop",
        "rateLimit"
})
public class JanusForumInfo {

    protected int forumId;
    protected int forumGroupId;
    protected String shortForumName;
    protected String forumName;
    protected int rated;
    protected int inTop;
    protected int rateLimit;

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
     * Gets the value of the forumGroupId property.
     */
    public int getForumGroupId() {
        return forumGroupId;
    }

    /**
     * Sets the value of the forumGroupId property.
     */
    public void setForumGroupId(int value) {
        this.forumGroupId = value;
    }

    /**
     * Gets the value of the shortForumName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getShortForumName() {
        return shortForumName;
    }

    /**
     * Sets the value of the shortForumName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setShortForumName(String value) {
        this.shortForumName = value;
    }

    /**
     * Gets the value of the forumName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getForumName() {
        return forumName;
    }

    /**
     * Sets the value of the forumName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setForumName(String value) {
        this.forumName = value;
    }

    /**
     * Gets the value of the rated property.
     */
    public int getRated() {
        return rated;
    }

    /**
     * Sets the value of the rated property.
     */
    public void setRated(int value) {
        this.rated = value;
    }

    /**
     * Gets the value of the inTop property.
     */
    public int getInTop() {
        return inTop;
    }

    /**
     * Sets the value of the inTop property.
     */
    public void setInTop(int value) {
        this.inTop = value;
    }

    /**
     * Gets the value of the rateLimit property.
     */
    public int getRateLimit() {
        return rateLimit;
    }

    /**
     * Sets the value of the rateLimit property.
     */
    public void setRateLimit(int value) {
        this.rateLimit = value;
    }

}
