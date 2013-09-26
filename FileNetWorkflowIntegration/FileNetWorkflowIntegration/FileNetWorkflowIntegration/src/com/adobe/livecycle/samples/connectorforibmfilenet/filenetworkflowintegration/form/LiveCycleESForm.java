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

import com.adobe.idp.services.SamplesFileNetWorkflowIntegration_Processes_TemplateFileNetWorkflowStepRenderService;
import com.adobe.idp.services.SamplesFileNetWorkflowIntegration_Processes_TemplateFileNetWorkflowStepSubmitService;
import com.adobe.livecycle.samples.connectorforibmfilenet.filenetworkflowintegration.utilities.LiveCycleESServices;


/**
 * LiveCycle ES Form
 */
public class LiveCycleESForm implements ILiveCycleESForm 
{
    public com.adobe.idp.services.BLOB renderForm(
            String serviceName, String invocationUserName, String invocationPassword, 
            java.util.HashMap inputDataMap)
    throws javax.xml.rpc.ServiceException, java.rmi.RemoteException 
    {
        try
        {
            SamplesFileNetWorkflowIntegration_Processes_TemplateFileNetWorkflowStepRenderService _templateFileNetWorkflowStepRenderService = 
                LiveCycleESServices.getTemplateFileNetWorkflowStepRenderService(serviceName, 
                        invocationUserName, invocationPassword);

            return _templateFileNetWorkflowStepRenderService.invoke(inputDataMap);
        }
        catch (java.net.MalformedURLException e)
        {
            throw new javax.xml.rpc.ServiceException(e);
        }
    }

    public void submitForm(String serviceName, String invocationUserName, String invocationPassword,
            java.util.HashMap inputDataMap, com.adobe.idp.services.BLOB submittedDocument)
    throws javax.xml.rpc.ServiceException, java.rmi.RemoteException 
    {
        try
        {
            SamplesFileNetWorkflowIntegration_Processes_TemplateFileNetWorkflowStepSubmitService _templateFileNetWorkflowStepSubmitService = 
                LiveCycleESServices.getTemplateFileNetWorkflowStepSubmitService(serviceName, 
                        invocationUserName, invocationPassword);

            _templateFileNetWorkflowStepSubmitService.invoke(inputDataMap, submittedDocument);            
        }
        catch (java.net.MalformedURLException e)
        {
            throw new javax.xml.rpc.ServiceException(e);
        }
    }
}