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
package com.adobe.livecycle.samples.connectorforibmfilenet.filenetworkflowintegration.utilities;

import com.adobe.idp.services.SamplesConnectorforIBMFileNet_Processes_ArchiveSubmittedDataWithNRFileNet;
import com.adobe.idp.services.SamplesConnectorforIBMFileNet_Processes_ArchiveSubmittedDataWithNRFileNetServiceLocator;
import com.adobe.idp.services.SamplesConnectorforIBMFileNet_Processes_RenderFormsFromNRFileNet;
import com.adobe.idp.services.SamplesConnectorforIBMFileNet_Processes_RenderFormsFromNRFileNetServiceLocator;
import com.adobe.idp.services.SamplesFileNetWorkflowIntegration_Processes_TemplateFileNetWorkflowStepRenderService;
import com.adobe.idp.services.SamplesFileNetWorkflowIntegration_Processes_TemplateFileNetWorkflowStepRenderServiceServiceLocator;
import com.adobe.idp.services.SamplesFileNetWorkflowIntegration_Processes_TemplateFileNetWorkflowStepSubmitService;
import com.adobe.idp.services.SamplesFileNetWorkflowIntegration_Processes_TemplateFileNetWorkflowStepSubmitServiceServiceLocator;

/** LiveCycle ES Services */
public class LiveCycleESServices
{
    /** Returns the WSDL URL for the LiveCycle service */  
    private static java.net.URL getURLForService(String serviceName) throws java.net.MalformedURLException 
    {
        String _lcBaseURL = new Configuration().getLCBaseURL();
        return new java.net.URL(_lcBaseURL + "/soap/services/" + serviceName + "?blob=base64"); 
    }

    /** Sets the invocation credentials for the service stub */
    private static void setCredentials(javax.xml.rpc.Stub serviceStub, String invocationUserName, String invocationPassword)
    {
        serviceStub._setProperty(javax.xml.rpc.Stub.USERNAME_PROPERTY, invocationUserName);
        serviceStub._setProperty(javax.xml.rpc.Stub.PASSWORD_PROPERTY, invocationPassword);
    }
        
    /** Returns an instance of the RenderFormsFromNRWithDataFilenet service **/
    public static SamplesConnectorforIBMFileNet_Processes_RenderFormsFromNRFileNet getRenderFormsFromNRFilenetService(
            String invocationUserName, String invocationPassword) 
    throws java.net.MalformedURLException, javax.xml.rpc.ServiceException
    {
        SamplesConnectorforIBMFileNet_Processes_RenderFormsFromNRFileNetServiceLocator _renderFormsFromNRFilenetServiceLocator = 
            new SamplesConnectorforIBMFileNet_Processes_RenderFormsFromNRFileNetServiceLocator();
        SamplesConnectorforIBMFileNet_Processes_RenderFormsFromNRFileNet _renderFormsFromNRFilenetServiceClient = 
            _renderFormsFromNRFilenetServiceLocator.getRenderFormsFromNRFileNet();

        setCredentials((javax.xml.rpc.Stub)_renderFormsFromNRFilenetServiceClient, 
                invocationUserName, invocationPassword);
        
        return _renderFormsFromNRFilenetServiceClient;
    }

    /** Returns an instance of the SamplesConnectorforIBMFileNetProcessesArchiveSubmittedDataWithNRFilenet service **/
    public static SamplesConnectorforIBMFileNet_Processes_ArchiveSubmittedDataWithNRFileNet getArchiveSubmittedDataWithNRFilenetService(
            String invocationUserName, String invocationPassword) 
    throws java.net.MalformedURLException, javax.xml.rpc.ServiceException
    {
        
        SamplesConnectorforIBMFileNet_Processes_ArchiveSubmittedDataWithNRFileNetServiceLocator _archiveSubmittedDataWithNRFileNetServiceLocator = 
            new SamplesConnectorforIBMFileNet_Processes_ArchiveSubmittedDataWithNRFileNetServiceLocator();
        SamplesConnectorforIBMFileNet_Processes_ArchiveSubmittedDataWithNRFileNet _archiveSubmittedDataWithNRFilenetServiceClient = 
            _archiveSubmittedDataWithNRFileNetServiceLocator.getArchiveSubmittedDataWithNRFileNet();
        
        setCredentials((javax.xml.rpc.Stub)_archiveSubmittedDataWithNRFilenetServiceClient, invocationUserName, invocationPassword);
        
        return _archiveSubmittedDataWithNRFilenetServiceClient;
    }

    /** Returns an instance of the SamplesFileNetWorkflowIntegrationProcessesTemplateFileNetWorkflowStepRenderService service **/
    public static SamplesFileNetWorkflowIntegration_Processes_TemplateFileNetWorkflowStepRenderService getTemplateFileNetWorkflowStepRenderService(
            String serviceName, String invocationUserName, String invocationPassword) 
    throws java.net.MalformedURLException, javax.xml.rpc.ServiceException
    {
        java.net.URL _serviceURL = getURLForService(serviceName);
        
        SamplesFileNetWorkflowIntegration_Processes_TemplateFileNetWorkflowStepRenderServiceServiceLocator _templateFileNetWorkflowStepRenderServiceServiceLocator = 
            new SamplesFileNetWorkflowIntegration_Processes_TemplateFileNetWorkflowStepRenderServiceServiceLocator();
        SamplesFileNetWorkflowIntegration_Processes_TemplateFileNetWorkflowStepRenderService _templateFileNetWorkflowStepRenderServiceClient = 
            _templateFileNetWorkflowStepRenderServiceServiceLocator.getTemplateFileNetWorkflowStepRenderService(_serviceURL); 

        setCredentials((javax.xml.rpc.Stub)_templateFileNetWorkflowStepRenderServiceClient, invocationUserName, invocationPassword);
        
        return _templateFileNetWorkflowStepRenderServiceClient;
    }

    /** Returns an instance of the SamplesFileNetWorkflowIntegrationProcessesTemplateFileNetWorkflowStepSubmitService service **/
    public static SamplesFileNetWorkflowIntegration_Processes_TemplateFileNetWorkflowStepSubmitService getTemplateFileNetWorkflowStepSubmitService(
            String serviceName, String invocationUserName, String invocationPassword) 
    throws java.net.MalformedURLException, javax.xml.rpc.ServiceException
    {
        java.net.URL _serviceURL = getURLForService(serviceName);
        
        SamplesFileNetWorkflowIntegration_Processes_TemplateFileNetWorkflowStepSubmitServiceServiceLocator _templateFileNetWorkflowStepSubmitServiceLocator = 
            new SamplesFileNetWorkflowIntegration_Processes_TemplateFileNetWorkflowStepSubmitServiceServiceLocator();
        SamplesFileNetWorkflowIntegration_Processes_TemplateFileNetWorkflowStepSubmitService _templateFileNetWorkflowStepSubmitServiceClient = 
            _templateFileNetWorkflowStepSubmitServiceLocator.getTemplateFileNetWorkflowStepSubmitService(_serviceURL);

        setCredentials((javax.xml.rpc.Stub)_templateFileNetWorkflowStepSubmitServiceClient, invocationUserName, invocationPassword);
        
        return _templateFileNetWorkflowStepSubmitServiceClient;
    }    
}