/**
 * JanusATLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.rsdn.Janus;

public class JanusATLocator extends org.apache.axis.client.Service implements ru.rsdn.Janus.JanusAT {

    public JanusATLocator() {
    }


    public JanusATLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public JanusATLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for JanusATSoap12
    private java.lang.String JanusATSoap12_address = "http://rsdn.ru/ws/JanusAT.asmx";

    public java.lang.String getJanusATSoap12Address() {
        return JanusATSoap12_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String JanusATSoap12WSDDServiceName = "JanusATSoap12";

    public java.lang.String getJanusATSoap12WSDDServiceName() {
        return JanusATSoap12WSDDServiceName;
    }

    public void setJanusATSoap12WSDDServiceName(java.lang.String name) {
        JanusATSoap12WSDDServiceName = name;
    }

    public ru.rsdn.Janus.JanusATSoap getJanusATSoap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(JanusATSoap12_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getJanusATSoap12(endpoint);
    }

    public ru.rsdn.Janus.JanusATSoap getJanusATSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            ru.rsdn.Janus.JanusATSoap12Stub _stub = new ru.rsdn.Janus.JanusATSoap12Stub(portAddress, this);
            _stub.setPortName(getJanusATSoap12WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setJanusATSoap12EndpointAddress(java.lang.String address) {
        JanusATSoap12_address = address;
    }


    // Use to get a proxy class for JanusATSoap
    private java.lang.String JanusATSoap_address = "http://rsdn.ru/ws/JanusAT.asmx";

    public java.lang.String getJanusATSoapAddress() {
        return JanusATSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String JanusATSoapWSDDServiceName = "JanusATSoap";

    public java.lang.String getJanusATSoapWSDDServiceName() {
        return JanusATSoapWSDDServiceName;
    }

    public void setJanusATSoapWSDDServiceName(java.lang.String name) {
        JanusATSoapWSDDServiceName = name;
    }

    public ru.rsdn.Janus.JanusATSoap getJanusATSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(JanusATSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getJanusATSoap(endpoint);
    }

    public ru.rsdn.Janus.JanusATSoap getJanusATSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            ru.rsdn.Janus.JanusATSoapStub _stub = new ru.rsdn.Janus.JanusATSoapStub(portAddress, this);
            _stub.setPortName(getJanusATSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setJanusATSoapEndpointAddress(java.lang.String address) {
        JanusATSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     * This service has multiple ports for a given interface;
     * the proxy implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (ru.rsdn.Janus.JanusATSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                ru.rsdn.Janus.JanusATSoap12Stub _stub = new ru.rsdn.Janus.JanusATSoap12Stub(new java.net.URL(JanusATSoap12_address), this);
                _stub.setPortName(getJanusATSoap12WSDDServiceName());
                return _stub;
            }
            if (ru.rsdn.Janus.JanusATSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                ru.rsdn.Janus.JanusATSoapStub _stub = new ru.rsdn.Janus.JanusATSoapStub(new java.net.URL(JanusATSoap_address), this);
                _stub.setPortName(getJanusATSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("JanusATSoap12".equals(inputPortName)) {
            return getJanusATSoap12();
        }
        else if ("JanusATSoap".equals(inputPortName)) {
            return getJanusATSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusAT");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusATSoap12"));
            ports.add(new javax.xml.namespace.QName("http://rsdn.ru/Janus/", "JanusATSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("JanusATSoap12".equals(portName)) {
            setJanusATSoap12EndpointAddress(address);
        }
        else 
if ("JanusATSoap".equals(portName)) {
            setJanusATSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
