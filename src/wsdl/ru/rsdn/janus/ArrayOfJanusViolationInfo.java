package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ArrayOfJanusViolationInfo complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="ArrayOfJanusViolationInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="JanusViolationInfo" type="{http://rsdn.ru/Janus/}JanusViolationInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfJanusViolationInfo", propOrder = {
        "janusViolationInfo"
})
public class ArrayOfJanusViolationInfo {

    @XmlElement(name = "JanusViolationInfo", nillable = true)
    protected List<JanusViolationInfo> janusViolationInfo;

    /**
     * Gets the value of the janusViolationInfo property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the janusViolationInfo property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getJanusViolationInfo().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JanusViolationInfo }
     */
    public List<JanusViolationInfo> getJanusViolationInfo() {
        if (janusViolationInfo == null) {
            janusViolationInfo = new ArrayList<>();
        }
        return this.janusViolationInfo;
    }

}
