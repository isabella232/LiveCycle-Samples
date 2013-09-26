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
 * Renders the document associated with the workflow step
 */
public class AdobeLiveCycleESStepRenderServlet extends HttpServlet
{
    private static final long serialVersionUID = 7823034435435809090L;
    
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
        
    /** Handles the GET and POST requests */
    public void processRequest(HttpServletRequest aRequest, HttpServletResponse aResponse) 
    throws ServletException, IOException 
    {
        try
        {
            System.out.println("Enter: AdobeLiveCycleESStepRenderServlet");

            //Step: Get Session attributes
            HttpSession _httpSession = aRequest.getSession();
            String _userID = (String) _httpSession.getAttribute(Constants.SESSION_USERID);
            String _password  = (String) _httpSession.getAttribute(Constants.SESSION_PASSWORD);
            String _routerURL = (String) _httpSession.getAttribute(Constants.SESSION_ROUTER_URL);
            String _queueName = (String) _httpSession.getAttribute(Constants.SESSION_QUEUE_NAME);
            String _wobNum = (String) _httpSession.getAttribute(Constants.SESSION_WOB_NUM);

            // Step: Setup input parameters for the service invocation
            VWSession _processEngineSession = new VWSession(_userID, _password, _routerURL);
            VWStepElement _stepElement = PEStepElementUtilities.getStepElement(_processEngineSession, _queueName, _wobNum);

            String _renderServiceName = (String) _stepElement.getParameterValue(Constants.STEP_PARAMETER_RENDER_SVC_NAME);
            System.out.println("Render Service Name: " + _renderServiceName);
            
            _processEngineSession.logoff();            

            java.util.HashMap _serviceInputDataMap = new java.util.HashMap();
            _serviceInputDataMap.put(Constants.USERID, _userID);
            _serviceInputDataMap.put(Constants.PASSWORD, _password);
            _serviceInputDataMap.put(Constants.QUEUE_NAME, _queueName);
            _serviceInputDataMap.put(Constants.WORK_OBJECT_NUMBER, _wobNum);
            _serviceInputDataMap.put(Constants.SUBMIT_URL, 
                    HttpServletUtilities.getAbsoluteURLForServlet(aRequest, Constants.RELATIVE_PATH_ADOBE_LC_ES_STEP_SUBMIT_SERVLET));
            System.out.println("\tInput Data Map=" + _serviceInputDataMap);
            
            // Invoke LiveCycle ES render service
            ILiveCycleESForm _liveCycleESForm = new LiveCycleESForm();
            com.adobe.idp.services.BLOB _renderedDocument = 
                _liveCycleESForm.renderForm(_renderServiceName, _userID, _password, 
                        _serviceInputDataMap
                );

            // Write document to response stream
            HttpServletUtilities.writeBLOBToResponseStream(_renderedDocument, aResponse);
            System.out.println("Exit: AdobeLiveCycleESStepRenderServlet");            
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            aResponse.getOutputStream().write(("The following exception occurred: " + e.getMessage()).getBytes());
        } 
    }   
}