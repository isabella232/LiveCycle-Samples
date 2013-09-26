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
package com.adobe.livecycle.samples.connectorforibmfilenet.filenetworkflowintegration.stepprocessor.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.adobe.livecycle.samples.connectorforibmfilenet.filenetworkflowintegration.utilities.Constants;
import com.filenet.wcm.toolkit.server.base.WcmController;
import com.filenet.wcm.toolkit.server.dp.WcmEProcessDataProvider;
import com.filenet.wcm.toolkit.server.ui.WcmJSPModule;
import com.filenet.wcm.toolkit.server.util.WcmEProcessStateKeys;
import com.filenet.wcm.toolkit.server.util.WcmParameter;
import com.filenet.wcm.toolkit.server.util.WcmServerCredentials;
import com.filenet.wcm.toolkit.util.WcmEncodingUtil;
import com.filenet.wcm.toolkit.util.WcmException;

import filenet.vw.api.VWSession;

/**
 * AdobeLiveCycleESStepModule class implements the initalization and event handling code for the 
 * Adobe LiveCycle step processor. This module redirects the browser to the render servlet.
 */
public class AdobeLiveCycleESStepModule extends WcmJSPModule
{    
    WcmEProcessDataProvider eProcessDataProvider;

    String queueName;   // Set in initialize() from Url parameters
    String wobNum;      // Set in initialize() from Url parameters

    /**
     *  Override, and initialize this UI module.
     *
     *  @throws Exception
     */
    public void initialize() throws Exception
    {
        WcmController controller = getController();

        eProcessDataProvider = (WcmEProcessDataProvider) queryDataProvider(WcmEProcessDataProvider.TYPE);
        if (eProcessDataProvider == null)
        {
            throw new WcmException("server.AdobeLiveCycleESStepModule.dataProviderUndefined", "WcmEProcessDataProvider undefined: {0}", getName());
        }

        // Get the URL parameters that specify which step to display and where to return when finished
        wobNum    = controller.getPageParameter(WcmParameter.WOB_NUM);
        queueName = WcmEncodingUtil.decodeLabel(controller.getPageParameter(WcmParameter.QUEUE_NAME));

        if ( (wobNum != null && wobNum.length() > 0 ) && (queueName != null && queueName.length() > 0 ) )
        {
            // Set info into data provider, the UI modules use it later to get the data
            eProcessDataProvider.setClassProperty(WcmEProcessStateKeys.KEY_WOB_NUM, wobNum );
            eProcessDataProvider.setClassProperty(WcmEProcessStateKeys.KEY_QUEUE_NAME, queueName);
        }

        // Edit this JSP file to change the rendering
        setJSP("apps/AdobeLiveCycleESStepModuleJSP.jsp");

        super.initialize();
    }

    /**
     * Retrieve the step element information from the Process Engine
     *
     * @param   request  The JSP request object HttpServletRequest
     * @param   response The JSP response object HttpServletResponse
     * @throws  Exception If there are problems retrieving data.
     */
    public void onStartPage(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        // Get the server and process engine credentials
        WcmServerCredentials _serverCredentials = getDataStore().getServerCredentials();
        VWSession _processEngineSession = _serverCredentials.getVWSession();
        
        // Store the credentials in the HTTP session object
        HttpSession _httpSession = request.getSession();
        _httpSession.setAttribute(Constants.SESSION_USERID, _serverCredentials.getUserId());
        _httpSession.setAttribute(Constants.SESSION_PASSWORD, _serverCredentials.getPassword());
        _httpSession.setAttribute(Constants.SESSION_ROUTER_URL, _processEngineSession.getRouterURL());
        _httpSession.setAttribute(Constants.SESSION_QUEUE_NAME, queueName);
        _httpSession.setAttribute(Constants.SESSION_WOB_NUM, wobNum);
        
        // Forward request to the render servlet
        response.sendRedirect(getBasePath() + Constants.RELATIVE_PATH_ADOBE_LC_ES_STEP_RENDER_SERVLET);
    }

    /** Return the HTML form name */
    public String getFormName()
    {
        return "AdobeLiveCycleESStepModule";
    }    
}