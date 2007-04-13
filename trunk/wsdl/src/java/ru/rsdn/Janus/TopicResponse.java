/**
 * TopicResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.rsdn.Janus;

public class TopicResponse  implements java.io.Serializable {
    private ru.rsdn.Janus.JanusMessageInfo[] messages;

    private ru.rsdn.Janus.JanusRatingInfo[] rating;

    private ru.rsdn.Janus.JanusModerateInfo[] moderate;

    public TopicResponse() {
    }

    public TopicResponse(
           ru.rsdn.Janus.JanusMessageInfo[] messages,
           ru.rsdn.Janus.JanusRatingInfo[] rating,
           ru.rsdn.Janus.JanusModerateInfo[] moderate) {
           this.messages = messages;
           this.rating = rating;
           this.moderate = moderate;
    }


    /**
     * Gets the messages value for this TopicResponse.
     * 
     * @return messages
     */
    public ru.rsdn.Janus.JanusMessageInfo[] getMessages() {
        return messages;
    }


    /**
     * Sets the messages value for this TopicResponse.
     * 
     * @param messages
     */
    public void setMessages(ru.rsdn.Janus.JanusMessageInfo[] messages) {
        this.messages = messages;
    }


    /**
     * Gets the rating value for this TopicResponse.
     * 
     * @return rating
     */
    public ru.rsdn.Janus.JanusRatingInfo[] getRating() {
        return rating;
    }


    /**
     * Sets the rating value for this TopicResponse.
     * 
     * @param rating
     */
    public void setRating(ru.rsdn.Janus.JanusRatingInfo[] rating) {
        this.rating = rating;
    }


    /**
     * Gets the moderate value for this TopicResponse.
     * 
     * @return moderate
     */
    public ru.rsdn.Janus.JanusModerateInfo[] getModerate() {
        return moderate;
    }


    /**
     * Sets the moderate value for this TopicResponse.
     * 
     * @param moderate
     */
    public void setModerate(ru.rsdn.Janus.JanusModerateInfo[] moderate) {
        this.moderate = moderate;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TopicResponse)) return false;
        TopicResponse other = (TopicResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.messages==null && other.getMessages()==null) || 
             (this.messages!=null &&
              java.util.Arrays.equals(this.messages, other.getMessages()))) &&
            ((this.rating==null && other.getRating()==null) || 
             (this.rating!=null &&
              java.util.Arrays.equals(this.rating, other.getRating()))) &&
            ((this.moderate==null && other.getModerate()==null) || 
             (this.moderate!=null &&
              java.util.Arrays.equals(this.moderate, other.getModerate())));
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
        if (getMessages() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMessages());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMessages(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getRating() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRating());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRating(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getModerate() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getModerate());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getModerate(), i);
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
        new org.apache.axis.description.TypeDesc(TopicResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "TopicResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("messages");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "Messages"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusMessageInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusMessageInfo"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rating");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "Rating"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusRatingInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusRatingInfo"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("moderate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "Moderate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusModerateInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusModerateInfo"));
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
