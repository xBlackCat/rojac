/**
 * UserRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.rsdn.Janus;

public class UserRequest  implements java.io.Serializable {
    private java.lang.String userName;

    private java.lang.String password;

    private byte[] lastRowVersion;

    private int maxOutput;

    public UserRequest() {
    }

    public UserRequest(
           java.lang.String userName,
           java.lang.String password,
           byte[] lastRowVersion,
           int maxOutput) {
           this.userName = userName;
           this.password = password;
           this.lastRowVersion = lastRowVersion;
           this.maxOutput = maxOutput;
    }


    /**
     * Gets the userName value for this UserRequest.
     * 
     * @return userName
     */
    public java.lang.String getUserName() {
        return userName;
    }


    /**
     * Sets the userName value for this UserRequest.
     * 
     * @param userName
     */
    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }


    /**
     * Gets the password value for this UserRequest.
     * 
     * @return password
     */
    public java.lang.String getPassword() {
        return password;
    }


    /**
     * Sets the password value for this UserRequest.
     * 
     * @param password
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }


    /**
     * Gets the lastRowVersion value for this UserRequest.
     * 
     * @return lastRowVersion
     */
    public byte[] getLastRowVersion() {
        return lastRowVersion;
    }


    /**
     * Sets the lastRowVersion value for this UserRequest.
     * 
     * @param lastRowVersion
     */
    public void setLastRowVersion(byte[] lastRowVersion) {
        this.lastRowVersion = lastRowVersion;
    }


    /**
     * Gets the maxOutput value for this UserRequest.
     * 
     * @return maxOutput
     */
    public int getMaxOutput() {
        return maxOutput;
    }


    /**
     * Sets the maxOutput value for this UserRequest.
     * 
     * @param maxOutput
     */
    public void setMaxOutput(int maxOutput) {
        this.maxOutput = maxOutput;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UserRequest)) return false;
        UserRequest other = (UserRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.userName==null && other.getUserName()==null) || 
             (this.userName!=null &&
              this.userName.equals(other.getUserName()))) &&
            ((this.password==null && other.getPassword()==null) || 
             (this.password!=null &&
              this.password.equals(other.getPassword()))) &&
            ((this.lastRowVersion==null && other.getLastRowVersion()==null) || 
             (this.lastRowVersion!=null &&
              java.util.Arrays.equals(this.lastRowVersion, other.getLastRowVersion()))) &&
            this.maxOutput == other.getMaxOutput();
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
        if (getUserName() != null) {
            _hashCode += getUserName().hashCode();
        }
        if (getPassword() != null) {
            _hashCode += getPassword().hashCode();
        }
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
        _hashCode += getMaxOutput();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UserRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "UserRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "userName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("password");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "password"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastRowVersion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "lastRowVersion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxOutput");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "maxOutput"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
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
