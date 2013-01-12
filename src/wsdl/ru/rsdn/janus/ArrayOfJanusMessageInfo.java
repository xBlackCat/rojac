
package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ArrayOfJanusMessageInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfJanusMessageInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="JanusMessageInfo" type="{http://rsdn.ru/Janus/}JanusMessageInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfJanusMessageInfo", propOrder = {
    "janusMessageInfo"
})
public class ArrayOfJanusMessageInfo {

    @XmlElement(name = "JanusMessageInfo", nillable = true)
    protected List<JanusMessageInfo> janusMessageInfo;

    /**
     * Gets the value of the janusMessageInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the janusMessageInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getJanusMessageInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JanusMessageInfo }
     * 
     * 
     */
    public List<JanusMessageInfo> getJanusMessageInfo() {
        if (janusMessageInfo == null) {
            janusMessageInfo = new ArrayList<JanusMessageInfo>();
        }
        return this.janusMessageInfo;
    }

}
