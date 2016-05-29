package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ArrayOfModerateExceptionInfo complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="ArrayOfModerateExceptionInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ModerateExceptionInfo" type="{http://rsdn.ru/Janus/}ModerateExceptionInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfModerateExceptionInfo", propOrder = {
        "moderateExceptionInfo"
})
public class ArrayOfModerateExceptionInfo {

    @XmlElement(name = "ModerateExceptionInfo", nillable = true)
    protected List<ModerateExceptionInfo> moderateExceptionInfo;

    /**
     * Gets the value of the moderateExceptionInfo property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the moderateExceptionInfo property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getModerateExceptionInfo().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ModerateExceptionInfo }
     */
    public List<ModerateExceptionInfo> getModerateExceptionInfo() {
        if (moderateExceptionInfo == null) {
            moderateExceptionInfo = new ArrayList<>();
        }
        return this.moderateExceptionInfo;
    }

}
