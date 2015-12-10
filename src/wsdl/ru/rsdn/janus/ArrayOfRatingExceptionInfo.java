package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ArrayOfRatingExceptionInfo complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="ArrayOfRatingExceptionInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RatingExceptionInfo" type="{http://rsdn.ru/Janus/}RatingExceptionInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfRatingExceptionInfo", propOrder = {
        "ratingExceptionInfo"
})
public class ArrayOfRatingExceptionInfo {

    @XmlElement(name = "RatingExceptionInfo", nillable = true)
    protected List<RatingExceptionInfo> ratingExceptionInfo;

    /**
     * Gets the value of the ratingExceptionInfo property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ratingExceptionInfo property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRatingExceptionInfo().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RatingExceptionInfo }
     */
    public List<RatingExceptionInfo> getRatingExceptionInfo() {
        if (ratingExceptionInfo == null) {
            ratingExceptionInfo = new ArrayList<>();
        }
        return this.ratingExceptionInfo;
    }

}
