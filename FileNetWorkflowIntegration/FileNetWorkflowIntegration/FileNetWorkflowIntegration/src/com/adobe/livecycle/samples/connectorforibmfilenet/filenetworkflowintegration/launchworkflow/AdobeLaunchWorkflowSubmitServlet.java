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
package com.adobe.livecycle.samples.connectorforibmfilenet.filenetworkflowintegration.launchworkflow;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.adobe.idp.services.SamplesConnectorforIBMFileNet_Processes_ArchiveSubmittedDataWithNRFileNet;
import com.adobe.livecycle.samples.connectorforibmfilenet.filenetworkflowintegration.utilities.Configuration;
import com.adobe.livecycle.samples.connectorforibmfilenet.filenetworkflowintegration.utilities.Constants;
import com.adobe.livecycle.samples.connectorforibmfilenet.filenetworkflowintegration.utilities.HttpServletUtilities;
import com.adobe.livecycle.samples.connectorforibmfilenet.filenetworkflowintegration.utilities.LiveCycleESServices;
import com.adobe.livecycle.samples.connectorforibmfilenet.filenetworkflowintegration.utilities.PEWorkflowUtilities;

public class AdobeLaunchWorkflowSubmitServlet extends HttpServlet 
{
    private static final long serialVersionUID = 730343984932065490L;
    
    /*
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, 
     * javax.servlet.http.HttpServletResponse)
     */
    protected void doGet(HttpServletRequest aRequest, HttpServletResponse aResponse) 
    throws ServletException, IOException
    {
        processRequest(aRequest, aResponse);
    }

    /*
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, 
     * javax.servlet.http.HttpServletResponse)
     */
    protected void doPost(HttpServletRequest aRequest, HttpServletResponse aResponse) 
    throws ServletException, IOException
    {
        processRequest(aRequest, aResponse);
    }

    /** Handles the get/post request */
    public void processRequest(HttpServletRequest aRequest, HttpServletResponse aResponse) 
    throws ServletException, IOException 
    {
        try
        {
            System.out.println("Enter: AdobeLaunchWorkflowSubmitServlet");

            // Step: Get session parameters saved by AdobeLaunchFileNetWorkflowSubmit servlet
            HttpSession _httpSession = aRequest.getSession();
            String _userID = (String)_httpSession.getAttribute(Constants.SESSION_USERID);
            String _password =(String) _httpSession.getAttribute(Constants.SESSION_PASSWORD);
            String _objectStoreName = (String)_httpSession.getAttribute(Constants.SESSION_OBJECT_STORE_NAME);
            String _folderPath = (String)_httpSession.getAttribute(Constants.SESSION_FOLDER_PATH);
            String _xdpFileName = (String)_httpSession.getAttribute(Constants.SESSION_XDP_FILE_NAME);
            _httpSession.removeAttribute(Constants.SESSION_USERID);
            _httpSession.removeAttribute(Constants.SESSION_PASSWORD);
            _httpSession.removeAttribute(Constants.SESSION_OBJECT_STORE_NAME);
            _httpSession.removeAttribute(Constants.SESSION_FOLDER_PATH);
            _httpSession.removeAttribute(Constants.SESSION_XDP_FILE_NAME);

            // Step: Prepare invocation parameters
            String _xdpPath = _folderPath + "/" + _xdpFileName; 
            String _xmlDataFilePrefix = getXDPName(_xdpFileName);
            String _environmentVariables = HttpServletUtilities.getEnvironmentVariables(aRequest);
            byte[] _submittedFormData = HttpServletUtilities.getRequestBuffer(aRequest);

            com.adobe.idp.services.BLOB _submittedDataBLOB = new com.adobe.idp.services.BLOB();
            _submittedDataBLOB.setBinaryData(_submittedFormData);
            
            // Step: Invoke Archive Submitted Data service
            SamplesConnectorforIBMFileNet_Processes_ArchiveSubmittedDataWithNRFileNet _archiveSubmittedDataWithNRServiceClient = 
                LiveCycleESServices.getArchiveSubmittedDataWithNRFilenetService(_userID, _password);

            System.out.println("Invoking Archive Submitted Data Service with arguments:");
            System.out.println("\tEnvironment Variables=" + _environmentVariables);
            System.out.println("\tUser Name=" + _userID);
            System.out.println("\tPassword=" + _password);
            System.out.println("\tObject Store Name=" + _objectStoreName);
            System.out.println("\tSubmittedData=" + _submittedDataBLOB);
            System.out.println("\tXDPPath=" + _xdpPath);
            System.out.println("\tXMLDataFilePrefix=" + _xmlDataFilePrefix);

            // To use FileNet session token for authentication do the following: 
            // [1] Create a session token and pass for the null parameter below.
            // [2] Choose 'Use FileNet Credentials token' in Login settings parameter in the FileNet
            //     Content Repository Connector operations used in the ArchiveSubmittedDataWithNR-FileNet orchestration
            java.util.HashMap _outputMap = _archiveSubmittedDataWithNRServiceClient.invoke(
                    "/LiveCycleES/ConnectorForIBMFileNet/ArchiveSubmittedDataWithNR-FileNet/Data",_environmentVariables,_password, null, _userID, 
                    _objectStoreName, _submittedDataBLOB, _xdpPath, _xmlDataFilePrefix);
            System.out.println("Invocation Result Map=" + _outputMap);  
            
            // Step: Launch workflow
            Configuration _configurationProps = new Configuration();
            PEWorkflowUtilities.prepareAndLaunchWorkflow(_userID, _password, 
                    _configurationProps.getFileNetRouterURL(),
                    _configurationProps.getSampleWorkflowDefinitionFileLocation(),
                    _outputMap);
            
            // Step: Close browser window
            HttpServletUtilities.closeWindowUsingJavaScript(aResponse);
            System.out.println("Exit: AdobeLaunchWorkflowSubmitServlet");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            aResponse.getOutputStream().write(("The following exception occurred: " + e.getMessage()).getBytes());
        }
    }

    /** Gets the XDP name (sans the part after extension) */
    private String getXDPName(String xdpFileName)
    {
        String _xmlDataFilePrefix = xdpFileName;
        int indexOfFirstDot = xdpFileName.indexOf(".");
        
        if (indexOfFirstDot != -1)
            _xmlDataFilePrefix = xdpFileName.substring(0, indexOfFirstDot);
        
        return _xmlDataFilePrefix;
    }
}