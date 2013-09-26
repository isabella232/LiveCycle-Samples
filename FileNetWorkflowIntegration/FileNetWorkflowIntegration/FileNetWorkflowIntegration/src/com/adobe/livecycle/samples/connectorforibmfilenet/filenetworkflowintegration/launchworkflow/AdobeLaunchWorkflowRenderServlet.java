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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.adobe.idp.services.SamplesConnectorforIBMFileNet_Processes_RenderFormsFromNRFileNet;
import com.adobe.livecycle.samples.connectorforibmfilenet.filenetworkflowintegration.utilities.Constants;
import com.adobe.livecycle.samples.connectorforibmfilenet.filenetworkflowintegration.utilities.HttpServletUtilities;
import com.adobe.livecycle.samples.connectorforibmfilenet.filenetworkflowintegration.utilities.LiveCycleESServices;
import com.filenet.wcm.api.BaseObject;
import com.filenet.wcm.api.ObjectFactory;
import com.filenet.wcm.api.ObjectStore;
import com.filenet.wcm.api.Property;
import com.filenet.wcm.api.PropertyNotFoundException;
import com.filenet.wcm.api.Session;
import com.filenet.wcm.api.TransportInputStream;
import com.filenet.wcm.api.VersionSeries;


public class AdobeLaunchWorkflowRenderServlet extends HttpServlet
{
    private static final long serialVersionUID = 7823034395657765490L;    
    
    static final String PATH_SEPARATOR = "/";
    
    // Servlet request attributes
    private static final String OBJECT_STORE_NAME = "objectStoreName";
    private static final String FOLDER_ID = "folderId";
    private static final String UT = "ut";
    private static final String VS_ID = "vsId";

    // Session identifiers
    private static final String APP_USER_TOKEN = "UserToken";
    private static final String APP_FILENET_WORKFLOW_INTEGRATION = "FileNetWorkflowIntegrationApp";

    private static final String FILE_NAME = "FILE_NAME";
    private static final String FOLDER_PATH = "FOLDER_PATH";
    private static final String FILE_PATH = "FILE_PATH";
    
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
            System.out.println("Enter: AdobeLaunchWorkflowRenderServlet");
            
            // Step: Extract request parameters
            String _objectStoreName = aRequest.getParameter(OBJECT_STORE_NAME);
            String _verSeriesID = aRequest.getParameter(VS_ID);
            String _userToken = aRequest.getParameter(UT);
            String _folderID = aRequest.getParameter(FOLDER_ID);

            // Step: Prepare invocation parameters
            // User name and password
            Session _session = ObjectFactory.getSession(APP_USER_TOKEN);
            Map _tokenMap = _session.fromToken(_userToken);
            String _userID = (String)_tokenMap.get(Session.USERID);
            String _password = (String)_tokenMap.get(Session.PASSWORD);

            // FolderPath and XDP File Name
            java.util.Map _fileDetailsMap = getFileDetails(_userID, _password, _objectStoreName, _folderID, _verSeriesID);
            String _folderPath = (String) _fileDetailsMap.get(FOLDER_PATH);
            String _xdpFileName = (String) _fileDetailsMap.get(FILE_NAME);
            String _xdpFilePath = (String) _fileDetailsMap.get(FILE_PATH);

            // Data Document
            // Note: To prepopulate the xdp with data, pass a non-NULL value
            com.adobe.idp.services.BLOB _dataDocument = null;

            // Submit URL
            String _submitURL = HttpServletUtilities.getAbsoluteURLForServlet(aRequest, 
                    Constants.RELATIVE_PATH_ADOBE_LAUNCH_WORKFLOW_SUBMIT_SERVLET);
            
            // Step: Invoke RenderFormsFromNRFilenet Service
            SamplesConnectorforIBMFileNet_Processes_RenderFormsFromNRFileNet _renderFormServiceClient = 
                LiveCycleESServices.getRenderFormsFromNRFilenetService(_userID, _password);
            
            System.out.println("Invoking Reader Enabled Forms With Data Service with arguments:");
            System.out.println("userID: " + _userID);
            System.out.println("\tObject Store Name=" + _objectStoreName);
            System.out.println("\tFolder Path=" + _folderPath);
            System.out.println("\tXDP _xdpFilePath=" + _xdpFilePath );
            System.out.println("\tData Document= " + _dataDocument);
            System.out.println("\tSubmit URL= " + _submitURL);

            com.adobe.idp.services.BLOB _renderedDocument =  
                _renderFormServiceClient.invoke(_submitURL, _dataDocument, _folderPath, _xdpFilePath);
            System.out.println("Invocation Result BLOB=" + _renderedDocument);
            
            // Step: Save parameters in session for invocation of AdobeLaunchFileNetWorkflowSubmit servlet 
            HttpSession httpSession = aRequest.getSession();
            httpSession.setAttribute(Constants.SESSION_USERID, _userID);
            httpSession.setAttribute(Constants.SESSION_PASSWORD, _password);
            httpSession.setAttribute(Constants.SESSION_OBJECT_STORE_NAME, _objectStoreName);
            httpSession.setAttribute(Constants.SESSION_FOLDER_PATH, _folderPath);
            httpSession.setAttribute(Constants.SESSION_XDP_FILE_NAME, _xdpFileName);

            // Step: Write document to response stream
            String remoteUrl = _renderedDocument.getRemoteURL();
            aResponse.sendRedirect(remoteUrl);
            System.out.println("Exit: AdobeLaunchWorkflowRenderServlet");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            aResponse.getOutputStream().write(("The following exception occurred: " + e.getMessage()).getBytes());
        }
    }

    /** Returns the details for the file on which the click ACTION was executed */
    private java.util.Map getFileDetails(String userName, String password, 
            String objectStoreName, String folderID, String verSeriesID)
    throws PropertyNotFoundException, IOException
    {
        java.util.Map _fileDetailsMap = new java.util.HashMap();
        Session _session;
        
        String formTemplateURL = null;
        String formTemplateNameInLCRepository = null;
        String formTemplatePathInLCRepository = null;

        _session = ObjectFactory.getSession(APP_FILENET_WORKFLOW_INTEGRATION, null, userName, password);
        _session.verify();

        // Fetch Asset Object from FileNet Repository.
        ObjectStore objectStore = ObjectFactory.getObjectStore(objectStoreName, _session);
        VersionSeries versionSeries = (VersionSeries) objectStore.getObject(BaseObject.TYPE_VERSIONSERIES, verSeriesID);
        versionSeries.refresh(new String[] { Property.CURRENT_VERSION });
        com.filenet.wcm.api.Document currentDoc = versionSeries.getReleasedVersion();
        TransportInputStream assetObjectStream = currentDoc.getContent();
        InputStream inputStream = assetObjectStream.getContentStream();

        List dataBytesOfAssetObjectList = new ArrayList();
        byte[] byteArray = new byte[1024*1024];
        while(true)
        {
            int len = inputStream.read(byteArray);
            if(len==-1)
                break;
            for (int i = 0; i < len; i++)
            {
                dataBytesOfAssetObjectList.add(Byte.toString(byteArray[i]));
            }
        }

        byte[] dataBytesOfAssetObject = new byte[dataBytesOfAssetObjectList.size()];
        for (int i = 0; i < dataBytesOfAssetObject.length; i++)
        {
            dataBytesOfAssetObject[i] = (Byte.parseByte((String)dataBytesOfAssetObjectList.get(i)));
        }
        
        formTemplateURL = new String(dataBytesOfAssetObject);

        if (formTemplateURL != null)
        {
            formTemplateNameInLCRepository = getXdpNameFromURL(formTemplateURL);
            formTemplatePathInLCRepository = getXdpFolderPathFromURL(formTemplateURL);
        }
        
      _fileDetailsMap.put(FOLDER_PATH, formTemplatePathInLCRepository);
      _fileDetailsMap.put(FILE_NAME, formTemplateNameInLCRepository);
      _fileDetailsMap.put(FILE_PATH, formTemplateURL);
      
        return _fileDetailsMap;
    }
    
    private String getXdpNameFromURL(String formTemplateURL)
    {
        int lastIndexOfPathSeperator = formTemplateURL.lastIndexOf(AdobeLaunchWorkflowRenderServlet.PATH_SEPARATOR);
        String formTemplateName = formTemplateURL.substring(lastIndexOfPathSeperator + 1, formTemplateURL
        .length());
        return formTemplateName;
    }

    private String getXdpFolderPathFromURL(String formTemplateURL)
    {
        int lastIndexOfPathSeperator = formTemplateURL.lastIndexOf(AdobeLaunchWorkflowRenderServlet.PATH_SEPARATOR);

        String formTemplateFolderPath = formTemplateURL.substring(0, lastIndexOfPathSeperator);
        return formTemplateFolderPath;
    }

}