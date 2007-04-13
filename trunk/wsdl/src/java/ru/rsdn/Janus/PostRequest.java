/**
 * PostRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.rsdn.Janus;

public class PostRequest  implements java.io.Serializable {
    private java.lang.String userName;

    private java.lang.String password;

    private ru.rsdn.Janus.PostMessageInfo[] writedMessages;

    private ru.rsdn.Janus.PostRatingInfo[] rates;

    public PostRequest() {
    }

    public PostRequest(
           java.lang.String userName,
           java.lang.String password,
           ru.rsdn.Janus.PostMessageInfo[] writedMessages,
           ru.rsdn.Janus.PostRatingInfo[] rates) {
           this.userName = userName;
           this.password = password;
           this.writedMessages = writedMessages;
           this.rates = rates;
    }


    /**
     * Gets the userName value for this PostRequest.
     * 
     * @return userName
     */
    public java.lang.String getUserName() {
        return userName;
    }


    /**
     * Sets the userName value for this PostRequest.
     * 
     * @param userName
     */
    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }


    /**
     * Gets the password value for this PostRequest.
     * 
     * @return password
     */
    public java.lang.String getPassword() {
        return password;
    }


    /**
     * Sets the password value for this PostRequest.
     * 
     * @param password
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }


    /**
     * Gets the writedMessages value for this PostRequest.
     * 
     * @return writedMessages
     */
    public ru.rsdn.Janus.PostMessageInfo[] getWritedMessages() {
        return writedMessages;
    }


    /**
     * Sets the writedMessages value for this PostRequest.
     * 
     * @param writedMessages
     */
    public void setWritedMessages(ru.rsdn.Janus.PostMessageInfo[] writedMessages) {
        this.writedMessages = writedMessages;
    }


    /**
     * Gets the rates value for this PostRequest.
     * 
     * @return rates
     */
    public ru.rsdn.Janus.PostRatingInfo[] getRates() {
        return rates;
    }


    /**
     * Sets the rates value for this PostRequest.
     * 
     * @param rates
     */
    public void setRates(ru.rsdn.Janus.PostRatingInfo[] rates) {
        this.rates = rates;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PostRequest)) return false;
        PostRequest other = (PostRequest) obj;
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
            ((this.writedMessages==null && other.getWritedMessages()==null) || 
             (this.writedMessages!=null &&
              java.util.Arrays.equals(this.writedMessages, other.getWritedMessages()))) &&
            ((this.rates==null && other.getRates()==null) || 
             (this.rates!=null &&
              java.util.Arrays.equals(this.rates, other.getRates())));
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
        if (getWritedMessages() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getWritedMessages());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getWritedMessages(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getRates() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRates());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRates(), i);
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
        new org.apache.axis.description.TypeDesc(PostRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "PostRequest"));
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
        elemField.setFieldName("writedMessages");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "writedMessages"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "PostMessageInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "PostMessageInfo"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rates");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "rates"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "PostRatingInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "PostRatingInfo"));
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
