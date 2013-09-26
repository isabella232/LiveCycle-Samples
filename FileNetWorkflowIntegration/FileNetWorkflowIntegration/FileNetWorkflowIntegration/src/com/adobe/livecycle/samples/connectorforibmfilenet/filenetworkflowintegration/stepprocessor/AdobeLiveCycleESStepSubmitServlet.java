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
package com.adobe.livecycle.samples.connectorforibmfilenet.filenetworkflowintegration.stepprocessor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.adobe.livecycle.samples.connectorforibmfilenet.filenetworkflowintegration.form.ILiveCycleESForm;
import com.adobe.livecycle.samples.connectorforibmfilenet.filenetworkflowintegration.form.LiveCycleESForm;
import com.adobe.livecycle.samples.connectorforibmfilenet.filenetworkflowintegration.utilities.Constants;
import com.adobe.livecycle.samples.connectorforibmfilenet.filenetworkflowintegration.utilities.PEStepElementUtilities;
import com.adobe.livecycle.samples.connectorforibmfilenet.filenetworkflowintegration.utilities.HttpServletUtilities;

import filenet.vw.api.VWSession;
import filenet.vw.api.VWStepElement;


/** 
 * Submits the document associated with the workflow step
 */
public class AdobeLiveCycleESStepSubmitServlet extends HttpServlet
{
    
    private static final long serialVersionUID = 8230343956574230988L;
    
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
            System.out.println("Enter: AdobeLiveCycleESStepSubmitServlet");
            
            // Step: Get Session attributes
            HttpSession _httpSession = aRequest.getSession();
            String _userID = (String) _httpSession.getAttribute(Constants.SESSION_USERID);
            String _password  = (String) _httpSession.getAttribute(Constants.SESSION_PASSWORD);
            String _routerURL = (String) _httpSession.getAttribute(Constants.SESSION_ROUTER_URL);
            String _queueName = (String) _httpSession.getAttribute(Constants.SESSION_QUEUE_NAME);
            String _wobNum = (String) _httpSession.getAttribute(Constants.SESSION_WOB_NUM);

            System.out.println("Session Attributes: ");
            System.out.println("User ID: " + _userID);
            System.out.println("Password: " + _password);
            System.out.println("Router URL: " + _routerURL);
            System.out.println("Queue Name: " + _queueName);
            System.out.println("Work Object Number: " + _wobNum);
            
            // Step: Setup input parameters for service invocation
            VWSession _processEngineSession = new VWSession(_userID, _password, _routerURL);
            VWStepElement _stepElement = PEStepElementUtilities.getStepElement(_processEngineSession, _queueName, _wobNum);
            
            System.out.println("Step Element: " + _stepElement!=null?_stepElement.getWorkObjectNumber():null);
            
            String _submitServiceName = (String) _stepElement.getParameterValue(Constants.STEP_PARAMETER_SUBMIT_SVC_NAME);            
            _processEngineSession.logoff();
            System.out.println("Submit Service Name: " + _submitServiceName);
            
            java.util.HashMap _serviceInputDataMap = new java.util.HashMap();
            _serviceInputDataMap.put(Constants.ENVIRONMENT_VARIABLES, HttpServletUtilities.getEnvironmentVariables(aRequest));
            _serviceInputDataMap.put(Constants.USERID, _userID);
            _serviceInputDataMap.put(Constants.PASSWORD, _password);
            _serviceInputDataMap.put(Constants.QUEUE_NAME, _queueName);
            _serviceInputDataMap.put(Constants.WORK_OBJECT_NUMBER, _wobNum);
            System.out.println("\tInput Data Map=" + _serviceInputDataMap);

            com.adobe.idp.services.BLOB _submittedDocument = new com.adobe.idp.services.BLOB();
            _submittedDocument.setBinaryData(HttpServletUtilities.getRequestBuffer(aRequest));
            System.out.println("\tBlob Data=" + _submittedDocument);
            
            // Step: Invoke LiveCycle ES submit service
            ILiveCycleESForm _liveCycleESForm = new LiveCycleESForm();
            _liveCycleESForm.submitForm(_submitServiceName, _userID, _password, 
                    _serviceInputDataMap, _submittedDocument
            );

            // Step: Close browser window
            HttpServletUtilities.closeWindowUsingJavaScript(aResponse);
            
            // TODO: Refresh the Inbox page as a last step. See the sample custom step module for details...
            System.out.println("Exit: AdobeLiveCycleESStepSubmitServlet");
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            aResponse.getOutputStream().write(("The following exception occurred: " + e.getMessage()).getBytes());
        } 
    }
}