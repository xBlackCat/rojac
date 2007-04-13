/**
 * PostResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.rsdn.Janus;

public class PostResponse  implements java.io.Serializable {
    private int[] commitedIds;

    private ru.rsdn.Janus.PostExceptionInfo[] exceptions;

    public PostResponse() {
    }

    public PostResponse(
           int[] commitedIds,
           ru.rsdn.Janus.PostExceptionInfo[] exceptions) {
           this.commitedIds = commitedIds;
           this.exceptions = exceptions;
    }


    /**
     * Gets the commitedIds value for this PostResponse.
     * 
     * @return commitedIds
     */
    public int[] getCommitedIds() {
        return commitedIds;
    }


    /**
     * Sets the commitedIds value for this PostResponse.
     * 
     * @param commitedIds
     */
    public void setCommitedIds(int[] commitedIds) {
        this.commitedIds = commitedIds;
    }


    /**
     * Gets the exceptions value for this PostResponse.
     * 
     * @return exceptions
     */
    public ru.rsdn.Janus.PostExceptionInfo[] getExceptions() {
        return exceptions;
    }


    /**
     * Sets the exceptions value for this PostResponse.
     * 
     * @param exceptions
     */
    public void setExceptions(ru.rsdn.Janus.PostExceptionInfo[] exceptions) {
        this.exceptions = exceptions;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PostResponse)) return false;
        PostResponse other = (PostResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.commitedIds==null && other.getCommitedIds()==null) || 
             (this.commitedIds!=null &&
              java.util.Arrays.equals(this.commitedIds, other.getCommitedIds()))) &&
            ((this.exceptions==null && other.getExceptions()==null) || 
             (this.exceptions!=null &&
              java.util.Arrays.equals(this.exceptions, other.getExceptions())));
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
        if (getCommitedIds() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCommitedIds());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCommitedIds(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getExceptions() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getExceptions());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getExceptions(), i);
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
        new org.apache.axis.description.TypeDesc(PostResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "PostResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("commitedIds");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "commitedIds"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("exceptions");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "exceptions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "PostExceptionInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "PostExceptionInfo"));
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
