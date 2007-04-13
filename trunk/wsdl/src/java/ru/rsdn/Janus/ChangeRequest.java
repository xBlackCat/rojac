/**
 * ChangeRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.rsdn.Janus;

public class ChangeRequest  implements java.io.Serializable {
    private java.lang.String userName;

    private java.lang.String password;

    private ru.rsdn.Janus.RequestForumInfo[] subscribedForums;

    private byte[] ratingRowVersion;

    private byte[] messageRowVersion;

    private byte[] moderateRowVersion;

    private int[] breakMsgIds;

    private int[] breakTopicIds;

    private int maxOutput;

    public ChangeRequest() {
    }

    public ChangeRequest(
           java.lang.String userName,
           java.lang.String password,
           ru.rsdn.Janus.RequestForumInfo[] subscribedForums,
           byte[] ratingRowVersion,
           byte[] messageRowVersion,
           byte[] moderateRowVersion,
           int[] breakMsgIds,
           int[] breakTopicIds,
           int maxOutput) {
           this.userName = userName;
           this.password = password;
           this.subscribedForums = subscribedForums;
           this.ratingRowVersion = ratingRowVersion;
           this.messageRowVersion = messageRowVersion;
           this.moderateRowVersion = moderateRowVersion;
           this.breakMsgIds = breakMsgIds;
           this.breakTopicIds = breakTopicIds;
           this.maxOutput = maxOutput;
    }


    /**
     * Gets the userName value for this ChangeRequest.
     * 
     * @return userName
     */
    public java.lang.String getUserName() {
        return userName;
    }


    /**
     * Sets the userName value for this ChangeRequest.
     * 
     * @param userName
     */
    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }


    /**
     * Gets the password value for this ChangeRequest.
     * 
     * @return password
     */
    public java.lang.String getPassword() {
        return password;
    }


    /**
     * Sets the password value for this ChangeRequest.
     * 
     * @param password
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }


    /**
     * Gets the subscribedForums value for this ChangeRequest.
     * 
     * @return subscribedForums
     */
    public ru.rsdn.Janus.RequestForumInfo[] getSubscribedForums() {
        return subscribedForums;
    }


    /**
     * Sets the subscribedForums value for this ChangeRequest.
     * 
     * @param subscribedForums
     */
    public void setSubscribedForums(ru.rsdn.Janus.RequestForumInfo[] subscribedForums) {
        this.subscribedForums = subscribedForums;
    }


    /**
     * Gets the ratingRowVersion value for this ChangeRequest.
     * 
     * @return ratingRowVersion
     */
    public byte[] getRatingRowVersion() {
        return ratingRowVersion;
    }


    /**
     * Sets the ratingRowVersion value for this ChangeRequest.
     * 
     * @param ratingRowVersion
     */
    public void setRatingRowVersion(byte[] ratingRowVersion) {
        this.ratingRowVersion = ratingRowVersion;
    }


    /**
     * Gets the messageRowVersion value for this ChangeRequest.
     * 
     * @return messageRowVersion
     */
    public byte[] getMessageRowVersion() {
        return messageRowVersion;
    }


    /**
     * Sets the messageRowVersion value for this ChangeRequest.
     * 
     * @param messageRowVersion
     */
    public void setMessageRowVersion(byte[] messageRowVersion) {
        this.messageRowVersion = messageRowVersion;
    }


    /**
     * Gets the moderateRowVersion value for this ChangeRequest.
     * 
     * @return moderateRowVersion
     */
    public byte[] getModerateRowVersion() {
        return moderateRowVersion;
    }


    /**
     * Sets the moderateRowVersion value for this ChangeRequest.
     * 
     * @param moderateRowVersion
     */
    public void setModerateRowVersion(byte[] moderateRowVersion) {
        this.moderateRowVersion = moderateRowVersion;
    }


    /**
     * Gets the breakMsgIds value for this ChangeRequest.
     * 
     * @return breakMsgIds
     */
    public int[] getBreakMsgIds() {
        return breakMsgIds;
    }


    /**
     * Sets the breakMsgIds value for this ChangeRequest.
     * 
     * @param breakMsgIds
     */
    public void setBreakMsgIds(int[] breakMsgIds) {
        this.breakMsgIds = breakMsgIds;
    }


    /**
     * Gets the breakTopicIds value for this ChangeRequest.
     * 
     * @return breakTopicIds
     */
    public int[] getBreakTopicIds() {
        return breakTopicIds;
    }


    /**
     * Sets the breakTopicIds value for this ChangeRequest.
     * 
     * @param breakTopicIds
     */
    public void setBreakTopicIds(int[] breakTopicIds) {
        this.breakTopicIds = breakTopicIds;
    }


    /**
     * Gets the maxOutput value for this ChangeRequest.
     * 
     * @return maxOutput
     */
    public int getMaxOutput() {
        return maxOutput;
    }


    /**
     * Sets the maxOutput value for this ChangeRequest.
     * 
     * @param maxOutput
     */
    public void setMaxOutput(int maxOutput) {
        this.maxOutput = maxOutput;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ChangeRequest)) return false;
        ChangeRequest other = (ChangeRequest) obj;
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
            ((this.subscribedForums==null && other.getSubscribedForums()==null) || 
             (this.subscribedForums!=null &&
              java.util.Arrays.equals(this.subscribedForums, other.getSubscribedForums()))) &&
            ((this.ratingRowVersion==null && other.getRatingRowVersion()==null) || 
             (this.ratingRowVersion!=null &&
              java.util.Arrays.equals(this.ratingRowVersion, other.getRatingRowVersion()))) &&
            ((this.messageRowVersion==null && other.getMessageRowVersion()==null) || 
             (this.messageRowVersion!=null &&
              java.util.Arrays.equals(this.messageRowVersion, other.getMessageRowVersion()))) &&
            ((this.moderateRowVersion==null && other.getModerateRowVersion()==null) || 
             (this.moderateRowVersion!=null &&
              java.util.Arrays.equals(this.moderateRowVersion, other.getModerateRowVersion()))) &&
            ((this.breakMsgIds==null && other.getBreakMsgIds()==null) || 
             (this.breakMsgIds!=null &&
              java.util.Arrays.equals(this.breakMsgIds, other.getBreakMsgIds()))) &&
            ((this.breakTopicIds==null && other.getBreakTopicIds()==null) || 
             (this.breakTopicIds!=null &&
              java.util.Arrays.equals(this.breakTopicIds, other.getBreakTopicIds()))) &&
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
        if (getSubscribedForums() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSubscribedForums());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSubscribedForums(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getRatingRowVersion() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRatingRowVersion());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRatingRowVersion(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getMessageRowVersion() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMessageRowVersion());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMessageRowVersion(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getModerateRowVersion() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getModerateRowVersion());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getModerateRowVersion(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getBreakMsgIds() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getBreakMsgIds());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getBreakMsgIds(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getBreakTopicIds() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getBreakTopicIds());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getBreakTopicIds(), i);
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
        new org.apache.axis.description.TypeDesc(ChangeRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "ChangeRequest"));
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
        elemField.setFieldName("subscribedForums");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "subscribedForums"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "RequestForumInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "RequestForumInfo"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ratingRowVersion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "ratingRowVersion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("messageRowVersion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "messageRowVersion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("moderateRowVersion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "moderateRowVersion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("breakMsgIds");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "breakMsgIds"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("breakTopicIds");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "breakTopicIds"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "int"));
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
