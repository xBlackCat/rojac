/**
 * UserRole.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.rsdn.Janus;

public class UserRole implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected UserRole(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Admin = "Admin";
    public static final java.lang.String _Moderator = "Moderator";
    public static final java.lang.String _TeamMember = "TeamMember";
    public static final java.lang.String _User = "User";
    public static final java.lang.String _Expert = "Expert";
    public static final java.lang.String _Anonym = "Anonym";
    public static final UserRole Admin = new UserRole(_Admin);
    public static final UserRole Moderator = new UserRole(_Moderator);
    public static final UserRole TeamMember = new UserRole(_TeamMember);
    public static final UserRole User = new UserRole(_User);
    public static final UserRole Expert = new UserRole(_Expert);
    public static final UserRole Anonym = new UserRole(_Anonym);
    public java.lang.String getValue() { return _value_;}
    public static UserRole fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        UserRole enumeration = (UserRole)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static UserRole fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UserRole.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "UserRole"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
