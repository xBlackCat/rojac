/**
 * JanusUserInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.rsdn.Janus;

public class JanusUserInfo  implements java.io.Serializable {
    private int userId;

    private java.lang.String userName;

    private java.lang.String userNick;

    private java.lang.String realName;

    private java.lang.String publicEmail;

    private java.lang.String homePage;

    private java.lang.String specialization;

    private java.lang.String whereFrom;

    private java.lang.String origin;

    public JanusUserInfo() {
    }

    public JanusUserInfo(
           int userId,
           java.lang.String userName,
           java.lang.String userNick,
           java.lang.String realName,
           java.lang.String publicEmail,
           java.lang.String homePage,
           java.lang.String specialization,
           java.lang.String whereFrom,
           java.lang.String origin) {
           this.userId = userId;
           this.userName = userName;
           this.userNick = userNick;
           this.realName = realName;
           this.publicEmail = publicEmail;
           this.homePage = homePage;
           this.specialization = specialization;
           this.whereFrom = whereFrom;
           this.origin = origin;
    }


    /**
     * Gets the userId value for this JanusUserInfo.
     * 
     * @return userId
     */
    public int getUserId() {
        return userId;
    }


    /**
     * Sets the userId value for this JanusUserInfo.
     * 
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }


    /**
     * Gets the userName value for this JanusUserInfo.
     * 
     * @return userName
     */
    public java.lang.String getUserName() {
        return userName;
    }


    /**
     * Sets the userName value for this JanusUserInfo.
     * 
     * @param userName
     */
    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }


    /**
     * Gets the userNick value for this JanusUserInfo.
     * 
     * @return userNick
     */
    public java.lang.String getUserNick() {
        return userNick;
    }


    /**
     * Sets the userNick value for this JanusUserInfo.
     * 
     * @param userNick
     */
    public void setUserNick(java.lang.String userNick) {
        this.userNick = userNick;
    }


    /**
     * Gets the realName value for this JanusUserInfo.
     * 
     * @return realName
     */
    public java.lang.String getRealName() {
        return realName;
    }


    /**
     * Sets the realName value for this JanusUserInfo.
     * 
     * @param realName
     */
    public void setRealName(java.lang.String realName) {
        this.realName = realName;
    }


    /**
     * Gets the publicEmail value for this JanusUserInfo.
     * 
     * @return publicEmail
     */
    public java.lang.String getPublicEmail() {
        return publicEmail;
    }


    /**
     * Sets the publicEmail value for this JanusUserInfo.
     * 
     * @param publicEmail
     */
    public void setPublicEmail(java.lang.String publicEmail) {
        this.publicEmail = publicEmail;
    }


    /**
     * Gets the homePage value for this JanusUserInfo.
     * 
     * @return homePage
     */
    public java.lang.String getHomePage() {
        return homePage;
    }


    /**
     * Sets the homePage value for this JanusUserInfo.
     * 
     * @param homePage
     */
    public void setHomePage(java.lang.String homePage) {
        this.homePage = homePage;
    }


    /**
     * Gets the specialization value for this JanusUserInfo.
     * 
     * @return specialization
     */
    public java.lang.String getSpecialization() {
        return specialization;
    }


    /**
     * Sets the specialization value for this JanusUserInfo.
     * 
     * @param specialization
     */
    public void setSpecialization(java.lang.String specialization) {
        this.specialization = specialization;
    }


    /**
     * Gets the whereFrom value for this JanusUserInfo.
     * 
     * @return whereFrom
     */
    public java.lang.String getWhereFrom() {
        return whereFrom;
    }


    /**
     * Sets the whereFrom value for this JanusUserInfo.
     * 
     * @param whereFrom
     */
    public void setWhereFrom(java.lang.String whereFrom) {
        this.whereFrom = whereFrom;
    }


    /**
     * Gets the origin value for this JanusUserInfo.
     * 
     * @return origin
     */
    public java.lang.String getOrigin() {
        return origin;
    }


    /**
     * Sets the origin value for this JanusUserInfo.
     * 
     * @param origin
     */
    public void setOrigin(java.lang.String origin) {
        this.origin = origin;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof JanusUserInfo)) return false;
        JanusUserInfo other = (JanusUserInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.userId == other.getUserId() &&
            ((this.userName==null && other.getUserName()==null) || 
             (this.userName!=null &&
              this.userName.equals(other.getUserName()))) &&
            ((this.userNick==null && other.getUserNick()==null) || 
             (this.userNick!=null &&
              this.userNick.equals(other.getUserNick()))) &&
            ((this.realName==null && other.getRealName()==null) || 
             (this.realName!=null &&
              this.realName.equals(other.getRealName()))) &&
            ((this.publicEmail==null && other.getPublicEmail()==null) || 
             (this.publicEmail!=null &&
              this.publicEmail.equals(other.getPublicEmail()))) &&
            ((this.homePage==null && other.getHomePage()==null) || 
             (this.homePage!=null &&
              this.homePage.equals(other.getHomePage()))) &&
            ((this.specialization==null && other.getSpecialization()==null) || 
             (this.specialization!=null &&
              this.specialization.equals(other.getSpecialization()))) &&
            ((this.whereFrom==null && other.getWhereFrom()==null) || 
             (this.whereFrom!=null &&
              this.whereFrom.equals(other.getWhereFrom()))) &&
            ((this.origin==null && other.getOrigin()==null) || 
             (this.origin!=null &&
              this.origin.equals(other.getOrigin())));
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
        _hashCode += getUserId();
        if (getUserName() != null) {
            _hashCode += getUserName().hashCode();
        }
        if (getUserNick() != null) {
            _hashCode += getUserNick().hashCode();
        }
        if (getRealName() != null) {
            _hashCode += getRealName().hashCode();
        }
        if (getPublicEmail() != null) {
            _hashCode += getPublicEmail().hashCode();
        }
        if (getHomePage() != null) {
            _hashCode += getHomePage().hashCode();
        }
        if (getSpecialization() != null) {
            _hashCode += getSpecialization().hashCode();
        }
        if (getWhereFrom() != null) {
            _hashCode += getWhereFrom().hashCode();
        }
        if (getOrigin() != null) {
            _hashCode += getOrigin().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(JanusUserInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusUserInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "userId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "userName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userNick");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "userNick"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("realName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "realName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("publicEmail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "publicEmail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("homePage");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "homePage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("specialization");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "specialization"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("whereFrom");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "whereFrom"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("origin");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "origin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
