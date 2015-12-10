package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ModerateActionType.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ModerateActionType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="MoveMessage"/>
 *     &lt;enumeration value="DeleteMessage"/>
 *     &lt;enumeration value="DeleteThread"/>
 *     &lt;enumeration value="DeleteErrorMessage"/>
 *     &lt;enumeration value="SplitThread"/>
 *     &lt;enumeration value="CloseTopic"/>
 *     &lt;enumeration value="OpenTopic"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "ModerateActionType")
@XmlEnum
public enum ModerateActionType {

    @XmlEnumValue("MoveMessage")
    MOVE_MESSAGE("MoveMessage"),
    @XmlEnumValue("DeleteMessage")
    DELETE_MESSAGE("DeleteMessage"),
    @XmlEnumValue("DeleteThread")
    DELETE_THREAD("DeleteThread"),
    @XmlEnumValue("DeleteErrorMessage")
    DELETE_ERROR_MESSAGE("DeleteErrorMessage"),
    @XmlEnumValue("SplitThread")
    SPLIT_THREAD("SplitThread"),
    @XmlEnumValue("CloseTopic")
    CLOSE_TOPIC("CloseTopic"),
    @XmlEnumValue("OpenTopic")
    OPEN_TOPIC("OpenTopic");
    private final String value;

    ModerateActionType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ModerateActionType fromValue(String v) {
        for (ModerateActionType c : ModerateActionType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
