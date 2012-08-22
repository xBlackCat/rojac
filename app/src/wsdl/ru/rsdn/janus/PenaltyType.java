
package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PenaltyType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PenaltyType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Ban"/>
 *     &lt;enumeration value="Close"/>
 *     &lt;enumeration value="Warning"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PenaltyType")
@XmlEnum
public enum PenaltyType {

    @XmlEnumValue("Ban")
    BAN("Ban"),
    @XmlEnumValue("Close")
    CLOSE("Close"),
    @XmlEnumValue("Warning")
    WARNING("Warning");
    private final String value;

    PenaltyType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PenaltyType fromValue(String v) {
        for (PenaltyType c: PenaltyType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
