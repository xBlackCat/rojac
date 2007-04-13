/**
 * JanusATSoap12Stub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.rsdn.Janus;

public class JanusATSoap12Stub extends org.apache.axis.client.Stub implements ru.rsdn.Janus.JanusATSoap {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[7];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetTopicByMessage");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "topicRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "TopicRequest"), ru.rsdn.Janus.TopicRequest.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "TopicResponse"));
        oper.setReturnClass(ru.rsdn.Janus.TopicResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "GetTopicByMessageResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetNewData");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "changeRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "ChangeRequest"), ru.rsdn.Janus.ChangeRequest.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "ChangeResponse"));
        oper.setReturnClass(ru.rsdn.Janus.ChangeResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "GetNewDataResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetForumList");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "forumRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "ForumRequest"), ru.rsdn.Janus.ForumRequest.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "ForumResponse"));
        oper.setReturnClass(ru.rsdn.Janus.ForumResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "GetForumListResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetNewUsers");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "userRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "UserRequest"), ru.rsdn.Janus.UserRequest.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "UserResponse"));
        oper.setReturnClass(ru.rsdn.Janus.UserResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "GetNewUsersResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("PostChange");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "postRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "PostRequest"), ru.rsdn.Janus.PostRequest.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("PostChangeCommit");
        oper.setReturnType(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "PostResponse"));
        oper.setReturnClass(ru.rsdn.Janus.PostResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "PostChangeCommitResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("Check");
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[6] = oper;

    }

    public JanusATSoap12Stub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public JanusATSoap12Stub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public JanusATSoap12Stub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "ArrayOfInt");
            cachedSerQNames.add(qName);
            cls = int[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int");
            qName2 = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "int");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "ArrayOfJanusForumGroupInfo");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.JanusForumGroupInfo[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusForumGroupInfo");
            qName2 = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusForumGroupInfo");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "ArrayOfJanusForumInfo");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.JanusForumInfo[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusForumInfo");
            qName2 = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusForumInfo");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "ArrayOfJanusMessageInfo");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.JanusMessageInfo[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusMessageInfo");
            qName2 = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusMessageInfo");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "ArrayOfJanusModerateInfo");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.JanusModerateInfo[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusModerateInfo");
            qName2 = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusModerateInfo");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "ArrayOfJanusRatingInfo");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.JanusRatingInfo[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusRatingInfo");
            qName2 = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusRatingInfo");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "ArrayOfJanusUserInfo");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.JanusUserInfo[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusUserInfo");
            qName2 = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusUserInfo");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "ArrayOfPostExceptionInfo");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.PostExceptionInfo[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "PostExceptionInfo");
            qName2 = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "PostExceptionInfo");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "ArrayOfPostMessageInfo");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.PostMessageInfo[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "PostMessageInfo");
            qName2 = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "PostMessageInfo");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "ArrayOfPostRatingInfo");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.PostRatingInfo[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "PostRatingInfo");
            qName2 = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "PostRatingInfo");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "ArrayOfRequestForumInfo");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.RequestForumInfo[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "RequestForumInfo");
            qName2 = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "RequestForumInfo");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "ChangeRequest");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.ChangeRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "ChangeResponse");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.ChangeResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "ForumRequest");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.ForumRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "ForumResponse");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.ForumResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusForumGroupInfo");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.JanusForumGroupInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusForumInfo");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.JanusForumInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusMessageInfo");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.JanusMessageInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusModerateInfo");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.JanusModerateInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusRatingInfo");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.JanusRatingInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusUserInfo");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.JanusUserInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "PostExceptionInfo");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.PostExceptionInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "PostMessageInfo");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.PostMessageInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "PostRatingInfo");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.PostRatingInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "PostRequest");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.PostRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "PostResponse");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.PostResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "RequestForumInfo");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.RequestForumInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "TopicRequest");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.TopicRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "TopicResponse");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.TopicResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "UserRequest");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.UserRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "UserResponse");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.UserResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "UserRole");
            cachedSerQNames.add(qName);
            cls = ru.rsdn.Janus.UserRole.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public ru.rsdn.Janus.TopicResponse getTopicByMessage(ru.rsdn.Janus.TopicRequest topicRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://rsdn.ru/Janus/GetTopicByMessage");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP12_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "GetTopicByMessage"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {topicRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ru.rsdn.Janus.TopicResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (ru.rsdn.Janus.TopicResponse) org.apache.axis.utils.JavaUtils.convert(_resp, ru.rsdn.Janus.TopicResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ru.rsdn.Janus.ChangeResponse getNewData(ru.rsdn.Janus.ChangeRequest changeRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://rsdn.ru/Janus/GetNewData");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP12_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "GetNewData"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {changeRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ru.rsdn.Janus.ChangeResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (ru.rsdn.Janus.ChangeResponse) org.apache.axis.utils.JavaUtils.convert(_resp, ru.rsdn.Janus.ChangeResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ru.rsdn.Janus.ForumResponse getForumList(ru.rsdn.Janus.ForumRequest forumRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://rsdn.ru/Janus/GetForumList");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP12_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "GetForumList"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {forumRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ru.rsdn.Janus.ForumResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (ru.rsdn.Janus.ForumResponse) org.apache.axis.utils.JavaUtils.convert(_resp, ru.rsdn.Janus.ForumResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ru.rsdn.Janus.UserResponse getNewUsers(ru.rsdn.Janus.UserRequest userRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://rsdn.ru/Janus/GetNewUsers");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP12_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "GetNewUsers"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {userRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ru.rsdn.Janus.UserResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (ru.rsdn.Janus.UserResponse) org.apache.axis.utils.JavaUtils.convert(_resp, ru.rsdn.Janus.UserResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void postChange(ru.rsdn.Janus.PostRequest postRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://rsdn.ru/Janus/PostChange");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP12_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "PostChange"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {postRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ru.rsdn.Janus.PostResponse postChangeCommit() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://rsdn.ru/Janus/PostChangeCommit");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP12_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "PostChangeCommit"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ru.rsdn.Janus.PostResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (ru.rsdn.Janus.PostResponse) org.apache.axis.utils.JavaUtils.convert(_resp, ru.rsdn.Janus.PostResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void check() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://rsdn.ru/Janus/Check");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP12_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "Check"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
