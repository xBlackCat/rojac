/**
 * ChangeResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.rsdn.Janus;

public class ChangeResponse  implements java.io.Serializable {
    private ru.rsdn.Janus.JanusMessageInfo[] newMessages;

    private ru.rsdn.Janus.JanusRatingInfo[] newRating;

    private ru.rsdn.Janus.JanusModerateInfo[] newModerate;

    private byte[] lastRatingRowVersion;

    private byte[] lastForumRowVersion;

    private byte[] lastModerateRowVersion;

    private int userId;

    public ChangeResponse() {
    }

    public ChangeResponse(
           ru.rsdn.Janus.JanusMessageInfo[] newMessages,
           ru.rsdn.Janus.JanusRatingInfo[] newRating,
           ru.rsdn.Janus.JanusModerateInfo[] newModerate,
           byte[] lastRatingRowVersion,
           byte[] lastForumRowVersion,
           byte[] lastModerateRowVersion,
           int userId) {
           this.newMessages = newMessages;
           this.newRating = newRating;
           this.newModerate = newModerate;
           this.lastRatingRowVersion = lastRatingRowVersion;
           this.lastForumRowVersion = lastForumRowVersion;
           this.lastModerateRowVersion = lastModerateRowVersion;
           this.userId = userId;
    }


    /**
     * Gets the newMessages value for this ChangeResponse.
     * 
     * @return newMessages
     */
    public ru.rsdn.Janus.JanusMessageInfo[] getNewMessages() {
        return newMessages;
    }


    /**
     * Sets the newMessages value for this ChangeResponse.
     * 
     * @param newMessages
     */
    public void setNewMessages(ru.rsdn.Janus.JanusMessageInfo[] newMessages) {
        this.newMessages = newMessages;
    }


    /**
     * Gets the newRating value for this ChangeResponse.
     * 
     * @return newRating
     */
    public ru.rsdn.Janus.JanusRatingInfo[] getNewRating() {
        return newRating;
    }


    /**
     * Sets the newRating value for this ChangeResponse.
     * 
     * @param newRating
     */
    public void setNewRating(ru.rsdn.Janus.JanusRatingInfo[] newRating) {
        this.newRating = newRating;
    }


    /**
     * Gets the newModerate value for this ChangeResponse.
     * 
     * @return newModerate
     */
    public ru.rsdn.Janus.JanusModerateInfo[] getNewModerate() {
        return newModerate;
    }


    /**
     * Sets the newModerate value for this ChangeResponse.
     * 
     * @param newModerate
     */
    public void setNewModerate(ru.rsdn.Janus.JanusModerateInfo[] newModerate) {
        this.newModerate = newModerate;
    }


    /**
     * Gets the lastRatingRowVersion value for this ChangeResponse.
     * 
     * @return lastRatingRowVersion
     */
    public byte[] getLastRatingRowVersion() {
        return lastRatingRowVersion;
    }


    /**
     * Sets the lastRatingRowVersion value for this ChangeResponse.
     * 
     * @param lastRatingRowVersion
     */
    public void setLastRatingRowVersion(byte[] lastRatingRowVersion) {
        this.lastRatingRowVersion = lastRatingRowVersion;
    }


    /**
     * Gets the lastForumRowVersion value for this ChangeResponse.
     * 
     * @return lastForumRowVersion
     */
    public byte[] getLastForumRowVersion() {
        return lastForumRowVersion;
    }


    /**
     * Sets the lastForumRowVersion value for this ChangeResponse.
     * 
     * @param lastForumRowVersion
     */
    public void setLastForumRowVersion(byte[] lastForumRowVersion) {
        this.lastForumRowVersion = lastForumRowVersion;
    }


    /**
     * Gets the lastModerateRowVersion value for this ChangeResponse.
     * 
     * @return lastModerateRowVersion
     */
    public byte[] getLastModerateRowVersion() {
        return lastModerateRowVersion;
    }


    /**
     * Sets the lastModerateRowVersion value for this ChangeResponse.
     * 
     * @param lastModerateRowVersion
     */
    public void setLastModerateRowVersion(byte[] lastModerateRowVersion) {
        this.lastModerateRowVersion = lastModerateRowVersion;
    }


    /**
     * Gets the userId value for this ChangeResponse.
     * 
     * @return userId
     */
    public int getUserId() {
        return userId;
    }


    /**
     * Sets the userId value for this ChangeResponse.
     * 
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ChangeResponse)) return false;
        ChangeResponse other = (ChangeResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.newMessages==null && other.getNewMessages()==null) || 
             (this.newMessages!=null &&
              java.util.Arrays.equals(this.newMessages, other.getNewMessages()))) &&
            ((this.newRating==null && other.getNewRating()==null) || 
             (this.newRating!=null &&
              java.util.Arrays.equals(this.newRating, other.getNewRating()))) &&
            ((this.newModerate==null && other.getNewModerate()==null) || 
             (this.newModerate!=null &&
              java.util.Arrays.equals(this.newModerate, other.getNewModerate()))) &&
            ((this.lastRatingRowVersion==null && other.getLastRatingRowVersion()==null) || 
             (this.lastRatingRowVersion!=null &&
              java.util.Arrays.equals(this.lastRatingRowVersion, other.getLastRatingRowVersion()))) &&
            ((this.lastForumRowVersion==null && other.getLastForumRowVersion()==null) || 
             (this.lastForumRowVersion!=null &&
              java.util.Arrays.equals(this.lastForumRowVersion, other.getLastForumRowVersion()))) &&
            ((this.lastModerateRowVersion==null && other.getLastModerateRowVersion()==null) || 
             (this.lastModerateRowVersion!=null &&
              java.util.Arrays.equals(this.lastModerateRowVersion, other.getLastModerateRowVersion()))) &&
            this.userId == other.getUserId();
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
        if (getNewMessages() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getNewMessages());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getNewMessages(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getNewRating() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getNewRating());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getNewRating(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getNewModerate() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getNewModerate());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getNewModerate(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getLastRatingRowVersion() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getLastRatingRowVersion());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getLastRatingRowVersion(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getLastForumRowVersion() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getLastForumRowVersion());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getLastForumRowVersion(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getLastModerateRowVersion() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getLastModerateRowVersion());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getLastModerateRowVersion(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getUserId();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ChangeResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "ChangeResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("newMessages");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "newMessages"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusMessageInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusMessageInfo"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("newRating");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "newRating"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusRatingInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusRatingInfo"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("newModerate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "newModerate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusModerateInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusModerateInfo"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastRatingRowVersion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "lastRatingRowVersion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastForumRowVersion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "lastForumRowVersion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastModerateRowVersion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "lastModerateRowVersion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "userId"));
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
