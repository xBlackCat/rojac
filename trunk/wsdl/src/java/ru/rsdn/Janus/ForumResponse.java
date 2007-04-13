/**
 * ForumResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.rsdn.Janus;

public class ForumResponse  implements java.io.Serializable {
    private ru.rsdn.Janus.JanusForumInfo[] forumList;

    private ru.rsdn.Janus.JanusForumGroupInfo[] groupList;

    public ForumResponse() {
    }

    public ForumResponse(
           ru.rsdn.Janus.JanusForumInfo[] forumList,
           ru.rsdn.Janus.JanusForumGroupInfo[] groupList) {
           this.forumList = forumList;
           this.groupList = groupList;
    }


    /**
     * Gets the forumList value for this ForumResponse.
     * 
     * @return forumList
     */
    public ru.rsdn.Janus.JanusForumInfo[] getForumList() {
        return forumList;
    }


    /**
     * Sets the forumList value for this ForumResponse.
     * 
     * @param forumList
     */
    public void setForumList(ru.rsdn.Janus.JanusForumInfo[] forumList) {
        this.forumList = forumList;
    }


    /**
     * Gets the groupList value for this ForumResponse.
     * 
     * @return groupList
     */
    public ru.rsdn.Janus.JanusForumGroupInfo[] getGroupList() {
        return groupList;
    }


    /**
     * Sets the groupList value for this ForumResponse.
     * 
     * @param groupList
     */
    public void setGroupList(ru.rsdn.Janus.JanusForumGroupInfo[] groupList) {
        this.groupList = groupList;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ForumResponse)) return false;
        ForumResponse other = (ForumResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.forumList==null && other.getForumList()==null) || 
             (this.forumList!=null &&
              java.util.Arrays.equals(this.forumList, other.getForumList()))) &&
            ((this.groupList==null && other.getGroupList()==null) || 
             (this.groupList!=null &&
              java.util.Arrays.equals(this.groupList, other.getGroupList())));
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
        if (getForumList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getForumList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getForumList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getGroupList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getGroupList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getGroupList(), i);
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
        new org.apache.axis.description.TypeDesc(ForumResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "ForumResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("forumList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "forumList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusForumInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusForumInfo"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("groupList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "groupList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusForumGroupInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusForumGroupInfo"));
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
