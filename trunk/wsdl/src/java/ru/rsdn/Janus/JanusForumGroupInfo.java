/**
 * JanusForumGroupInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.rsdn.Janus;

public class JanusForumGroupInfo  implements java.io.Serializable {
    private int forumGroupId;

    private java.lang.String forumGroupName;

    private int sortOrder;

    public JanusForumGroupInfo() {
    }

    public JanusForumGroupInfo(
           int forumGroupId,
           java.lang.String forumGroupName,
           int sortOrder) {
           this.forumGroupId = forumGroupId;
           this.forumGroupName = forumGroupName;
           this.sortOrder = sortOrder;
    }


    /**
     * Gets the forumGroupId value for this JanusForumGroupInfo.
     * 
     * @return forumGroupId
     */
    public int getForumGroupId() {
        return forumGroupId;
    }


    /**
     * Sets the forumGroupId value for this JanusForumGroupInfo.
     * 
     * @param forumGroupId
     */
    public void setForumGroupId(int forumGroupId) {
        this.forumGroupId = forumGroupId;
    }


    /**
     * Gets the forumGroupName value for this JanusForumGroupInfo.
     * 
     * @return forumGroupName
     */
    public java.lang.String getForumGroupName() {
        return forumGroupName;
    }


    /**
     * Sets the forumGroupName value for this JanusForumGroupInfo.
     * 
     * @param forumGroupName
     */
    public void setForumGroupName(java.lang.String forumGroupName) {
        this.forumGroupName = forumGroupName;
    }


    /**
     * Gets the sortOrder value for this JanusForumGroupInfo.
     * 
     * @return sortOrder
     */
    public int getSortOrder() {
        return sortOrder;
    }


    /**
     * Sets the sortOrder value for this JanusForumGroupInfo.
     * 
     * @param sortOrder
     */
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof JanusForumGroupInfo)) return false;
        JanusForumGroupInfo other = (JanusForumGroupInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.forumGroupId == other.getForumGroupId() &&
            ((this.forumGroupName==null && other.getForumGroupName()==null) || 
             (this.forumGroupName!=null &&
              this.forumGroupName.equals(other.getForumGroupName()))) &&
            this.sortOrder == other.getSortOrder();
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
        _hashCode += getForumGroupId();
        if (getForumGroupName() != null) {
            _hashCode += getForumGroupName().hashCode();
        }
        _hashCode += getSortOrder();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(JanusForumGroupInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusForumGroupInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("forumGroupId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "forumGroupId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("forumGroupName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "forumGroupName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sortOrder");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "sortOrder"));
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
