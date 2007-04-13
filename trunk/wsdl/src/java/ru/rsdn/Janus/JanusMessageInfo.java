/**
 * JanusMessageInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.rsdn.Janus;

public class JanusMessageInfo  implements java.io.Serializable {
    private int messageId;

    private int topicId;

    private int parentId;

    private int userId;

    private int forumId;

    private java.lang.String subject;

    private java.lang.String messageName;

    private java.lang.String userNick;

    private java.lang.String message;

    private int articleId;

    private java.util.Calendar messageDate;

    private java.util.Calendar updateDate;

    private ru.rsdn.Janus.UserRole userRole;

    private java.lang.String userTitle;

    private int userTitleColor;

    private java.util.Calendar lastModerated;

    public JanusMessageInfo() {
    }

    public JanusMessageInfo(
           int messageId,
           int topicId,
           int parentId,
           int userId,
           int forumId,
           java.lang.String subject,
           java.lang.String messageName,
           java.lang.String userNick,
           java.lang.String message,
           int articleId,
           java.util.Calendar messageDate,
           java.util.Calendar updateDate,
           ru.rsdn.Janus.UserRole userRole,
           java.lang.String userTitle,
           int userTitleColor,
           java.util.Calendar lastModerated) {
           this.messageId = messageId;
           this.topicId = topicId;
           this.parentId = parentId;
           this.userId = userId;
           this.forumId = forumId;
           this.subject = subject;
           this.messageName = messageName;
           this.userNick = userNick;
           this.message = message;
           this.articleId = articleId;
           this.messageDate = messageDate;
           this.updateDate = updateDate;
           this.userRole = userRole;
           this.userTitle = userTitle;
           this.userTitleColor = userTitleColor;
           this.lastModerated = lastModerated;
    }


    /**
     * Gets the messageId value for this JanusMessageInfo.
     * 
     * @return messageId
     */
    public int getMessageId() {
        return messageId;
    }


    /**
     * Sets the messageId value for this JanusMessageInfo.
     * 
     * @param messageId
     */
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }


    /**
     * Gets the topicId value for this JanusMessageInfo.
     * 
     * @return topicId
     */
    public int getTopicId() {
        return topicId;
    }


    /**
     * Sets the topicId value for this JanusMessageInfo.
     * 
     * @param topicId
     */
    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }


    /**
     * Gets the parentId value for this JanusMessageInfo.
     * 
     * @return parentId
     */
    public int getParentId() {
        return parentId;
    }


    /**
     * Sets the parentId value for this JanusMessageInfo.
     * 
     * @param parentId
     */
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }


    /**
     * Gets the userId value for this JanusMessageInfo.
     * 
     * @return userId
     */
    public int getUserId() {
        return userId;
    }


    /**
     * Sets the userId value for this JanusMessageInfo.
     * 
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }


    /**
     * Gets the forumId value for this JanusMessageInfo.
     * 
     * @return forumId
     */
    public int getForumId() {
        return forumId;
    }


    /**
     * Sets the forumId value for this JanusMessageInfo.
     * 
     * @param forumId
     */
    public void setForumId(int forumId) {
        this.forumId = forumId;
    }


    /**
     * Gets the subject value for this JanusMessageInfo.
     * 
     * @return subject
     */
    public java.lang.String getSubject() {
        return subject;
    }


    /**
     * Sets the subject value for this JanusMessageInfo.
     * 
     * @param subject
     */
    public void setSubject(java.lang.String subject) {
        this.subject = subject;
    }


    /**
     * Gets the messageName value for this JanusMessageInfo.
     * 
     * @return messageName
     */
    public java.lang.String getMessageName() {
        return messageName;
    }


    /**
     * Sets the messageName value for this JanusMessageInfo.
     * 
     * @param messageName
     */
    public void setMessageName(java.lang.String messageName) {
        this.messageName = messageName;
    }


    /**
     * Gets the userNick value for this JanusMessageInfo.
     * 
     * @return userNick
     */
    public java.lang.String getUserNick() {
        return userNick;
    }


    /**
     * Sets the userNick value for this JanusMessageInfo.
     * 
     * @param userNick
     */
    public void setUserNick(java.lang.String userNick) {
        this.userNick = userNick;
    }


    /**
     * Gets the message value for this JanusMessageInfo.
     * 
     * @return message
     */
    public java.lang.String getMessage() {
        return message;
    }


    /**
     * Sets the message value for this JanusMessageInfo.
     * 
     * @param message
     */
    public void setMessage(java.lang.String message) {
        this.message = message;
    }


    /**
     * Gets the articleId value for this JanusMessageInfo.
     * 
     * @return articleId
     */
    public int getArticleId() {
        return articleId;
    }


    /**
     * Sets the articleId value for this JanusMessageInfo.
     * 
     * @param articleId
     */
    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }


    /**
     * Gets the messageDate value for this JanusMessageInfo.
     * 
     * @return messageDate
     */
    public java.util.Calendar getMessageDate() {
        return messageDate;
    }


    /**
     * Sets the messageDate value for this JanusMessageInfo.
     * 
     * @param messageDate
     */
    public void setMessageDate(java.util.Calendar messageDate) {
        this.messageDate = messageDate;
    }


    /**
     * Gets the updateDate value for this JanusMessageInfo.
     * 
     * @return updateDate
     */
    public java.util.Calendar getUpdateDate() {
        return updateDate;
    }


    /**
     * Sets the updateDate value for this JanusMessageInfo.
     * 
     * @param updateDate
     */
    public void setUpdateDate(java.util.Calendar updateDate) {
        this.updateDate = updateDate;
    }


    /**
     * Gets the userRole value for this JanusMessageInfo.
     * 
     * @return userRole
     */
    public ru.rsdn.Janus.UserRole getUserRole() {
        return userRole;
    }


    /**
     * Sets the userRole value for this JanusMessageInfo.
     * 
     * @param userRole
     */
    public void setUserRole(ru.rsdn.Janus.UserRole userRole) {
        this.userRole = userRole;
    }


    /**
     * Gets the userTitle value for this JanusMessageInfo.
     * 
     * @return userTitle
     */
    public java.lang.String getUserTitle() {
        return userTitle;
    }


    /**
     * Sets the userTitle value for this JanusMessageInfo.
     * 
     * @param userTitle
     */
    public void setUserTitle(java.lang.String userTitle) {
        this.userTitle = userTitle;
    }


    /**
     * Gets the userTitleColor value for this JanusMessageInfo.
     * 
     * @return userTitleColor
     */
    public int getUserTitleColor() {
        return userTitleColor;
    }


    /**
     * Sets the userTitleColor value for this JanusMessageInfo.
     * 
     * @param userTitleColor
     */
    public void setUserTitleColor(int userTitleColor) {
        this.userTitleColor = userTitleColor;
    }


    /**
     * Gets the lastModerated value for this JanusMessageInfo.
     * 
     * @return lastModerated
     */
    public java.util.Calendar getLastModerated() {
        return lastModerated;
    }


    /**
     * Sets the lastModerated value for this JanusMessageInfo.
     * 
     * @param lastModerated
     */
    public void setLastModerated(java.util.Calendar lastModerated) {
        this.lastModerated = lastModerated;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof JanusMessageInfo)) return false;
        JanusMessageInfo other = (JanusMessageInfo) obj;
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
            this.parentId == other.getParentId() &&
            this.userId == other.getUserId() &&
            this.forumId == other.getForumId() &&
            ((this.subject==null && other.getSubject()==null) || 
             (this.subject!=null &&
              this.subject.equals(other.getSubject()))) &&
            ((this.messageName==null && other.getMessageName()==null) || 
             (this.messageName!=null &&
              this.messageName.equals(other.getMessageName()))) &&
            ((this.userNick==null && other.getUserNick()==null) || 
             (this.userNick!=null &&
              this.userNick.equals(other.getUserNick()))) &&
            ((this.message==null && other.getMessage()==null) || 
             (this.message!=null &&
              this.message.equals(other.getMessage()))) &&
            this.articleId == other.getArticleId() &&
            ((this.messageDate==null && other.getMessageDate()==null) || 
             (this.messageDate!=null &&
              this.messageDate.equals(other.getMessageDate()))) &&
            ((this.updateDate==null && other.getUpdateDate()==null) || 
             (this.updateDate!=null &&
              this.updateDate.equals(other.getUpdateDate()))) &&
            ((this.userRole==null && other.getUserRole()==null) || 
             (this.userRole!=null &&
              this.userRole.equals(other.getUserRole()))) &&
            ((this.userTitle==null && other.getUserTitle()==null) || 
             (this.userTitle!=null &&
              this.userTitle.equals(other.getUserTitle()))) &&
            this.userTitleColor == other.getUserTitleColor() &&
            ((this.lastModerated==null && other.getLastModerated()==null) || 
             (this.lastModerated!=null &&
              this.lastModerated.equals(other.getLastModerated())));
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
        _hashCode += getParentId();
        _hashCode += getUserId();
        _hashCode += getForumId();
        if (getSubject() != null) {
            _hashCode += getSubject().hashCode();
        }
        if (getMessageName() != null) {
            _hashCode += getMessageName().hashCode();
        }
        if (getUserNick() != null) {
            _hashCode += getUserNick().hashCode();
        }
        if (getMessage() != null) {
            _hashCode += getMessage().hashCode();
        }
        _hashCode += getArticleId();
        if (getMessageDate() != null) {
            _hashCode += getMessageDate().hashCode();
        }
        if (getUpdateDate() != null) {
            _hashCode += getUpdateDate().hashCode();
        }
        if (getUserRole() != null) {
            _hashCode += getUserRole().hashCode();
        }
        if (getUserTitle() != null) {
            _hashCode += getUserTitle().hashCode();
        }
        _hashCode += getUserTitleColor();
        if (getLastModerated() != null) {
            _hashCode += getLastModerated().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(JanusMessageInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusMessageInfo"));
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
        elemField.setFieldName("parentId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "parentId"));
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
        elemField.setFieldName("subject");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "subject"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("messageName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "messageName"));
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
        elemField.setFieldName("message");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "message"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("articleId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "articleId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("messageDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "messageDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("updateDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "updateDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userRole");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "userRole"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "UserRole"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userTitle");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "userTitle"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userTitleColor");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "userTitleColor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastModerated");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "lastModerated"));
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
