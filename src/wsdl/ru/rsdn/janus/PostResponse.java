package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PostResponse complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="PostResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="commitedIds" type="{http://rsdn.ru/Janus/}ArrayOfInt" minOccurs="0"/>
 *         &lt;element name="exceptions" type="{http://rsdn.ru/Janus/}ArrayOfPostExceptionInfo" minOccurs="0"/>
 *         &lt;element name="commitedRatingIds" type="{http://rsdn.ru/Janus/}ArrayOfInt" minOccurs="0"/>
 *         &lt;element name="ratingExceptions" type="{http://rsdn.ru/Janus/}ArrayOfRatingExceptionInfo" minOccurs="0"/>
 *         &lt;element name="commitedModerateIds" type="{http://rsdn.ru/Janus/}ArrayOfInt" minOccurs="0"/>
 *         &lt;element name="moderateExceptions" type="{http://rsdn.ru/Janus/}ArrayOfModerateExceptionInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PostResponse", propOrder = {
        "commitedIds",
        "exceptions",
        "commitedRatingIds",
        "ratingExceptions",
        "commitedModerateIds",
        "moderateExceptions"
})
public class PostResponse {

    protected ArrayOfInt commitedIds;
    protected ArrayOfPostExceptionInfo exceptions;
    protected ArrayOfInt commitedRatingIds;
    protected ArrayOfRatingExceptionInfo ratingExceptions;
    protected ArrayOfInt commitedModerateIds;
    protected ArrayOfModerateExceptionInfo moderateExceptions;

    /**
     * Gets the value of the commitedIds property.
     *
     * @return possible object is
     * {@link ArrayOfInt }
     */
    public ArrayOfInt getCommitedIds() {
        return commitedIds;
    }

    /**
     * Sets the value of the commitedIds property.
     *
     * @param value allowed object is
     *              {@link ArrayOfInt }
     */
    public void setCommitedIds(ArrayOfInt value) {
        this.commitedIds = value;
    }

    /**
     * Gets the value of the exceptions property.
     *
     * @return possible object is
     * {@link ArrayOfPostExceptionInfo }
     */
    public ArrayOfPostExceptionInfo getExceptions() {
        return exceptions;
    }

    /**
     * Sets the value of the exceptions property.
     *
     * @param value allowed object is
     *              {@link ArrayOfPostExceptionInfo }
     */
    public void setExceptions(ArrayOfPostExceptionInfo value) {
        this.exceptions = value;
    }

    /**
     * Gets the value of the commitedRatingIds property.
     *
     * @return possible object is
     * {@link ArrayOfInt }
     */
    public ArrayOfInt getCommitedRatingIds() {
        return commitedRatingIds;
    }

    /**
     * Sets the value of the commitedRatingIds property.
     *
     * @param value allowed object is
     *              {@link ArrayOfInt }
     */
    public void setCommitedRatingIds(ArrayOfInt value) {
        this.commitedRatingIds = value;
    }

    /**
     * Gets the value of the ratingExceptions property.
     *
     * @return possible object is
     * {@link ArrayOfRatingExceptionInfo }
     */
    public ArrayOfRatingExceptionInfo getRatingExceptions() {
        return ratingExceptions;
    }

    /**
     * Sets the value of the ratingExceptions property.
     *
     * @param value allowed object is
     *              {@link ArrayOfRatingExceptionInfo }
     */
    public void setRatingExceptions(ArrayOfRatingExceptionInfo value) {
        this.ratingExceptions = value;
    }

    /**
     * Gets the value of the commitedModerateIds property.
     *
     * @return possible object is
     * {@link ArrayOfInt }
     */
    public ArrayOfInt getCommitedModerateIds() {
        return commitedModerateIds;
    }

    /**
     * Sets the value of the commitedModerateIds property.
     *
     * @param value allowed object is
     *              {@link ArrayOfInt }
     */
    public void setCommitedModerateIds(ArrayOfInt value) {
        this.commitedModerateIds = value;
    }

    /**
     * Gets the value of the moderateExceptions property.
     *
     * @return possible object is
     * {@link ArrayOfModerateExceptionInfo }
     */
    public ArrayOfModerateExceptionInfo getModerateExceptions() {
        return moderateExceptions;
    }

    /**
     * Sets the value of the moderateExceptions property.
     *
     * @param value allowed object is
     *              {@link ArrayOfModerateExceptionInfo }
     */
    public void setModerateExceptions(ArrayOfModerateExceptionInfo value) {
        this.moderateExceptions = value;
    }

}
