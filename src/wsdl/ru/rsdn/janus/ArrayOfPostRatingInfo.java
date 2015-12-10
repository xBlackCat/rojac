package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ArrayOfPostRatingInfo complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="ArrayOfPostRatingInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PostRatingInfo" type="{http://rsdn.ru/Janus/}PostRatingInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfPostRatingInfo", propOrder = {
        "postRatingInfo"
})
public class ArrayOfPostRatingInfo {

    @XmlElement(name = "PostRatingInfo", nillable = true)
    protected List<PostRatingInfo> postRatingInfo;

    /**
     * Gets the value of the postRatingInfo property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the postRatingInfo property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPostRatingInfo().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PostRatingInfo }
     */
    public List<PostRatingInfo> getPostRatingInfo() {
        if (postRatingInfo == null) {
            postRatingInfo = new ArrayList<>();
        }
        return this.postRatingInfo;
    }

}
