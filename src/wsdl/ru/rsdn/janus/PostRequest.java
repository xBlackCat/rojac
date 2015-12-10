package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PostRequest complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="PostRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="userName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="writedMessages" type="{http://rsdn.ru/Janus/}ArrayOfPostMessageInfo" minOccurs="0"/>
 *         &lt;element name="rates" type="{http://rsdn.ru/Janus/}ArrayOfPostRatingInfo" minOccurs="0"/>
 *         &lt;element name="moderates" type="{http://rsdn.ru/Janus/}ArrayOfPostModerateInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PostRequest", propOrder = {
        "userName",
        "password",
        "writedMessages",
        "rates",
        "moderates"
})
public class PostRequest {

    protected String userName;
    protected String password;
    protected ArrayOfPostMessageInfo writedMessages;
    protected ArrayOfPostRatingInfo rates;
    protected ArrayOfPostModerateInfo moderates;

    /**
     * Gets the value of the userName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * Gets the value of the password property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the writedMessages property.
     *
     * @return possible object is
     * {@link ArrayOfPostMessageInfo }
     */
    public ArrayOfPostMessageInfo getWritedMessages() {
        return writedMessages;
    }

    /**
     * Sets the value of the writedMessages property.
     *
     * @param value allowed object is
     *              {@link ArrayOfPostMessageInfo }
     */
    public void setWritedMessages(ArrayOfPostMessageInfo value) {
        this.writedMessages = value;
    }

    /**
     * Gets the value of the rates property.
     *
     * @return possible object is
     * {@link ArrayOfPostRatingInfo }
     */
    public ArrayOfPostRatingInfo getRates() {
        return rates;
    }

    /**
     * Sets the value of the rates property.
     *
     * @param value allowed object is
     *              {@link ArrayOfPostRatingInfo }
     */
    public void setRates(ArrayOfPostRatingInfo value) {
        this.rates = value;
    }

    /**
     * Gets the value of the moderates property.
     *
     * @return possible object is
     * {@link ArrayOfPostModerateInfo }
     */
    public ArrayOfPostModerateInfo getModerates() {
        return moderates;
    }

    /**
     * Sets the value of the moderates property.
     *
     * @param value allowed object is
     *              {@link ArrayOfPostModerateInfo }
     */
    public void setModerates(ArrayOfPostModerateInfo value) {
        this.moderates = value;
    }

}
