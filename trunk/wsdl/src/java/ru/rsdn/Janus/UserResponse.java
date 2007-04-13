/**
 * UserResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.rsdn.Janus;

public class UserResponse  implements java.io.Serializable {
    private byte[] lastRowVersion;

    private ru.rsdn.Janus.JanusUserInfo[] users;

    public UserResponse() {
    }

    public UserResponse(
           byte[] lastRowVersion,
           ru.rsdn.Janus.JanusUserInfo[] users) {
           this.lastRowVersion = lastRowVersion;
           this.users = users;
    }


    /**
     * Gets the lastRowVersion value for this UserResponse.
     * 
     * @return lastRowVersion
     */
    public byte[] getLastRowVersion() {
        return lastRowVersion;
    }


    /**
     * Sets the lastRowVersion value for this UserResponse.
     * 
     * @param lastRowVersion
     */
    public void setLastRowVersion(byte[] lastRowVersion) {
        this.lastRowVersion = lastRowVersion;
    }


    /**
     * Gets the users value for this UserResponse.
     * 
     * @return users
     */
    public ru.rsdn.Janus.JanusUserInfo[] getUsers() {
        return users;
    }


    /**
     * Sets the users value for this UserResponse.
     * 
     * @param users
     */
    public void setUsers(ru.rsdn.Janus.JanusUserInfo[] users) {
        this.users = users;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UserResponse)) return false;
        UserResponse other = (UserResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.lastRowVersion==null && other.getLastRowVersion()==null) || 
             (this.lastRowVersion!=null &&
              java.util.Arrays.equals(this.lastRowVersion, other.getLastRowVersion()))) &&
            ((this.users==null && other.getUsers()==null) || 
             (this.users!=null &&
              java.util.Arrays.equals(this.users, other.getUsers())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getLastRowVersion() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getLastRowVersion());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getLastRowVersion(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getUsers() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getUsers());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getUsers(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UserResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "UserResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastRowVersion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "lastRowVersion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("users");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "users"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusUserInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusUserInfo"));
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
