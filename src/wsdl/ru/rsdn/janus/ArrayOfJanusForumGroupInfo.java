package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ArrayOfJanusForumGroupInfo complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="ArrayOfJanusForumGroupInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="JanusForumGroupInfo" type="{http://rsdn.ru/Janus/}JanusForumGroupInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfJanusForumGroupInfo", propOrder = {
        "janusForumGroupInfo"
})
public class ArrayOfJanusForumGroupInfo {

    @XmlElement(name = "JanusForumGroupInfo", nillable = true)
    protected List<JanusForumGroupInfo> janusForumGroupInfo;

    /**
     * Gets the value of the janusForumGroupInfo property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the janusForumGroupInfo property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getJanusForumGroupInfo().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JanusForumGroupInfo }
     */
    public List<JanusForumGroupInfo> getJanusForumGroupInfo() {
        if (janusForumGroupInfo == null) {
            janusForumGroupInfo = new ArrayList<JanusForumGroupInfo>();
        }
        return this.janusForumGroupInfo;
    }

}
