package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ArrayOfJanusRatingInfo complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="ArrayOfJanusRatingInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="JanusRatingInfo" type="{http://rsdn.ru/Janus/}JanusRatingInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfJanusRatingInfo", propOrder = {
        "janusRatingInfo"
})
public class ArrayOfJanusRatingInfo {

    @XmlElement(name = "JanusRatingInfo", nillable = true)
    protected List<JanusRatingInfo> janusRatingInfo;

    /**
     * Gets the value of the janusRatingInfo property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the janusRatingInfo property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getJanusRatingInfo().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JanusRatingInfo }
     */
    public List<JanusRatingInfo> getJanusRatingInfo() {
        if (janusRatingInfo == null) {
            janusRatingInfo = new ArrayList<>();
        }
        return this.janusRatingInfo;
    }

}
