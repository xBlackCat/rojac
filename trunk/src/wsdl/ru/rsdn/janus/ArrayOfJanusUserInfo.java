
package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ArrayOfJanusUserInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfJanusUserInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="JanusUserInfo" type="{http://rsdn.ru/Janus/}JanusUserInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfJanusUserInfo", propOrder = {
    "janusUserInfo"
})
public class ArrayOfJanusUserInfo {

    @XmlElement(name = "JanusUserInfo", nillable = true)
    protected List<JanusUserInfo> janusUserInfo;

    /**
     * Gets the value of the janusUserInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the janusUserInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getJanusUserInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JanusUserInfo }
     * 
     * 
     */
    public List<JanusUserInfo> getJanusUserInfo() {
        if (janusUserInfo == null) {
            janusUserInfo = new ArrayList<JanusUserInfo>();
        }
        return this.janusUserInfo;
    }

}
