/**
 * JanusForumInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.rsdn.Janus;

public class JanusForumInfo  implements java.io.Serializable {
    private int forumId;

    private int forumGroupId;

    private java.lang.String shortForumName;

    private java.lang.String forumName;

    private int rated;

    private int inTop;

    private int rateLimit;

    public JanusForumInfo() {
    }

    public JanusForumInfo(
           int forumId,
           int forumGroupId,
           java.lang.String shortForumName,
           java.lang.String forumName,
           int rated,
           int inTop,
           int rateLimit) {
           this.forumId = forumId;
           this.forumGroupId = forumGroupId;
           this.shortForumName = shortForumName;
           this.forumName = forumName;
           this.rated = rated;
           this.inTop = inTop;
           this.rateLimit = rateLimit;
    }


    /**
     * Gets the forumId value for this JanusForumInfo.
     * 
     * @return forumId
     */
    public int getForumId() {
        return forumId;
    }


    /**
     * Sets the forumId value for this JanusForumInfo.
     * 
     * @param forumId
     */
    public void setForumId(int forumId) {
        this.forumId = forumId;
    }


    /**
     * Gets the forumGroupId value for this JanusForumInfo.
     * 
     * @return forumGroupId
     */
    public int getForumGroupId() {
        return forumGroupId;
    }


    /**
     * Sets the forumGroupId value for this JanusForumInfo.
     * 
     * @param forumGroupId
     */
    public void setForumGroupId(int forumGroupId) {
        this.forumGroupId = forumGroupId;
    }


    /**
     * Gets the shortForumName value for this JanusForumInfo.
     * 
     * @return shortForumName
     */
    public java.lang.String getShortForumName() {
        return shortForumName;
    }


    /**
     * Sets the shortForumName value for this JanusForumInfo.
     * 
     * @param shortForumName
     */
    public void setShortForumName(java.lang.String shortForumName) {
        this.shortForumName = shortForumName;
    }


    /**
     * Gets the forumName value for this JanusForumInfo.
     * 
     * @return forumName
     */
    public java.lang.String getForumName() {
        return forumName;
    }


    /**
     * Sets the forumName value for this JanusForumInfo.
     * 
     * @param forumName
     */
    public void setForumName(java.lang.String forumName) {
        this.forumName = forumName;
    }


    /**
     * Gets the rated value for this JanusForumInfo.
     * 
     * @return rated
     */
    public int getRated() {
        return rated;
    }


    /**
     * Sets the rated value for this JanusForumInfo.
     * 
     * @param rated
     */
    public void setRated(int rated) {
        this.rated = rated;
    }


    /**
     * Gets the inTop value for this JanusForumInfo.
     * 
     * @return inTop
     */
    public int getInTop() {
        return inTop;
    }


    /**
     * Sets the inTop value for this JanusForumInfo.
     * 
     * @param inTop
     */
    public void setInTop(int inTop) {
        this.inTop = inTop;
    }


    /**
     * Gets the rateLimit value for this JanusForumInfo.
     * 
     * @return rateLimit
     */
    public int getRateLimit() {
        return rateLimit;
    }


    /**
     * Sets the rateLimit value for this JanusForumInfo.
     * 
     * @param rateLimit
     */
    public void setRateLimit(int rateLimit) {
        this.rateLimit = rateLimit;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof JanusForumInfo)) return false;
        JanusForumInfo other = (JanusForumInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.forumId == other.getForumId() &&
            this.forumGroupId == other.getForumGroupId() &&
            ((this.shortForumName==null && other.getShortForumName()==null) || 
             (this.shortForumName!=null &&
              this.shortForumName.equals(other.getShortForumName()))) &&
            ((this.forumName==null && other.getForumName()==null) || 
             (this.forumName!=null &&
              this.forumName.equals(other.getForumName()))) &&
            this.rated == other.getRated() &&
            this.inTop == other.getInTop() &&
            this.rateLimit == other.getRateLimit();
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
        _hashCode += getForumId();
        _hashCode += getForumGroupId();
        if (getShortForumName() != null) {
            _hashCode += getShortForumName().hashCode();
        }
        if (getForumName() != null) {
            _hashCode += getForumName().hashCode();
        }
        _hashCode += getRated();
        _hashCode += getInTop();
        _hashCode += getRateLimit();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(JanusForumInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusForumInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("forumId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "forumId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("forumGroupId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "forumGroupId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shortForumName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "shortForumName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("forumName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "forumName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rated");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "rated"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inTop");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "inTop"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rateLimit");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "rateLimit"));
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
