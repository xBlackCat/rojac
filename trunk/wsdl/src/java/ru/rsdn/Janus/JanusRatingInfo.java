/**
 * JanusRatingInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.rsdn.Janus;

public class JanusRatingInfo  implements java.io.Serializable {
    private int messageId;

    private int topicId;

    private int userId;

    private int userRating;

    private int rate;

    private java.util.Calendar rateDate;

    public JanusRatingInfo() {
    }

    public JanusRatingInfo(
           int messageId,
           int topicId,
           int userId,
           int userRating,
           int rate,
           java.util.Calendar rateDate) {
           this.messageId = messageId;
           this.topicId = topicId;
           this.userId = userId;
           this.userRating = userRating;
           this.rate = rate;
           this.rateDate = rateDate;
    }


    /**
     * Gets the messageId value for this JanusRatingInfo.
     * 
     * @return messageId
     */
    public int getMessageId() {
        return messageId;
    }


    /**
     * Sets the messageId value for this JanusRatingInfo.
     * 
     * @param messageId
     */
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }


    /**
     * Gets the topicId value for this JanusRatingInfo.
     * 
     * @return topicId
     */
    public int getTopicId() {
        return topicId;
    }


    /**
     * Sets the topicId value for this JanusRatingInfo.
     * 
     * @param topicId
     */
    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }


    /**
     * Gets the userId value for this JanusRatingInfo.
     * 
     * @return userId
     */
    public int getUserId() {
        return userId;
    }


    /**
     * Sets the userId value for this JanusRatingInfo.
     * 
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }


    /**
     * Gets the userRating value for this JanusRatingInfo.
     * 
     * @return userRating
     */
    public int getUserRating() {
        return userRating;
    }


    /**
     * Sets the userRating value for this JanusRatingInfo.
     * 
     * @param userRating
     */
    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }


    /**
     * Gets the rate value for this JanusRatingInfo.
     * 
     * @return rate
     */
    public int getRate() {
        return rate;
    }


    /**
     * Sets the rate value for this JanusRatingInfo.
     * 
     * @param rate
     */
    public void setRate(int rate) {
        this.rate = rate;
    }


    /**
     * Gets the rateDate value for this JanusRatingInfo.
     * 
     * @return rateDate
     */
    public java.util.Calendar getRateDate() {
        return rateDate;
    }


    /**
     * Sets the rateDate value for this JanusRatingInfo.
     * 
     * @param rateDate
     */
    public void setRateDate(java.util.Calendar rateDate) {
        this.rateDate = rateDate;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof JanusRatingInfo)) return false;
        JanusRatingInfo other = (JanusRatingInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.messageId == other.getMessageId() &&
            this.topicId == other.getTopicId() &&
            this.userId == other.getUserId() &&
            this.userRating == other.getUserRating() &&
            this.rate == other.getRate() &&
            ((this.rateDate==null && other.getRateDate()==null) || 
             (this.rateDate!=null &&
              this.rateDate.equals(other.getRateDate())));
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
        _hashCode += getTopicId();
        _hashCode += getUserId();
        _hashCode += getUserRating();
        _hashCode += getRate();
        if (getRateDate() != null) {
            _hashCode += getRateDate().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(JanusRatingInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusRatingInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("messageId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "messageId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("topicId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "topicId"));
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
        elemField.setFieldName("userRating");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "userRating"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "rate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rateDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "rateDate"));
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
