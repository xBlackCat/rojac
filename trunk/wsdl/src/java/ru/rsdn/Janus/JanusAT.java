/**
 * JanusAT.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.rsdn.Janus;

public interface JanusAT extends javax.xml.rpc.Service {
    public java.lang.String getJanusATSoap12Address();

    public ru.rsdn.Janus.JanusATSoap getJanusATSoap12() throws javax.xml.rpc.ServiceException;

    public ru.rsdn.Janus.JanusATSoap getJanusATSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
    public java.lang.String getJanusATSoapAddress();

    public ru.rsdn.Janus.JanusATSoap getJanusATSoap() throws javax.xml.rpc.ServiceException;

    public ru.rsdn.Janus.JanusATSoap getJanusATSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
