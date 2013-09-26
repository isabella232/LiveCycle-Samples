/**
 * ADOBE SYSTEMS INCORPORATED
 * Copyright 2007 Adobe Systems Incorporated
 * All Rights Reserved
 * 
 * NOTICE:  Adobe permits you to use, modify, and distribute this file in accordance with the
 * terms of the Adobe license agreement accompanying it.  If you have received this file from a
 * source other than Adobe, then your use, modification, or distribution of it requires the prior
 * written permission of Adobe.
 */
package com.adobe.livecycle.samples.connectorforibmfilenet.filenetworkflowintegration.form;



/**
 * Represents operations that can be invoked LiveCycle Form
 */
public interface ILiveCycleESForm 
{
    /**
     * Method to be invoked when the form needs to be rendered
     * 
     * @param serviceName           Name of the LiveCycle service to be used as the render service
     * @param invocationUserName    User name to be used for invocation
     * @param invocationPassword    Password to be used for invocation
     * @param inputDataMap          Map containing input data for the render service
     * @return BLOB containing the document to be rendered
     * @throws java.rmi.RemoteException
     */
    public com.adobe.idp.services.BLOB renderForm(String serviceName, String invocationUserName, String invocationPassword, 
            java.util.HashMap inputDataMap) 
    throws javax.xml.rpc.ServiceException, java.rmi.RemoteException;

    /**
     * Method to be invoked when the form is submitted
     *  
     * @param serviceName           Name of the LiveCycle service that will be used as a submit service
     * @param invocationUserName    User name to be used for invocation
     * @param invocationPassword    Password to be used for invocation
     * @param inputDataMap          Map containing input data for the submit service
     * @param submittedDocument     BLOB containing submitted document for the submit service   
     * @throws java.rmi.RemoteException
     */
    public void submitForm(String serviceName, String invocationUserName, String invocationPassword,
            java.util.HashMap inputDataMap, com.adobe.idp.services.BLOB submittedDocument)
    throws javax.xml.rpc.ServiceException, java.rmi.RemoteException;
}