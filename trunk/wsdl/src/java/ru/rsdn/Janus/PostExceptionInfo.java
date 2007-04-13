/**
 * PostExceptionInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.rsdn.Janus;

public class PostExceptionInfo  implements java.io.Serializable {
    private java.lang.String exception;

    private int localMessageId;

    private java.lang.String info;

    public PostExceptionInfo() {
    }

    public PostExceptionInfo(
           java.lang.String exception,
           int localMessageId,
           java.lang.String info) {
           this.exception = exception;
           this.localMessageId = localMessageId;
           this.info = info;
    }


    /**
     * Gets the exception value for this PostExceptionInfo.
     * 
     * @return exception
     */
    public java.lang.String getException() {
        return exception;
    }


    /**
     * Sets the exception value for this PostExceptionInfo.
     * 
     * @param exception
     */
    public void setException(java.lang.String exception) {
        this.exception = exception;
    }


    /**
     * Gets the localMessageId value for this PostExceptionInfo.
     * 
     * @return localMessageId
     */
    public int getLocalMessageId() {
        return localMessageId;
    }


    /**
     * Sets the localMessageId value for this PostExceptionInfo.
     * 
     * @param localMessageId
     */
    public void setLocalMessageId(int localMessageId) {
        this.localMessageId = localMessageId;
    }


    /**
     * Gets the info value for this PostExceptionInfo.
     * 
     * @return info
     */
    public java.lang.String getInfo() {
        return info;
    }


    /**
     * Sets the info value for this PostExceptionInfo.
     * 
     * @param info
     */
    public void setInfo(java.lang.String info) {
        this.info = info;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PostExceptionInfo)) return false;
        PostExceptionInfo other = (PostExceptionInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.exception==null && other.getException()==null) || 
             (this.exception!=null &&
              this.exception.equals(other.getException()))) &&
            this.localMessageId == other.getLocalMessageId() &&
            ((this.info==null && other.getInfo()==null) || 
             (this.info!=null &&
              this.info.equals(other.getInfo())));
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
        if (getException() != null) {
            _hashCode += getException().hashCode();
        }
        _hashCode += getLocalMessageId();
        if (getInfo() != null) {
            _hashCode += getInfo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PostExceptionInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "PostExceptionInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("exception");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "exception"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("localMessageId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "localMessageId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("info");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "info"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
