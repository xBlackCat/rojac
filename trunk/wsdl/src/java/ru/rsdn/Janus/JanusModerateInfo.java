/**
 * JanusModerateInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.rsdn.Janus;

public class JanusModerateInfo  implements java.io.Serializable {
    private int messageId;

    private int userId;

    private int forumId;

    private java.util.Calendar create;

    public JanusModerateInfo() {
    }

    public JanusModerateInfo(
           int messageId,
           int userId,
           int forumId,
           java.util.Calendar create) {
           this.messageId = messageId;
           this.userId = userId;
           this.forumId = forumId;
           this.create = create;
    }


    /**
     * Gets the messageId value for this JanusModerateInfo.
     * 
     * @return messageId
     */
    public int getMessageId() {
        return messageId;
    }


    /**
     * Sets the messageId value for this JanusModerateInfo.
     * 
     * @param messageId
     */
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }


    /**
     * Gets the userId value for this JanusModerateInfo.
     * 
     * @return userId
     */
    public int getUserId() {
        return userId;
    }


    /**
     * Sets the userId value for this JanusModerateInfo.
     * 
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }


    /**
     * Gets the forumId value for this JanusModerateInfo.
     * 
     * @return forumId
     */
    public int getForumId() {
        return forumId;
    }


    /**
     * Sets the forumId value for this JanusModerateInfo.
     * 
     * @param forumId
     */
    public void setForumId(int forumId) {
        this.forumId = forumId;
    }


    /**
     * Gets the create value for this JanusModerateInfo.
     * 
     * @return create
     */
    public java.util.Calendar getCreate() {
        return create;
    }


    /**
     * Sets the create value for this JanusModerateInfo.
     * 
     * @param create
     */
    public void setCreate(java.util.Calendar create) {
        this.create = create;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof JanusModerateInfo)) return false;
        JanusModerateInfo other = (JanusModerateInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.messageId == other.getMessageId() &&
            this.userId == other.getUserId() &&
            this.forumId == other.getForumId() &&
            ((this.create==null && other.getCreate()==null) || 
             (this.create!=null &&
              this.create.equals(other.getCreate())));
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
        _hashCode += getMessageId();
        _hashCode += getUserId();
        _hashCode += getForumId();
        if (getCreate() != null) {
            _hashCode += getCreate().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(JanusModerateInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusModerateInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("messageId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "messageId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "userId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("forumId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "forumId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("create");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "create"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
