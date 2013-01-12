
package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ArrayOfPostMessageInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfPostMessageInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PostMessageInfo" type="{http://rsdn.ru/Janus/}PostMessageInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfPostMessageInfo", propOrder = {
    "postMessageInfo"
})
public class ArrayOfPostMessageInfo {

    @XmlElement(name = "PostMessageInfo", nillable = true)
    protected List<PostMessageInfo> postMessageInfo;

    /**
     * Gets the value of the postMessageInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the postMessageInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPostMessageInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PostMessageInfo }
     * 
     * 
     */
    public List<PostMessageInfo> getPostMessageInfo() {
        if (postMessageInfo == null) {
            postMessageInfo = new ArrayList<PostMessageInfo>();
        }
        return this.postMessageInfo;
    }

}
